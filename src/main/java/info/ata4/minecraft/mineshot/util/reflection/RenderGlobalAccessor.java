package info.ata4.minecraft.mineshot.util.reflection;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class RenderGlobalAccessor {

    private static final Logger L = LogManager.getLogger();
    private static final String[] FIELD_WORLDRENDERERS = new String[] { "worldRenderers", "field_72765_l" };

    private RenderGlobalAccessor() {}

    public static WorldRenderer[] getWorldRenderers(RenderGlobal rg) {
        try {
            return ReflectionHelper.getPrivateValue(RenderGlobal.class, rg, FIELD_WORLDRENDERERS);
        } catch (Exception ex) {
            L.error("getWordRenderers() failed", ex);
            return new WorldRenderer[] {};
        }
    }
}
