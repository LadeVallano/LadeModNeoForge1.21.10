package net.lade.lademod.networking;

import net.neoforged.neoforge.network.handling.IPayloadContext;




public class ServerPayloadHandler{


    public static void handleFluidPayload(FluidPayload payload, IPayloadContext context) {

        context.enqueueWork(() -> {
        });
    }
}