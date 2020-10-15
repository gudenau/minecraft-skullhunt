package net.gudenau.minecraft.skullhunt;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.gudenau.minecraft.skullhunt.cca.SkullHuntPlayerTracker;
import net.gudenau.minecraft.skullhunt.cca.SkullHuntPlayerTrackerImpl;
import net.gudenau.minecraft.skullhunt.cca.SkullHuntTracker;
import net.gudenau.minecraft.skullhunt.cca.SkullHuntTrackerImpl;
import net.minecraft.util.Identifier;

import static net.gudenau.minecraft.skullhunt.SkullHunt.MOD_ID;

public class SkullHuntCCA implements EntityComponentInitializer, ChunkComponentInitializer{
    public static final ComponentKey<SkullHuntTracker> TRACKER =
        ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MOD_ID, "tracker"), SkullHuntTracker.class);
    public static final ComponentKey<SkullHuntPlayerTracker> PLAYER_TRACKER =
        ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MOD_ID, "player_tracker"), SkullHuntPlayerTracker.class);
    
    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry){
        registry.register(TRACKER, (chunk)->new SkullHuntTrackerImpl());
    }
    
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry){
        registry.registerForPlayers(PLAYER_TRACKER, SkullHuntPlayerTrackerImpl::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
