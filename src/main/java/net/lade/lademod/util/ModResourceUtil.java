package net.lade.lademod.util;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ModResourceUtil {


    public static <R extends Resource> boolean transferFromTo(ResourceHandler<R> source, ResourceHandler<R> destination, int amount) {
        boolean result = true;
        for (int srcIndex = 0; srcIndex < source.size(); srcIndex++) {
            result = result && transferFromTo(srcIndex, source, destination, amount);
        }
        return result;
    }

    public static  <R extends Resource>  boolean transferFromTo(int i, ResourceHandler<R> source, ResourceHandler<R> destination, int amount) {

        for (int destIndex = 0; destIndex < destination.size(); destIndex++) {
            R resource = source.getResource(i);

            if (!resource.isEmpty()) {

                try (Transaction transaction = Transaction.openRoot()) {

                    int simulatedExtract = source.extract(i, resource, amount, transaction);

                    int simulatedInsert = destination.insert(destIndex, resource, simulatedExtract, transaction);
                    if (simulatedInsert == simulatedExtract) {

                        transaction.commit();
                        return true;
                    }
                }
            }

        }
        return false;
    }

}


