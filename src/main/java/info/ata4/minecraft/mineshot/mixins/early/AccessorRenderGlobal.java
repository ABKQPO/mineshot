package info.ata4.minecraft.mineshot.mixins.early;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderGlobal.class)
public interface AccessorRenderGlobal {

    @Accessor("worldRenderers")
    WorldRenderer[] getWorldRenderers();
}
