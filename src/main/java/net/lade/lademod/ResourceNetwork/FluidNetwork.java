package net.lade.lademod.ResourceNetwork;

import net.lade.lademod.block.custom.FluidCable;
import net.lade.lademod.block.entity.CableEntity;
import net.lade.lademod.block.entity.FluidCableEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import org.jetbrains.annotations.NotNull;

public class FluidNetwork extends ResourceNetwork<FluidResource> {
    public FluidNetwork(CableEntity<FluidResource> entity) {
        addToNetwork(entity);
    }

    public FluidNetwork(){
        super();
    }

    @Override
    protected ResourceNetwork<FluidResource> createNetwork(CableEntity<FluidResource> entity) {
        removeFromNetwork(entity);
        return new FluidNetwork(entity);
    }

    FluidStacksResourceHandler networkHandler = new FluidStacksResourceHandler(1, Integer.MAX_VALUE) {
        @Override
        public long getCapacityAsLong(int index, @NotNull FluidResource resource) {
            return totalCapacity;
        }

        @Override
        protected int getCapacity(int index, @NotNull FluidResource resource) {
            return totalCapacity;
        }

        @Override
        public long getAmountAsLong(int index) {
            return totalAmount;
        }

    };

    @Override
    public ResourceHandler<FluidResource> getNetworkHandler() {
        return networkHandler;
    }

    @Override
    protected FluidResource getEmptyResource() {
        return FluidResource.EMPTY;
    }
}
