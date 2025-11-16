package net.lade.lademod.networking;

import net.lade.lademod.block.entity.GeoGenBlockEntity;
import net.lade.lademod.gui.GeoGenMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleFluidPayload(final FluidPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    Level clientLevel = Minecraft.getInstance().level;
                    Player player = Minecraft.getInstance().player;
                    BlockPos pos = data.getPos();
                    if (clientLevel == null || player == null) return;
                    BlockEntity be = clientLevel.getBlockEntity(pos);

                    if (be instanceof GeoGenBlockEntity) {
                        AbstractContainerMenu menu = player.containerMenu;
                        if (menu instanceof GeoGenMenu geoGenMenu) {
                            for (int i = 0; i < 3; i++) {
                                geoGenMenu.setClientFluid(i, data.getFluid(i));
                            }
                        }
                    }

                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("lademod.networking.failed", e.getMessage()));
                    return null;
                });
    }
}
