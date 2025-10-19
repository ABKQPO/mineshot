package info.ata4.minecraft.mineshot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.ata4.minecraft.mineshot.client.OrthoViewHandler;
import info.ata4.minecraft.mineshot.client.ScreenshotHandler;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;

@Mod(
    modid = Mineshot.ID,
    name = Mineshot.NAME,
    version = Mineshot.VERSION,
    useMetadata = true,
    guiFactory = "info.ata4.minecraft.mineshot.client.config.MineshotConfigGuiFactory")
public class Mineshot {

    public static final String NAME = Tags.MODNAME;
    public static final String ID = Tags.MODID;
    public static final String VERSION = Tags.VERSION;

    @Instance(ID)
    public static Mineshot instance;

    private ModMetadata metadata;
    private MineshotConfig config;

    public MineshotConfig getConfig() {
        return config;
    }

    public ModMetadata getMetadata() {
        return metadata;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equals(ID)) {
            config.update(false);
        }
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent evt) {
        config = new MineshotConfig(new Configuration(evt.getSuggestedConfigurationFile()));
        metadata = evt.getModMetadata();
    }

    @EventHandler
    public void onInit(FMLInitializationEvent evt) {
        ScreenshotHandler sch = new ScreenshotHandler(config);
        FMLCommonHandler.instance()
            .bus()
            .register(sch);

        OrthoViewHandler ovh = new OrthoViewHandler();
        FMLCommonHandler.instance()
            .bus()
            .register(ovh);
        MinecraftForge.EVENT_BUS.register(ovh);

        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }
}
