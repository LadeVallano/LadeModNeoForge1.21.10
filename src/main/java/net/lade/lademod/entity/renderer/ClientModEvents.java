package net.lade.lademod.entity.renderer;

import net.lade.lademod.entity.ModEntities;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


public class ClientModEvents {

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NETHERPEARL_PROJECTILE.get(), NetherPearlRenderer::new);
    }
}
