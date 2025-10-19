package info.ata4.minecraft.mineshot.client.capture.task;

import java.io.File;
import java.io.IOException;

import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import info.ata4.minecraft.mineshot.client.capture.FramebufferCapturer;
import info.ata4.minecraft.mineshot.client.capture.FramebufferTiledWriter;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;

public class CaptureTiledTask implements RenderTickTask {

    private final MineshotConfig config;
    private final File file;

    public CaptureTiledTask(MineshotConfig config, File file) {
        this.config = config;
        this.file = file;
    }

    @Override
    public boolean onRenderTick(RenderTickEvent evt) throws IOException {
        int width = config.captureWidth.get();
        int height = config.captureHeight.get();

        FramebufferCapturer fbc = new FramebufferCapturer();
        FramebufferTiledWriter fbw = new FramebufferTiledWriter(file, fbc, width, height);
        fbw.write();

        return true;
    }

}
