package net.gudenau.minecraft.skullhunt;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.gudenau.minecraft.skullhunt.renderer.SkullHuntBlockEntityRenderer;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class SkullHuntClient implements ClientModInitializer{
    @Override
    public void onInitializeClient(){
        BlockEntityRendererRegistry.INSTANCE.register(
            SkullHunt.Blocks.Entities.SKULL,
            SkullHuntBlockEntityRenderer::new
        );
        BuiltinItemRendererRegistry.INSTANCE.register(
            SkullHunt.Blocks.SKULL,
            (stack, transformation, matrixStack, vertexConsumers, light, overlay)->{
                SkullHuntBlockEntityRenderer.renderItem(stack, matrixStack, vertexConsumers, light);
            }
        );
    }
}
