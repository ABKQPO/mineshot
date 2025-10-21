package info.ata4.minecraft.mineshot.mixins.early;

import static info.ata4.minecraft.mineshot.client.OrthoViewHandler.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "setupCameraTransform", at = @At("TAIL"))
    public void injectSetupCameraTransform(float renderPartialTicks, int pass, CallbackInfo ci) {
        if (!enabled) {
            return;
        }

        // update zoom and rotation
        if (!modifierKeyPressed()) {
            int ticksElapsed = tick - tickPrevious;
            double elapsed = ticksElapsed + (renderPartialTicks - partialPrevious);
            elapsed *= SECONDS_PER_TICK;
            updateZoomAndRotation(elapsed);

            tickPrevious = tick;
            partialPrevious = renderPartialTicks;
        }

        float width = zoom * (MC.displayWidth / (float) MC.displayHeight);
        float height = zoom;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        AccessorEntityRenderer entityRenderer = (AccessorEntityRenderer) MC.entityRenderer;

        double cameraZoom = entityRenderer.getCameraZoom();
        double cameraOfsX = entityRenderer.getCameraYaw();
        double cameraOfsY = entityRenderer.getCameraPitch();

        if (cameraZoom != 1) {
            glTranslated(cameraOfsX, -cameraOfsY, 0);
            glScaled(cameraZoom, cameraZoom, 1);
        }

        glOrtho(-width, width, -height, height, clip ? 0 : -9999, 9999);

        if (freeCam) {
            // rotate the orthographic camera with the player view
            xRot = MC.thePlayer.rotationPitch;
            yRot = MC.thePlayer.rotationYaw - 180;
        }

        // set camera rotation
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(xRot, 1, 0, 0);
        glRotatef(yRot, 0, 1, 0);

        // fix particle rotation
        if (!freeCam) {
            float pitch = xRot;
            float yaw = yRot + 180;
            ActiveRenderInfo.rotationX = MathHelper.cos(yaw * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationZ = MathHelper.sin(yaw * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationYZ = -ActiveRenderInfo.rotationZ * MathHelper.sin(pitch * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationXY = ActiveRenderInfo.rotationX * MathHelper.sin(pitch * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationXZ = MathHelper.cos(pitch * (float) Math.PI / 180f);
        }
    }
}
