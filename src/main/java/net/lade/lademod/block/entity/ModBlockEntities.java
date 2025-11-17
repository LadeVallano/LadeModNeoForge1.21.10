package net.lade.lademod.block.entity;

import net.lade.lademod.LadeMod;
import net.lade.lademod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LadeMod.MODID);


    //-------------------------------------------------------------BlockEntityType Registries--------------------------------------------------------------------------------------------------------------------
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidCableEntity>> FLUID_CABLE_ENTITY_TYPE =
            BLOCK_ENTITIES.register("cable_entity_type", () -> new BlockEntityType<>(
                    FluidCableEntity::new,
                    Set.of(ModBlocks.FLUID_CABLE.get()),
                    false
                    )
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeoGenBlockEntity>> GEO_GEN_BLOCK_ENTITY_TYPE =
            BLOCK_ENTITIES.register("geogen", () -> new BlockEntityType<>(
                    GeoGenBlockEntity::new,
                    Set.of(ModBlocks.GEOTHERMAL_GENERATOR.get()),
                    false
            ));


    //--------------------------------------------------------------.-----------------------------------------------------------------------------------------------------------------------------------
    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}