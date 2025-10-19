package info.ata4.minecraft.mineshot.client.capture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.util.Dimension;

public class FramebufferWriter {

    protected static int HEADER_SIZE = 18;

    protected FramebufferCapturer fbc;
    protected File file;

    public FramebufferWriter(File file, FramebufferCapturer fbc) {
        this.file = file;
        this.fbc = fbc;
    }

    public void write() throws IOException {
        fbc.setFlipColors(false);
        fbc.setFlipLines(true);
        fbc.capture();

        Dimension dim = fbc.getCaptureDimension();
        int width = dim.getWidth();
        int height = dim.getHeight();
        int bpp = fbc.getBytesPerPixel(); // should be 3 (RGB)

        ByteBuffer buffer = fbc.getByteBuffer();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        byte[] pixels = new byte[width * height * bpp];
        buffer.get(pixels);

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = pixels[index++] & 0xFF;
                int g = pixels[index++] & 0xFF;
                int b = pixels[index++] & 0xFF;
                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);
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
    }

    protected ByteBuffer buildTargaHeader(int width, int height, int bpp) {
        ByteBuffer bb = ByteBuffer.allocate(HEADER_SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.position(2);
        bb.put((byte) 2);
        bb.position(12);
        bb.putShort((short) (width & 0xffff));
        bb.putShort((short) (height & 0xffff));
        bb.put((byte) (bpp & 0xff));
        bb.rewind();
        return bb;
    }
}
