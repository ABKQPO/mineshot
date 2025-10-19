package info.ata4.minecraft.mineshot.client.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.config.GuiConfig;
import info.ata4.minecraft.mineshot.Mineshot;

public class MineshotConfigGui extends GuiConfig {

    private static String getTitle() {
        Configuration cfg = Mineshot.instance.getConfig()
            .getConfiguration();
        return GuiConfig.getAbridgedConfigPath(cfg.toString());
    }

    public MineshotConfigGui(GuiScreen parentScreen) {
        // telescoping into space while static methods prevent worse.
        // thanks for nothing, IModGui"Factory"...
        super(
            parentScreen,
            Mineshot.instance.getConfig()
                .getConfigElements(),
            Mineshot.ID,
            false,
            false,
            getTitle());
    }

}
