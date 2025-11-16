package net.lade.lademod.item.block;

import net.lade.lademod.block.custom.FluidCable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class CableItem extends BlockItem {
    public CableItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState defaultState = getBlock().defaultBlockState();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return defaultState
                .setValue(FluidCable.NORTH, canConnect(level, pos.north(), Direction.NORTH))
                .setValue(FluidCable.EAST, canConnect(level, pos.east(), Direction.EAST))
                .setValue(FluidCable.SOUTH, canConnect(level, pos.south(), Direction.SOUTH))
                .setValue(FluidCable.WEST, canConnect(level, pos.west(), Direction.WEST))
                .setValue(FluidCable.UP, canConnect(level, pos.above(), Direction.UP))
                .setValue(FluidCable.DOWN, canConnect(level, pos.below(), Direction.DOWN))
                .setValue(FluidCable.PULL_NORTH, false)
                .setValue(FluidCable.PULL_SOUTH, false)
                .setValue(FluidCable.PULL_EAST, false)
                .setValue(FluidCable.PULL_WEST, false)
                .setValue(FluidCable.PULL_UP, false)
                .setValue(FluidCable.PULL_DOWN, false);
    }

    protected abstract boolean canConnect(Level level, BlockPos pos, Direction direction);
}
