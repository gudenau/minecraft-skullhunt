package net.gudenau.minecraft.skullhunt.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import java.util.Set;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public interface SkullHuntPlayerTracker extends ComponentV3{
    Set<BlockPos> getSkullLocations(RegistryKey<World> dimension);
    boolean addSkullLocation(RegistryKey<World> dimension, BlockPos pos);
    void removeSkullLocation(RegistryKey<World> dimension, BlockPos pos);
    int getSkullCount();
}
