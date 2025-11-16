package net.lade.lademod.ResourceNetwork;

import net.lade.lademod.block.custom.Cable;
import net.lade.lademod.block.entity.CableEntity;
import net.lade.lademod.block.entity.FluidCableEntity;
import net.lade.lademod.util.ModFluidUtil;
import net.lade.lademod.util.ModResourceUtil;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.ResourceStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class ResourceNetwork<R extends Resource> {
    protected final List<CableEntity<R>> cables = new ArrayList<>();

    protected int totalCapacity;
    protected int totalAmount;
    
    public ResourceNetwork(){

    }

    ResourceStacksResourceHandler<R> networkHandler;

    protected abstract R getEmptyResource();

    public abstract ResourceHandler<R> getNetworkHandler();

    public void addToNetwork(CableEntity<R>  cable) {
        cables.add(cable);
        cable.setNetwork(this);
        ResourceHandler<R> handler = cable.getInternalResourceStorage();
        for (int i = 0; i < handler.size(); i++) {
            int capacity = handler.getCapacityAsInt(i, getEmptyResource());
            totalCapacity += handler.getCapacityAsInt(i, getEmptyResource());
        }
    }

    public void removeFromNetwork(CableEntity<R>  cable) {
        ResourceHandler<R>  handler = cable.getInternalResourceStorage();
        for (int i = 0; i < handler.size(); i++) {
            totalCapacity -= handler.getCapacityAsInt(i, getEmptyResource());
        }
        cables.remove(cable);
    }


    public void absorbNetwork(ResourceNetwork<R> network) {
        if (network == this || network == null) return;
        List<CableEntity<R>> cablesToMove = new ArrayList<>(network.cables);

        for (CableEntity<R> cable : cablesToMove) {
            addToNetwork(cable);
            network.removeFromNetwork(cable);
        }
    }

    public void destroy() {
        List<CableEntity<R>> cablesToMove = new ArrayList<>(cables);
        for (CableEntity<R> cable : cablesToMove) {
            removeFromNetwork(cable);
            cable.createResourceNetwork();
        }
    }
}
