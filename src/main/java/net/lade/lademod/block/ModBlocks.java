package net.lade.lademod.block;

import net.lade.lademod.LadeMod;
import net.lade.lademod.block.custom.FluidCable;
import net.lade.lademod.block.custom.GeoGenBlock;
import net.lade.lademod.fluid.ModFluids;
import net.lade.lademod.item.ModItems;
import net.lade.lademod.item.block.FluidCableItem;
import net.lade.lademod.item.custom.NetherPearl;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LadeMod.MODID);

    //-------------------------------------------------------------Block Registries----------------------------------------------------------------------------------------------------------------------
    public static final DeferredBlock<Block> NETHERPEARL_BLOCK = registerBlock("netherpearl_block",
            properties -> new Block(properties
                    .mapColor(DyeColor.RED)
            )
    );

    public static final DeferredBlock<Block> FLUID_CABLE = BLOCKS.registerBlock("fluid_cable",
            properties -> new FluidCable(properties
                    .strength(0.5f, 2.0f)
                    .noOcclusion()
                    .mapColor(DyeColor.BLUE)
            ));

    public static final DeferredItem<Item> FLUID_CABLE_ITEM = ModItems.ITEMS.registerItem("fluid_cable",
            (properties) -> new FluidCableItem(FLUID_CABLE.get(), properties));

    public static final DeferredBlock<Block> GEOTHERMAL_GENERATOR = registerBlock("geothermal_generator",
            properties -> new GeoGenBlock(properties
                    .strength(1.5f, 18.0f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .mapColor(DyeColor.GRAY)
            ));

    public static final DeferredBlock<LiquidBlock> STEAM_BLOCK = BLOCKS.registerBlock("steam_block",
            properties -> new LiquidBlock(ModFluids.STEAM_FLUID.get(),
                    properties
                            .replaceable()
                            .noOcclusion()));

    public static final DeferredHolder<Block, LiquidBlock> THICK_AIR_BLOCK = BLOCKS.register("thick_air_block",
            registryName -> {return new LiquidBlock(ModFluids.THICK_AIR_FLUID.get(), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, registryName)).mapColor(MapColor.NONE).replaceable().strength(100.0F).lightLevel((state) -> {
            return 0;
        }).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    });
    //----------------------------------------------------------------------------------- --------------------------------------------------------------------------------------------------------------------


    private static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> block) {
        DeferredBlock<B> toReturn = BLOCKS.registerBlock(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
