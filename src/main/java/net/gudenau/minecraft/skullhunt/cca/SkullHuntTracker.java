package net.gudenau.minecraft.skullhunt.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import java.util.Set;
import net.minecraft.util.math.BlockPos;

public interface SkullHuntTracker extends ComponentV3{
    Set<BlockPos> getSkullLocations();
    boolean addSkullLocation(BlockPos pos);
    void removeSkullLocation(BlockPos pos);
}
