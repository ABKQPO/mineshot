package info.ata4.minecraft.mineshot.mixins.early;

import net.minecraft.client.renderer.EntityRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface AccessorEntityRenderer {

    @Accessor("cameraZoom")
    void setCameraZoom(double zoom);

    @Accessor("cameraZoom")
    double getCameraZoom();

    @Accessor("cameraYaw")
    void setCameraYaw(double offset);

    @Accessor("cameraYaw")
    double getCameraYaw();

    @Accessor("cameraPitch")
    void setCameraPitch(double offset);

    @Accessor("cameraPitch")
    double getCameraPitch();
}
