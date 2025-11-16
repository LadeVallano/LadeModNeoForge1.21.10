package net.lade.lademod.fluid;

import net.lade.lademod.LadeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, LadeMod.MODID);

    public static DeferredHolder<Fluid, Steam_Fluid.Source> STEAM_FLUID = FLUIDS.register("steam", Steam_Fluid.Source::new);
    public static DeferredHolder<Fluid, Steam_Fluid.Flowing> STEAM_FLOW = FLUIDS.register("flowing_steam", Steam_Fluid.Flowing::new);

    public static DeferredHolder<Fluid, ThickAirFluid> THICK_AIR_FLUID = FLUIDS.register("thick_air", () -> new ThickAirFluid());

    public static void register(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
