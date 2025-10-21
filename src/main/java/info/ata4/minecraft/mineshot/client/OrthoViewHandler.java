package info.ata4.minecraft.mineshot.client;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import info.ata4.minecraft.mineshot.client.util.ChatUtils;

public class OrthoViewHandler {

    public static Minecraft MC = Minecraft.getMinecraft();
    public static String KEY_CATEGORY = "key.categories.mineshot";
    public static double ZOOM_STEP = 0.5f;
    public static double ROTATE_STEP = 15;
    public static double SECONDS_PER_TICK = 1f / 20f;

    public static KeyBinding keyToggle = new KeyBinding(
        "key.mineshot.ortho.toggle",
        Keyboard.KEY_NUMPAD5,
        KEY_CATEGORY);
    public static KeyBinding keyZoomIn = new KeyBinding("key.mineshot.ortho.zoom_in", Keyboard.KEY_ADD, KEY_CATEGORY);
    public static KeyBinding keyZoomOut = new KeyBinding(
        "key.mineshot.ortho.zoom_out",
        Keyboard.KEY_SUBTRACT,
        KEY_CATEGORY);
    public static KeyBinding keyRotateL = new KeyBinding(
        "key.mineshot.ortho.rotate_l",
        Keyboard.KEY_NUMPAD4,
        KEY_CATEGORY);
    public static KeyBinding keyRotateR = new KeyBinding(
        "key.mineshot.ortho.rotate_r",
        Keyboard.KEY_NUMPAD6,
        KEY_CATEGORY);
    public static KeyBinding keyRotateU = new KeyBinding(
        "key.mineshot.ortho.rotate_u",
        Keyboard.KEY_NUMPAD8,
        KEY_CATEGORY);
    public static KeyBinding keyRotateD = new KeyBinding(
        "key.mineshot.ortho.rotate_d",
        Keyboard.KEY_NUMPAD2,
        KEY_CATEGORY);
    public static KeyBinding keyRotateT = new KeyBinding(
        "key.mineshot.ortho.rotate_t",
        Keyboard.KEY_NUMPAD7,
        KEY_CATEGORY);
    public static KeyBinding keyRotateF = new KeyBinding(
        "key.mineshot.ortho.rotate_f",
        Keyboard.KEY_NUMPAD1,
        KEY_CATEGORY);
    public static KeyBinding keyRotateS = new KeyBinding(
        "key.mineshot.ortho.rotate_s",
        Keyboard.KEY_NUMPAD3,
        KEY_CATEGORY);
    public static KeyBinding keyClip = new KeyBinding("key.mineshot.ortho.clip", Keyboard.KEY_MULTIPLY, KEY_CATEGORY);

    public static boolean enabled;
    public static boolean freeCam;
    public static boolean clip;

    public static float zoom;
    public static float xRot;
    public static float yRot;

    public static int tick;
    public static int tickPrevious;
    public static double partialPrevious;

    public OrthoViewHandler() {
        ClientRegistry.registerKeyBinding(keyToggle);
        ClientRegistry.registerKeyBinding(keyZoomIn);
        ClientRegistry.registerKeyBinding(keyZoomOut);
        ClientRegistry.registerKeyBinding(keyRotateL);
        ClientRegistry.registerKeyBinding(keyRotateR);
        ClientRegistry.registerKeyBinding(keyRotateU);
        ClientRegistry.registerKeyBinding(keyRotateD);
        ClientRegistry.registerKeyBinding(keyRotateT);
        ClientRegistry.registerKeyBinding(keyRotateF);
        ClientRegistry.registerKeyBinding(keyRotateS);
        ClientRegistry.registerKeyBinding(keyClip);

        reset();
    }

    public static void reset() {
        freeCam = false;
        clip = false;

        zoom = 8;
        xRot = 30;
        yRot = -45;
        tick = 0;
        tickPrevious = 0;
        partialPrevious = 0;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void enable() {
        if (!enabled) {
            reset();

            // disable in multiplayer
            // Of course, programmers could just delete this check and abuse the
            // orthographic camera, but at least the official build won't support it
            if (!MC.isSingleplayer()) {
                ChatUtils.print("mineshot.orthomp");
                return;
            }
        }

        enabled = true;
    }

    public static void disable() {
        enabled = false;
    }

    public static void toggle() {
        if (isEnabled()) {
            disable();
        } else {
            enable();
        }
    }

    public static boolean modifierKeyPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyInput(InputEvent.KeyInputEvent evt) {
        boolean mod = modifierKeyPressed();

        if (keyToggle.isPressed()) {
            if (mod) {
                freeCam = !freeCam;
            } else {
                toggle();
            }
        } else if (keyClip.isPressed()) {
            clip = !clip;
        } else if (keyRotateT.getIsKeyPressed()) {
            xRot = mod ? -90 : 90;
            yRot = 0;
        } else if (keyRotateF.getIsKeyPressed()) {
            xRot = 0;
            yRot = mod ? -90 : 90;
        } else if (keyRotateS.getIsKeyPressed()) {
            xRot = 0;
            yRot = mod ? 180 : 0;
        }

        if (mod) {
            // snap values to step units
            xRot -= xRot % ROTATE_STEP;
            yRot -= yRot % ROTATE_STEP;
            zoom -= zoom % ZOOM_STEP;

            updateZoomAndRotation(1);
        }
    }

    public static void updateZoomAndRotation(double multi) {
        if (keyZoomIn.getIsKeyPressed()) {
            zoom *= 1 - ZOOM_STEP * multi;
        } else if (keyZoomOut.getIsKeyPressed()) {
            zoom *= 1 + ZOOM_STEP * multi;
        }

        if (keyRotateL.getIsKeyPressed()) {
            yRot += ROTATE_STEP * multi;
        } else if (keyRotateR.getIsKeyPressed()) {
            yRot -= ROTATE_STEP * multi;
        }

        if (keyRotateU.getIsKeyPressed()) {
            xRot += ROTATE_STEP * multi;
        } else if (keyRotateD.getIsKeyPressed()) {
            xRot -= ROTATE_STEP * multi;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTick(ClientTickEvent evt) {
        if (!enabled) {
            return;
        }

        if (evt.phase != Phase.START) {
            return;
        }

        tick++;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFogDensity(EntityViewRenderEvent.FogDensity evt) {}
}
