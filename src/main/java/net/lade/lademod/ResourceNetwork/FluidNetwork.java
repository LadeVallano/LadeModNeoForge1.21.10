package net.lade.lademod.ResourceNetwork;

import com.google.common.graph.Network;
import net.lade.lademod.block.entity.CableEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

public class FluidNetwork extends ResourceNetwork<FluidResource> {
    public FluidNetwork(CableEntity<FluidResource> entity) {
        addToNetwork(entity);
    }


    FluidStacksResourceHandler fluidHandler = new FluidStacksResourceHandler(1, Integer.MAX_VALUE) {
        @Override
        public long getCapacityAsLong(int index, FluidResource resource) {
            return totalCapacity;
        }

        @Override
        public int getCapacityAsInt(int index, FluidResource resource) {
            return totalCapacity;
        }

        @Override
        protected int getCapacity(int index, FluidResource resource) {
            return totalCapacity;
        }

        @Override
        public long getAmountAsLong(int index) {
            return totalAmount;
        }

        @Override
        public int getAmountAsInt(int index) {
            return totalAmount;
        }
    };

    @Override
    public ResourceHandler<FluidResource> getNetworkHandler() {
        return fluidHandler;
    }

    @Override
    protected FluidResource getEmptyResource() {
        return FluidResource.EMPTY;
    }
}
