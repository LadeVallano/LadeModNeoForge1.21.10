package net.lade.lademod.ResourceNetwork;

import net.lade.lademod.block.entity.CableEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceStacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;

import java.util.*;

public abstract class ResourceNetwork<R extends Resource> extends SavedData {
    protected final List<CableEntity<R>> cables = new ArrayList<>();

    protected int totalCapacity;
    protected int totalAmount;

    public ResourceNetwork(){
    }
    protected abstract ResourceNetwork<R> createNetwork(CableEntity<R> entity);



    ResourceStacksResourceHandler<R> networkHandler;

    protected abstract R getEmptyResource();

    public abstract ResourceHandler<R> getNetworkHandler();

    public void addToNetwork(CableEntity<R>  cable) {
        cables.add(cable);
        cable.setNetwork(this);
        ResourceHandler<R> handler = cable.getResourceHandler();
        for (int i = 0; i < handler.size(); i++) {
            int capacity = handler.getCapacityAsInt(i, getEmptyResource());
            totalCapacity += capacity;
        }
    }

    public void removeFromNetwork(CableEntity<R>  cable) {
        ResourceHandler<R>  handler = cable.getResourceHandler();
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

    private void splitNetwork(List<CableEntity<R>> cables){
        CableEntity<R> firstCable = cables.removeFirst();
        ResourceNetwork<R> network = createNetwork(firstCable);
        for (CableEntity<R> cable : cables) {
            network.addToNetwork(cable);
            removeFromNetwork(cable);
        }
    }

    public void checkConnectedAndSplit(){

        if (cables.isEmpty()) return;

        CableEntity<R> startCable = cables.getFirst();

        Queue<CableEntity<R>> queue = new LinkedList<>();
        Set<CableEntity<R>> visited = new HashSet<>();

        queue.add(startCable);
        visited.add(startCable);

        while (!queue.isEmpty()) {
            CableEntity<R> current = queue.poll();

            // Iterate over neighbors of the current cable
            for (CableEntity<R> neighbor : current.getAdjacentCablesInNetwork()) {

                // CRITICAL: Ensure we are not using the cable being removed
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        System.out.println(visited.size() + " " + cables.size());
        boolean connected = visited.size() == cables.size();
        if(!connected){
            splitNetwork(new ArrayList<>(visited));
        }
    }
}
