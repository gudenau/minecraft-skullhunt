package net.gudenau.minecraft.skullhunt.renderer;

import net.gudenau.minecraft.skullhunt.api.SkullType;
import net.gudenau.minecraft.skullhunt.block.entity.SkullHuntBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.DragonHeadEntityModel;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.entity.model.SkullOverlayEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class SkullHuntBlockEntityRenderer extends BlockEntityRenderer<SkullHuntBlockEntity>{
    private static final Identifier[] TEXTURES = new Identifier[]{
        new Identifier("textures/entity/skeleton/skeleton.png"),
        new Identifier("textures/entity/skeleton/wither_skeleton.png"),
        new Identifier("textures/entity/zombie/zombie.png"),
        new Identifier("textures/entity/creeper/creeper.png"),
        new Identifier("textures/entity/enderdragon/dragon.png")
    };
    private static final SkullEntityModel[] MODELS;
    static{
        SkullEntityModel shortTexture = new SkullEntityModel(0, 0, 64, 32);
        SkullEntityModel tallTexture = new SkullOverlayEntityModel();
        
        MODELS = new SkullEntityModel[]{
            // Skeleton
            shortTexture,
            // Wither Skeleton
            shortTexture,
            // Zombie
            tallTexture,
            // Creeper
            shortTexture,
            // Dragon
            new DragonHeadEntityModel(0.0F)
        };
    }
    
    public SkullHuntBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher){
        super(dispatcher);
    }
    
    public static void renderItem(ItemStack stack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light){
        SkullType type = SkullType.valueOf(stack.getOrCreateTag().getInt("SkullType"));
        Identifier texture = TEXTURES[type.ordinal()];
        SkullEntityModel model = MODELS[type.ordinal()];
        
        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.scale(-1, -1, 1);
        model.method_2821(0, 180, 0);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(texture));
        model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrixStack.pop();
    }
    
    @Override
    public void render(SkullHuntBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        SkullType type = entity.getSkullType();
        Direction surface = entity.getCachedState().get(Properties.FACING);
        Identifier texture = TEXTURES[type.ordinal()];
        SkullEntityModel model = MODELS[type.ordinal()];
        int rotation = entity.getRotation();
        
        matrices.push();
    
        matrices.translate(
            surface.getOffsetX() * 0.25 + 0.5,
            surface.getOffsetY() * 0.25 + 0.25,
            surface.getOffsetZ() * 0.25 + 0.5
        );
        matrices.scale(-1, -1, 1);
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(texture));
        model.method_2821(
            entity.getTicksPowered(tickDelta),
            22.5F * rotation,
            0
        );
        model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        matrices.pop();
    }
}
