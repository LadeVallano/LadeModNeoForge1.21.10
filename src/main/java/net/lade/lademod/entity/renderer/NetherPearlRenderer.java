package net.lade.lademod.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.lade.lademod.entity.custom.NetherPearlProjectile;

public class NetherPearlRenderer extends ThrownItemRenderer<NetherPearlProjectile> {
    public NetherPearlRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
