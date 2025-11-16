package net.lade.lademod.item.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.neoforged.neoforge.capabilities.Capabilities;


public class FluidCableItem extends CableItem {
    public FluidCableItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction direction) {
        return level.getCapability(Capabilities.Fluid.BLOCK, pos, level.getBlockState(pos), level.getBlockEntity(pos), direction.getOpposite()) != null;
    }
}
