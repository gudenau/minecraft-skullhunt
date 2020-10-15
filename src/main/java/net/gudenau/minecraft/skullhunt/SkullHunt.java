package net.gudenau.minecraft.skullhunt;

import java.util.function.Supplier;
import net.fabricmc.api.ModInitializer;
import net.gudenau.minecraft.skullhunt.block.SkullHuntBlock;
import net.gudenau.minecraft.skullhunt.block.entity.SkullHuntBlockEntity;
import net.gudenau.minecraft.skullhunt.mixin.ScoreboardCriterionAccessor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class SkullHunt implements ModInitializer{
    public static final String MOD_ID = "gud_skullhunt";
    
    @Override
    public void onInitialize(){
        Blocks.init();
        Blocks.Entities.init();
        Items.init();
        Stats.init();
        Scoreboard.init();
    }
    
    public static final class Blocks{
        public static final Block SKULL = new SkullHuntBlock(AbstractBlock.Settings.of(Material.SUPPORTED).strength(1.0F));
    
        private static void init(){
            register("skull", SKULL);
        }
        
        private static void register(String name, Block block){
            Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
        }
        
        public static final class Entities{
            public static final BlockEntityType<SkullHuntBlockEntity> SKULL = create(SkullHuntBlockEntity::new, Blocks.SKULL);
    
            private static void init(){
                register("skull", SKULL);
            }
            
            private static <T extends BlockEntity> BlockEntityType<T> create(Supplier<T> type, Block block){
                return BlockEntityType.Builder.create(type, block).build(null);
            }
    
            private static void register(String name, BlockEntityType<?> type){
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, name), type);
            }
        }
    }
    
    public static final class Items{
        public static final BlockItem SKULL = new BlockItem(Blocks.SKULL, new Item.Settings().group(ItemGroup.DECORATIONS).rarity(Rarity.UNCOMMON));
    
        private static void init(){
            register(SKULL);
        }
        
        private static void register(BlockItem item){
            register(Registry.BLOCK.getId(item.getBlock()), item);
        }
        
        private static void register(Identifier identifier, Item item){
            Registry.register(Registry.ITEM, identifier, item);
        }
    }
    
    public static final class Stats{
        public static final Identifier SKULLS = new Identifier(MOD_ID, "skulls");
        
        private static void init(){
            register(SKULLS);
        }
        
        private static void register(Identifier identifier){
            register(identifier, StatFormatter.DEFAULT);
        }
        
        private static void register(Identifier identifier, StatFormatter formatter){
            Registry.register(Registry.CUSTOM_STAT, identifier, identifier);
            net.minecraft.stat.Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        }
    }
    
    public static final class Scoreboard{
        public static ScoreboardCriterion SKULLS;
    
        private static void init(){
            SKULLS = ScoreboardCriterionAccessor.invokeInit("gud_skullhunt_skulls", true, ScoreboardCriterion.RenderType.INTEGER);
        }
    }
}
