package net.lade.lademod.block.custom;

import net.lade.lademod.block.entity.FluidCableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidCable extends Cable<FluidResource> {

    public FluidCable(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new FluidCableEntity(blockPos, blockState);
    }

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction direction) {
        return level.getBlockEntity(pos) instanceof FluidCableEntity || level.getCapability(Capabilities.Fluid.BLOCK, pos, level.getBlockState(pos), level.getBlockEntity(pos), direction.getOpposite()) != null;
    }



}
