package net.gudenau.minecraft.skullhunt.mixin;

import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MapRenderer.class)
public interface MapRendererAccessor{
    @Accessor static RenderLayer getField_21688(){return null;}
}
