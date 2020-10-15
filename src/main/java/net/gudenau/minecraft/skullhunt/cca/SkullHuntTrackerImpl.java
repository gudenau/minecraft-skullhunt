package net.gudenau.minecraft.skullhunt.cca;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

public class SkullHuntTrackerImpl implements SkullHuntTracker{
    private final Set<BlockPos> locations = new HashSet<>();
    
    @Override
    public Set<BlockPos> getSkullLocations(){
        return locations;
    }
    
    @Override
    public boolean addSkullLocation(BlockPos pos){
        return locations.add(pos);
    }
    
    @Override
    public void removeSkullLocation(BlockPos pos){
        locations.remove(pos);
    }
    
    @Override
    public void readFromNbt(CompoundTag compoundTag){
        long[] positions = compoundTag.getLongArray("locations");
        locations.clear();
        for(long position : positions){
            locations.add(BlockPos.fromLong(position));
        }
    }
    
    @Override
    public void writeToNbt(CompoundTag compoundTag){
        long[] positions = new long[locations.size()];
        int index = 0;
        for(BlockPos position : locations){
            positions[index++] = position.asLong();
        }
        compoundTag.putLongArray("locations", positions);
    }
}
