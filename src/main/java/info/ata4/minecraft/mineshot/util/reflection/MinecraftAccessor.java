package info.ata4.minecraft.mineshot.util.reflection;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class MinecraftAccessor {

    private static final Logger L = LogManager.getLogger();

    private static final String[] FIELD_TIMER = new String[] { "timer", "field_71428_T" };
    private static final String[] METHOD_RESIZE = new String[] { "resize", "func_71370_a" };

    private MinecraftAccessor() {}

    public static Timer getTimer(Minecraft mc) {
        try {
            return ReflectionHelper.getPrivateValue(Minecraft.class, mc, FIELD_TIMER);
        } catch (Exception ex) {
            L.error("getTimer() failed", ex);
            return null;
        }
    }

    public static void resize(Minecraft mc, int width, int height) {
        try {
            Method resize = ReflectionHelper.findMethod(Minecraft.class, mc, METHOD_RESIZE, Integer.TYPE, Integer.TYPE);
            resize.invoke(mc, width, height);
        } catch (Exception ex) {
            L.error("resize() failed", ex);
        }
    }
}
