package info.ata4.minecraft.mineshot.client.util;

import static net.minecraft.event.ClickEvent.Action.*;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private ChatUtils() {}

    public static void print(String msg, EnumChatFormatting format, Object... args) {
        if (MC.ingameGUI == null) {
            return;
        }

        GuiNewChat chat = MC.ingameGUI.getChatGUI();
        ChatComponentTranslation ret = new ChatComponentTranslation(msg, args);
        ret.getChatStyle()
            .setColor(format);

        chat.printChatMessage(ret);
    }

    public static void print(String msg, Object... args) {
        print(msg, null, args);
    }

    public static void printFileLink(String msg, File file) {
        ChatComponentText text = new ChatComponentText(file.getName());
        String path;

        try {
            path = file.getAbsoluteFile()
                .getCanonicalPath();
        } catch (IOException ex) {
            path = file.getAbsolutePath();
        }

        text.getChatStyle()
            .setChatClickEvent(new ClickEvent(OPEN_FILE, path));
        text.getChatStyle()
            .setUnderlined(true);

        print(msg, text);
    }
}
