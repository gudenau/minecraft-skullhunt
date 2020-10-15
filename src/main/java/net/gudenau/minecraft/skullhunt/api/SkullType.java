package net.gudenau.minecraft.skullhunt.api;

public enum SkullType{
    SKELETON,
    WITHER_SKELETON,
    ZOMBIE,
    CREEPER,
    DRAGON;
    
    public static SkullType valueOf(int index){
        return values()[index];
    }
}
