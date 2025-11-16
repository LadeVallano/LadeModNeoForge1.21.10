package net.lade.lademod;

import net.lade.lademod.block.ModBlocks;
import net.lade.lademod.entity.ModEntities;
import net.lade.lademod.entity.renderer.NetherPearlRenderer;
import net.lade.lademod.fluid.FluidType.ModFluidTypes;
import net.lade.lademod.fluid.ModFluids;
import net.lade.lademod.gui.screen.GeoGenScreen;
import net.lade.lademod.networking.ClientPayloadHandler;
import net.lade.lademod.networking.FluidPayload;
import net.lade.lademod.registries.ModMenuTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = LadeMod.MODID)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.GEOGEN_MENU_TYPE.get(), GeoGenScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModFluids.THICK_AIR_FLUID.get(), ChunkSectionLayer.TRANSLUCENT);
        });
    }

    @SubscribeEvent
    private static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private final ResourceLocation WATER_STILL_RL = ResourceLocation.withDefaultNamespace("block/water_still");
            private final ResourceLocation WATER_FLOW_RL = ResourceLocation.withDefaultNamespace("block/water_flow");
            private final ResourceLocation WATER_OVERLAY_RL = ResourceLocation.withDefaultNamespace("block/water_still");

            @Override
            public int getTintColor() {
                return 0xE12E2E2E;
            }

            public @NotNull ResourceLocation getStillTexture() {
                return this.WATER_STILL_RL;
            }

            public @NotNull ResourceLocation getFlowingTexture() {
                return this.WATER_FLOW_RL;
            }

            @Override
            public @NotNull ResourceLocation getOverlayTexture() {
                return this.WATER_OVERLAY_RL;
            }
        }, ModFluidTypes.STEAM_FLUID_TYPE);
        event.registerFluidType(new IClientFluidTypeExtensions() {
            final ResourceLocation THICK_AIR_STILL = ResourceLocation.fromNamespaceAndPath(LadeMod.MODID, "block/thick_air");
            final ResourceLocation THICK_AIR_FLOW = ResourceLocation.fromNamespaceAndPath(LadeMod.MODID, "block/thick_air");
            final ResourceLocation THICK_AIR_UNDER_FLUID_OVERLAY = ResourceLocation.withDefaultNamespace("misc/underwater.png");

            public ResourceLocation getFlowingTexture() {
                return this.THICK_AIR_FLOW;
            }

            public ResourceLocation getStillTexture() {
                return this.THICK_AIR_STILL;
            }

        }, ModFluidTypes.THICK_AIR_FLUID_TYPE);
    }

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void register(RegisterClientPayloadHandlersEvent event) {
        event.register(
                FluidPayload.TYPE,
                ClientPayloadHandler::handleFluidPayload
        );
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NETHERPEARL_PROJECTILE.get(), NetherPearlRenderer::new);
    }

}

