package net.lade.lademod.block.entity;

import net.lade.lademod.ResourceNetwork.FluidNetwork;
import net.lade.lademod.ResourceNetwork.ResourceNetwork;
import net.lade.lademod.util.ModFluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidCableEntity extends CableEntity<FluidResource> {

    FluidNetwork fluidNetwork = null;

    public FluidCableEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    private final int MAX_FLUID_AMOUNT = 1000;
    private final FluidStacksResourceHandler internalFluidStorage = new FluidStacksResourceHandler(1, MAX_FLUID_AMOUNT) {

        @Override
        protected void onContentsChanged(int index, @NotNull FluidStack previousContents) {
            setChanged();
        }

        @Override
        public @NotNull FluidResource getResource(int index) {
            if(getNetwork() == null)return FluidResource.EMPTY;
            return getNetwork().getNetworkHandler().getResource(index);
        }

        @Override
        public int extract(@NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
            if(getNetwork() == null) return 0;
            return getNetwork().getNetworkHandler().extract(resource, amount, transaction);
        }

        @Override
        public int extract(int index, @NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
            return this.extract(resource, amount, transaction);
        }

        @Override
        public int insert(@NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {
            if(getNetwork() == null) return 0;
            return getNetwork().getNetworkHandler().insert(resource, amount, transaction);
        }

        @Override
        public int insert(int index, @NotNull FluidResource resource, int amount, @NotNull TransactionContext transaction) {

            return this.insert(resource, amount, transaction);
        }
    };

    @Override
    public void createResourceNetwork() {
        new FluidNetwork(this);
    }

    @Override
    public void setNetwork(ResourceNetwork<FluidResource> tResourceNetwork) {
        fluidNetwork = (FluidNetwork) tResourceNetwork;
    }

    public FluidNetwork getNetwork() {
        return fluidNetwork;
    }

    @Override
    public ResourceHandler<FluidResource> getInternalResourceStorage() {
        return internalFluidStorage;
    }

    private void transferFluid(outputDirection fluidIO, List<Direction> directions) {
        if (level == null || level.isClientSide()) return;
        for (Direction direction : directions) {
            if (direction == null) continue;
            BlockPos neighbourPos = getBlockPos().relative(direction);

            ResourceHandler<FluidResource> neighbourHandler = level.getCapability(
                    Capabilities.Fluid.BLOCK,
                    neighbourPos,
                    level.getBlockState(neighbourPos),
                    level.getBlockEntity(neighbourPos),
                    direction.getOpposite()
            );

            if (neighbourHandler != null) {
                ResourceHandler<FluidResource> extractHandler = internalFluidStorage;
                ResourceHandler<FluidResource> insertHandler = internalFluidStorage;

                if (fluidIO == outputDirection.PUSH) {
                    insertHandler = neighbourHandler;
                }
                if (fluidIO == outputDirection.PULL) {
                    extractHandler = neighbourHandler;
                }
                ModFluidUtil.transferFromTo(extractHandler, insertHandler, MAX_FLUID_AMOUNT);
            }
        }
    }

    protected ResourceHandler<FluidResource> getCapabilityFrom(BlockPos neighbourPos, Direction direction) {
        return level.getCapability(
                Capabilities.Fluid.BLOCK,
                neighbourPos,
                level.getBlockState(neighbourPos),
                level.getBlockEntity(neighbourPos),
                direction.getOpposite()
        );
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        internalFluidStorage.serialize(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        internalFluidStorage.deserialize(input);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState blockState) {
        transferFluid(outputDirection.PUSH, pushDirections);
        transferFluid(outputDirection.PULL, pullDirections);
    }

}
