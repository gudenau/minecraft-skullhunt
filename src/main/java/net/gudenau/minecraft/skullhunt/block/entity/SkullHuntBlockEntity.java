package net.gudenau.minecraft.skullhunt.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.gudenau.minecraft.skullhunt.SkullHunt;
import net.gudenau.minecraft.skullhunt.api.SkullType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class SkullHuntBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable{
    private int rotation = 0;
    private SkullType type = SkullType.ZOMBIE;
    private int ticksPowered;
    private boolean powered;
    
    public SkullHuntBlockEntity(){
        super(SkullHunt.Blocks.Entities.SKULL);
    }
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag){
        super.fromTag(state, tag);
        fromClientTag(tag);
    }
    
    @Override
    public CompoundTag toTag(CompoundTag tag){
        return toClientTag(super.toTag(tag));
    }
    
    @Override
    public void fromClientTag(CompoundTag tag){
        rotation = tag.getInt("rotation");
        type = SkullType.valueOf(tag.getInt("type"));
    }
    
    @Override
    public CompoundTag toClientTag(CompoundTag tag){
        tag.putInt("rotation", rotation);
        tag.putInt("type", type.ordinal());
        return tag;
    }
    
    @Override
    public void tick(){
        if(type == SkullType.DRAGON){
            if(world.isReceivingRedstonePower(pos)){
                powered = true;
                ticksPowered++;
            }else{
                powered = false;
            }
        }
    }
    
    public int getRotation(){
        return rotation;
    }
    
    public SkullType getSkullType(){
        return type;
    }
    
    public void setType(SkullType type){
        this.type = type;
        markDirty();
    }
    
    public void setRotation(int rotation){
        this.rotation = rotation;
        markDirty();
    }
    
    @Environment(EnvType.CLIENT)
    public float getTicksPowered(float tickDelta){
        return powered ? ticksPowered + tickDelta : ticksPowered;
    }
}
