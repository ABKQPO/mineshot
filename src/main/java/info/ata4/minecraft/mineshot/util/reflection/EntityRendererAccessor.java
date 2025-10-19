package info.ata4.minecraft.mineshot.util.reflection;

import net.minecraft.client.renderer.EntityRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class EntityRendererAccessor {

    private static final Logger L = LogManager.getLogger();

    private static final String[] FIELD_CAMERA_ZOOM = new String[] { "cameraZoom", "field_78503_V" };
    private static final String[] FIELD_CAMERA_YAW = new String[] { "cameraYaw", "field_78502_W" };
    private static final String[] FIELD_CAMERA_PITCH = new String[] { "cameraPitch", "field_78509_X" };

    private EntityRendererAccessor() {}

    public static void setCameraZoom(EntityRenderer renderer, double zoom) {
        try {
            ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, zoom, FIELD_CAMERA_ZOOM);
        } catch (Exception ex) {
            L.error("setCameraZoom() failed", ex);
        }
    }

    public static double getCameraZoom(EntityRenderer renderer) {
        try {
            return ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, FIELD_CAMERA_ZOOM);
        } catch (Exception ex) {
            L.error("getCameraZoom() failed", ex);
            return 0;
        }
    }

    public static void setCameraOffsetX(EntityRenderer renderer, double offset) {
        try {
            ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, offset, FIELD_CAMERA_YAW);
        } catch (Exception ex) {
            L.error("setCameraOffsetX() failed", ex);
        }
    }

    public static double getCameraOffsetX(EntityRenderer renderer) {
        try {
            return ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, FIELD_CAMERA_YAW);
        } catch (Exception ex) {
            L.error("getCameraOffsetX() failed", ex);
            return 0;
        }
    }

    public static void setCameraOffsetY(EntityRenderer renderer, double offset) {
        try {
            ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, offset, FIELD_CAMERA_PITCH);
        } catch (Exception ex) {
            L.error("setCameraOffsetY() failed", ex);
        }
    }

    public static double getCameraOffsetY(EntityRenderer renderer) {
        try {
            return ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, FIELD_CAMERA_PITCH);
        } catch (Exception ex) {
            L.error("getCameraOffsetY() failed", ex);
            return 0;
        }
    }
}
