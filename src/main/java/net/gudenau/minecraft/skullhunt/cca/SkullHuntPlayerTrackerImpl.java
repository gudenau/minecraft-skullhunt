package net.gudenau.minecraft.skullhunt.cca;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.gudenau.minecraft.skullhunt.SkullHunt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SkullHuntPlayerTrackerImpl implements SkullHuntPlayerTracker{
    private final Map<RegistryKey<World>, Set<BlockPos>> locations = new HashMap<>();
    private final PlayerEntity player;
    private int skullCount;
    
    public SkullHuntPlayerTrackerImpl(PlayerEntity player){
        this.player = player;
    }
    
    @Override
    public Set<BlockPos> getSkullLocations(RegistryKey<World> dimension){
        return locations.computeIfAbsent(dimension, (dim)->new HashSet<>());
    }
    
    @Override
    public boolean addSkullLocation(RegistryKey<World> dimension, BlockPos pos){
        if(locations.computeIfAbsent(dimension, (dim)->new HashSet<>()).add(pos)){
            skullCount++;
            refreshStats();
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public void removeSkullLocation(RegistryKey<World> dimension, BlockPos pos){
        if(locations.computeIfAbsent(dimension, (dim)->new HashSet<>()).remove(pos)){
            skullCount--;
            refreshStats();
        }
    }
    
    @Override
    public int getSkullCount(){
        return skullCount;
    }
    
    private void refreshStats(){
        Stat<?> stat = Stats.CUSTOM.getOrCreateStat(SkullHunt.Stats.SKULLS);
        if(player instanceof ServerPlayerEntity){
            ServerPlayerEntity player = (ServerPlayerEntity)this.player;
            ServerStatHandler statHandler = player.getStatHandler();
            statHandler.setStat(player, stat, skullCount);
        }else{
            player.resetStat(stat);
            player.increaseStat(stat, skullCount);
        }
    }
    
    @Override
    public void readFromNbt(CompoundTag compoundTag){
        locations.clear();
        skullCount = 0;
        for(String dim : compoundTag.getKeys()){
            Set<BlockPos> positions = new HashSet<>();
            for(long position : compoundTag.getLongArray(dim)){
                positions.add(BlockPos.fromLong(position));
                skullCount++;
            }
            locations.put(RegistryKey.of(Registry.DIMENSION, new Identifier(dim)), positions);
        }
        refreshStats();
    }
    
    @Override
    public void writeToNbt(CompoundTag compoundTag){
        for(Map.Entry<RegistryKey<World>, Set<BlockPos>> entry : locations.entrySet()){
            Set<BlockPos> locations = entry.getValue();
            long[] positions = new long[locations.size()];
            int index = 0;
            for(BlockPos position : locations){
                positions[index++] = position.asLong();
            }
            compoundTag.putLongArray(entry.getKey().getValue().toString(), positions);
        }
    }
}
