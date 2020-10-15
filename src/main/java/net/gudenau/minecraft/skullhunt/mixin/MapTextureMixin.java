package net.gudenau.minecraft.skullhunt.mixin;

import java.util.List;
import java.util.stream.Collectors;
import net.gudenau.minecraft.skullhunt.SkullHuntCCA;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.client.gui.MapRenderer$MapTexture")
public abstract class MapTextureMixin{
    @Shadow @Final private MapState mapState;
    
    @Inject(
        method = "draw",
        at = @At("TAIL"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void postDraw(
        MatrixStack stack, VertexConsumerProvider consumers, boolean renderIcons, int light,
        CallbackInfo callbackInfo,
        int j, int k, float f, Matrix4f matrix, VertexConsumer consumer, int l
    ){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null){
            return;
        }
    
        int size = 128 * (1 << mapState.scale);
        int minX = mapState.xCenter - size;
        int minZ = mapState.zCenter - size;
        int maxX = mapState.xCenter + size;
        int maxZ = mapState.zCenter + size;
    
        RegistryKey<World> dimension = mapState.dimension;
        if(dimension == null){
            dimension = player.world.getRegistryKey();
        }
        
        List<BlockPos> positions = SkullHuntCCA.PLAYER_TRACKER.get(player)
            .getSkullLocations(dimension)
            .stream()
            .filter((pos)->{
                int x = pos.getX();
                int y = pos.getY();
                return x >= minX && x <= maxX && y >= minZ && y <= maxZ;
            })
            .collect(Collectors.toList());
    
        for(BlockPos pos : positions){
            stack.push();
            
            double x = pos.getX() - minX;
            double z = pos.getZ() - minZ;
            x /= size;
            z /= size;
            
            stack.translate(
                x / 2 + 64,
                z / 2 + 64,
                -0.019999999552965164D
            );
            stack.scale(4.0F, 4.0F, 3.0F);
            stack.translate(-0.125D, 0.125D, 0.0D);
            byte b = MapIcon.Type.TARGET_X.getId();
            float g = (float)(b % 16 + 0) / 16.0F;
            float h = (float)(b / 16 + 0) / 16.0F;
            float m = (float)(b % 16 + 1) / 16.0F;
            float n = (float)(b / 16 + 1) / 16.0F;
            Matrix4f matrix4f2 = stack.peek().getModel();
            VertexConsumer vertexConsumer2 = consumers.getBuffer(MapRendererAccessor.getField_21688());
            vertexConsumer2.vertex(matrix4f2, -1.0F, 1.0F, (float)l * -0.001F).color(255, 255, 255, 255).texture(g, h).light(light).next();
            vertexConsumer2.vertex(matrix4f2, 1.0F, 1.0F, (float)l * -0.001F).color(255, 255, 255, 255).texture(m, h).light(light).next();
            vertexConsumer2.vertex(matrix4f2, 1.0F, -1.0F, (float)l * -0.001F).color(255, 255, 255, 255).texture(m, n).light(light).next();
            vertexConsumer2.vertex(matrix4f2, -1.0F, -1.0F, (float)l * -0.001F).color(255, 255, 255, 255).texture(g, n).light(light).next();
            stack.pop();
            l++;
        }
    }
}
