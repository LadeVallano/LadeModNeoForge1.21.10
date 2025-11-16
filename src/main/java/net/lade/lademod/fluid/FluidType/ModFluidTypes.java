package net.lade.lademod.fluid.FluidType;

import net.lade.lademod.LadeMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluidTypes {


    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, LadeMod.MODID);

    public static final DeferredHolder<FluidType, SteamFluidType> STEAM_FLUID_TYPE = FLUID_TYPES.register("steam", SteamFluidType::new);
    public static final DeferredHolder<FluidType, ThickAirFluidType> THICK_AIR_FLUID_TYPE = FLUID_TYPES.register("thick_air", ThickAirFluidType::new);


    public static void register(IEventBus eventBus){
        FLUID_TYPES.register(eventBus);
    }
}
