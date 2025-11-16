package net.lade.lademod;

import net.lade.lademod.block.ModBlocks;
import net.lade.lademod.block.entity.FluidCableEntity;
import net.lade.lademod.block.entity.GeoGenBlockEntity;
import net.lade.lademod.block.entity.ModBlockEntities;
import net.lade.lademod.entity.ModEntities;
import net.lade.lademod.fluid.FluidType.ModFluidTypes;
import net.lade.lademod.fluid.ModFluids;
import net.lade.lademod.item.ModItems;
import net.lade.lademod.item.ModTabs;
import net.lade.lademod.networking.FluidPayload;
import net.lade.lademod.networking.ServerPayloadHandler;
import net.lade.lademod.particle.ModParticles;
import net.lade.lademod.registries.ModMenuTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@Mod(LadeMod.MODID)
@EventBusSubscriber(modid = LadeMod.MODID)
public class LadeMod   {
	public static final String MODID = "lademod";


	public LadeMod(IEventBus modBus, ModContainer modContainer) {
		ModTabs.register(modBus);
		ModItems.register(modBus);
		ModBlocks.register(modBus);
		ModMenuTypes.register(modBus);
		ModEntities.register(modBus);
		ModBlockEntities.register(modBus);
		ModFluidTypes.register(modBus);
		ModFluids.register(modBus);
		ModParticles.register(modBus);
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.Fluid.BLOCK,
				ModBlockEntities.GEO_GEN_BLOCK_ENTITY_TYPE.get(),
                GeoGenBlockEntity::getFluidHandlerForSide
		);
		/*
		event.registerBlockEntity(
				Capabilities.Fluid.BLOCK,
				ModBlockEntities.FLUID_CABLE_ENTITY_TYPE.get(),
				FluidCableEntity::getFluidHandlerForSide
		);
		 */
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {

		final PayloadRegistrar registrar = event.registrar("1");

		registrar.playBidirectional(
				FluidPayload.TYPE,
				FluidPayload.STREAM_CODEC,
				ServerPayloadHandler::handleFluidPayload
		);
	}

}