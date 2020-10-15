package net.gudenau.minecraft.skullhunt.mixin;

import net.gudenau.minecraft.skullhunt.SkullHunt;
import net.gudenau.minecraft.skullhunt.SkullHuntCCA;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin{
    @Shadow protected abstract void updateScores(ScoreboardCriterion criterion, int score);
    
    private int gud_skullhunt$lastSkulls = -1;
    
    @Inject(
        method ="playerTick",
        at = @At("TAIL")
    )
    private void playerTick(CallbackInfo ci){
        int skulls = SkullHuntCCA.PLAYER_TRACKER.get(this).getSkullCount();
        if(skulls != gud_skullhunt$lastSkulls){
            gud_skullhunt$lastSkulls = skulls;
            updateScores(SkullHunt.Scoreboard.SKULLS, skulls);
        }
    }
}
