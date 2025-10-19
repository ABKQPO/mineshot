package info.ata4.minecraft.mineshot.client.capture;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Timer;

import org.lwjgl.util.Dimension;

import info.ata4.minecraft.mineshot.mixins.early.AccessorEntityRenderer;
import info.ata4.minecraft.mineshot.mixins.early.AccessorMinecraft;

public class FramebufferTiledWriter extends FramebufferWriter {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private final int widthTiled;
    private final int heightTiled;

    private boolean advancedOpengl;
    private boolean hideGUI;

    public FramebufferTiledWriter(File file, FramebufferCapturer fbc, int width, int height) {
        super(file, fbc);
        this.widthTiled = width;
        this.heightTiled = height;
    }

    private void modifySettings() {
        advancedOpengl = MC.gameSettings.advancedOpengl;
        MC.gameSettings.advancedOpengl = false;

        hideGUI = MC.gameSettings.hideGUI;
        MC.gameSettings.hideGUI = true;

        if (MC.theWorld != null) {
            for (Entity ent : MC.theWorld.loadedEntityList) {
                ent.ignoreFrustumCheck = true;
                ent.renderDistanceWeight = 16;
            }
        }
    }

    private void restoreSettings() {
        MC.gameSettings.hideGUI = hideGUI;
        MC.gameSettings.advancedOpengl = advancedOpengl;

        if (MC.theWorld != null) {
            for (Entity ent : MC.theWorld.loadedEntityList) {
                ent.ignoreFrustumCheck = false;
                ent.renderDistanceWeight = 1;
            }
        }
    }

    @Override
    public void write() throws IOException {
        Dimension dim = fbc.getCaptureDimension();
        int widthViewport = dim.getWidth();
        int heightViewport = dim.getHeight();
        int bpp = fbc.getBytesPerPixel();

        double tilesX = widthTiled / (double) widthViewport;
        double tilesY = heightTiled / (double) heightViewport;

        int numTilesX = (int) Math.ceil(tilesX);
        int numTilesY = (int) Math.ceil(tilesY);
        double camZoom = Math.max(tilesX, tilesY);

        AccessorEntityRenderer entityRenderer = (AccessorEntityRenderer) MC.entityRenderer;
        Timer timer = ((AccessorMinecraft) MC).getTimer();

        fbc.setFlipColors(true);
        fbc.setFlipLines(false);

        modifySettings();

        try {
            BufferedImage image = new BufferedImage(widthTiled, heightTiled, BufferedImage.TYPE_INT_RGB);
            WritableRaster raster = image.getRaster();

            for (int y = 0; y < numTilesY; y++) {
                for (int x = 0; x < numTilesX; x++) {
                    int tileWidth = Math.min(widthViewport, widthTiled - (widthViewport * x));
                    int tileHeight = Math.min(heightViewport, heightTiled - (heightViewport * y));

                    double camOfsX = (widthTiled - widthViewport - (widthViewport * x) * 2) / (double) widthViewport;
                    double camOfsY = (heightTiled - heightViewport - (heightViewport * (tilesY - y - 1)) * 2)
                        / (double) heightViewport;

                    entityRenderer.setCameraZoom(camZoom);
                    entityRenderer.setCameraYaw(camOfsX);
                    entityRenderer.setCameraPitch(camOfsY);

                    MC.entityRenderer.updateCameraAndRender(timer == null ? 0 : timer.renderPartialTicks);

                    fbc.capture();
                    ByteBuffer frameBuffer = fbc.getByteBuffer();

                    byte[] pixels = new byte[tileWidth * tileHeight * bpp];
                    frameBuffer.get(pixels, 0, tileWidth * tileHeight * bpp);

                    int index = 0;
                    for (int ty = 0; ty < tileHeight; ty++) {
                        for (int tx = 0; tx < tileWidth; tx++) {
                            int destY = heightTiled - (y * heightViewport + ty) - 1;
                            int destX = x * widthViewport + tx;

                            // BGR → RGB
                            int b = pixels[index++] & 0xFF;
                            int g = pixels[index++] & 0xFF;
                            int r = pixels[index++] & 0xFF;

                            raster.setPixel(destX, destY, new int[] { r, g, b });
                        }
                    }
                }
            }

            if (file.getName()
                .toLowerCase()
                .endsWith(".tga")) {
                String newName = file.getName()
                    .substring(
                        0,
                        file.getName()
                            .length() - 4)
                    + ".png";
                file = new File(file.getParentFile(), newName);
            }

            ImageIO.write(image, "png", file);

        } finally {
            entityRenderer.setCameraZoom(1);
            entityRenderer.setCameraYaw(0);
            entityRenderer.setCameraPitch(0);

            restoreSettings();
        }
    }
}
