package net.lade.lademod.block.custom;

import net.lade.lademod.block.entity.GeoGenBlockEntity;
import net.lade.lademod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeoGenBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0 / 16f, 0 / 16f, 12 / 16f, 4 / 16f, 7 / 16f, 16 / 16f),
            Shapes.box(4 / 16f, 0 / 16f, 4 / 16f, 12 / 16f, 7 / 16f, 12 / 16f),
            Shapes.box(12 / 16f, 0 / 16f, 12 / 16f, 16 / 16f, 7 / 16f, 16 / 16f),
            Shapes.box(12 / 16f, 0 / 16f, 0 / 16f, 16 / 16f, 7 / 16f, 4 / 16f),
            Shapes.box(0 / 16f, 0 / 16f, 0 / 16f, 4 / 16f, 7 / 16f, 4 / 16f),
            Shapes.box(12 / 16f, 0 / 16f, 4 / 16f, 13 / 16f, 7 / 16f, 5 / 16f),
            Shapes.box(0 / 16f, 7 / 16f, 0 / 16f, 16 / 16f, 14 / 16f, 16 / 16f),
            Shapes.box(2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 16 / 16f, 14 / 16f),
            Shapes.box(4 / 16f, 14 / 16f, 0 / 16f, 12 / 16f, 16 / 16f, 2 / 16f),
            Shapes.box(4 / 16f, 14 / 16f, 14 / 16f, 12 / 16f, 16 / 16f, 16 / 16f),
            Shapes.box(0 / 16f, 14 / 16f, 4 / 16f, 2 / 16f, 16 / 16f, 12 / 16f),
            Shapes.box(14 / 16f, 14 / 16f, 4 / 16f, 16 / 16f, 16 / 16f, 12 / 16f),
            Shapes.box(1 / 16f, 14 / 16f, 14 / 16f, 4 / 16f, 15 / 16f, 15 / 16f),
            Shapes.box(12 / 16f, 14 / 16f, 14 / 16f, 15 / 16f, 15 / 16f, 15 / 16f),
            Shapes.box(1 / 16f, 14 / 16f, 1 / 16f, 4 / 16f, 15 / 16f, 2 / 16f),
            Shapes.box(12 / 16f, 14 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 2 / 16f),
            Shapes.box(1 / 16f, 14 / 16f, 2 / 16f, 2 / 16f, 15 / 16f, 4 / 16f),
            Shapes.box(1 / 16f, 14 / 16f, 12 / 16f, 2 / 16f, 15 / 16f, 14 / 16f),
            Shapes.box(14 / 16f, 14 / 16f, 2 / 16f, 15 / 16f, 15 / 16f, 4 / 16f),
            Shapes.box(14 / 16f, 14 / 16f, 12 / 16f, 15 / 16f, 15 / 16f, 14 / 16f),
            Shapes.box(12 / 16f, 0 / 16f, 11 / 16f, 13 / 16f, 7 / 16f, 12 / 16f),
            Shapes.box(3 / 16f, 0 / 16f, 4 / 16f, 4 / 16f, 7 / 16f, 5 / 16f),
            Shapes.box(3 / 16f, 0 / 16f, 11 / 16f, 4 / 16f, 7 / 16f, 12 / 16f),
            Shapes.box(4 / 16f, 0 / 16f, 12 / 16f, 5 / 16f, 7 / 16f, 13 / 16f),
            Shapes.box(11 / 16f, 0 / 16f, 3 / 16f, 12 / 16f, 7 / 16f, 4 / 16f),
            Shapes.box(11 / 16f, 0 / 16f, 12 / 16f, 12 / 16f, 7 / 16f, 13 / 16f),
            Shapes.box(4 / 16f, 0 / 16f, 3 / 16f, 5 / 16f, 7 / 16f, 4 / 16f));

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    public GeoGenBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockStateChange(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState oldState, @NotNull BlockState newState) {
        super.onBlockStateChange(level, pos, oldState, newState);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GeoGenBlockEntity geo) {


            if (!player.isCrouching() && !world.isClientSide()) {
                //call menu
                (player).openMenu(new SimpleMenuProvider(geo, Component.literal("GeoGen")), pos);
            }
            return InteractionResult.CONSUME;
        }
        return super.useWithoutItem(state, world, pos, player, hitResult);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) return null;

        if (type == ModBlockEntities.GEO_GEN_BLOCK_ENTITY_TYPE.get()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof GeoGenBlockEntity geo) geo.tick(level, pos, blockState);
            };
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new GeoGenBlockEntity(blockPos, blockState);
    }
}