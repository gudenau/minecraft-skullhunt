package net.gudenau.minecraft.skullhunt.block;

import java.util.List;
import net.gudenau.minecraft.skullhunt.SkullHuntCCA;
import net.gudenau.minecraft.skullhunt.api.SkullType;
import net.gudenau.minecraft.skullhunt.block.entity.SkullHuntBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SkullHuntBlock extends Block implements BlockEntityProvider{
    private static final DirectionProperty ATTACHED_SIDE = Properties.FACING;
    private static final VoxelShape[] SHAPES = new VoxelShape[Direction.values().length];
    
    static{
        SHAPES[Direction.DOWN.getId()] = Block.createCuboidShape(4, 0, 4, 12, 8, 12);
        SHAPES[Direction.UP.getId()] = Block.createCuboidShape(4, 8, 4, 12, 16, 12);
        SHAPES[Direction.NORTH.getId()] = Block.createCuboidShape(4, 4, 0, 12, 12, 8);
        SHAPES[Direction.SOUTH.getId()] = Block.createCuboidShape(4, 4, 8, 12, 12, 16);
        SHAPES[Direction.WEST.getId()] = Block.createCuboidShape(0, 4, 4, 8, 12, 12);
        SHAPES[Direction.EAST.getId()] = Block.createCuboidShape(8, 4, 4, 16, 12, 12);
    }
    
    public SkullHuntBlock(Settings settings){
        super(settings);
        setDefaultState(
            getStateManager().getDefaultState()
            .with(ATTACHED_SIDE, Direction.DOWN)
        );
    }
    
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx){
        return getDefaultState().with(ATTACHED_SIDE, ctx.getSide().getOpposite());
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(ATTACHED_SIDE, rotation.rotate(state.get(ATTACHED_SIDE)));
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(ATTACHED_SIDE)));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(ATTACHED_SIDE);
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        return SHAPES[state.get(ATTACHED_SIDE).getId()];
    }
    
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world){
        return new SkullHuntBlockEntity();
    }
    
    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> list){
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = stack.getOrCreateTag();
        for(SkullType type : SkullType.values()){
            tag.putInt("SkullType", type.ordinal());
            list.add(stack.copy());
        }
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options){
        super.appendTooltip(stack, world, tooltip, options);
        CompoundTag tag = stack.getOrCreateTag();
        SkullType type = SkullType.valueOf(tag.getInt("SkullType"));
        tooltip.add(new TranslatableText("tooltip.gud_skullhunt.type." + type.name().toLowerCase()));
    }
    
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack){
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity rawEntity = world.getBlockEntity(pos);
        if(rawEntity instanceof SkullHuntBlockEntity){
            SkullHuntBlockEntity entity = (SkullHuntBlockEntity)rawEntity;
            CompoundTag tag = itemStack.getOrCreateTag();
            entity.setType(SkullType.valueOf(tag.getInt("SkullType")));
            entity.setRotation(MathHelper.floor(((placer == null ? 0 : placer.yaw) * 16 / 360) + 0.5) & 15);
        }
        SkullHuntCCA.TRACKER.get(world.getChunk(pos)).addSkullLocation(pos);
    }
    
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player){
        super.onBreak(world, pos, state, player);
        SkullHuntCCA.TRACKER.get(world.getChunk(pos)).removeSkullLocation(pos);
    }
    
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit){
        if(SkullHuntCCA.PLAYER_TRACKER.get(player).addSkullLocation(world.getRegistryKey(), pos)){
            world.playSound(
                player,
                pos,
                SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                SoundCategory.BLOCKS,
                1,
                1
            );
        }
        return ActionResult.SUCCESS;
    }
}
