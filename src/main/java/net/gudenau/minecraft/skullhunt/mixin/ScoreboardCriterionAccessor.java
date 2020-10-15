package net.gudenau.minecraft.skullhunt.mixin;

import java.util.Optional;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScoreboardCriterion.class)
public interface ScoreboardCriterionAccessor{
    @Invoker("<init>") static ScoreboardCriterion invokeInit(String name, boolean readOnly, ScoreboardCriterion.RenderType renderType){return null;}
    @Invoker static Optional<ScoreboardCriterion> invokeCreateStatCriterion(StatType<?> statType, Identifier id){return null;}
}
