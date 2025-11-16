package net.lade.lademod.item;

import net.lade.lademod.LadeMod;
import net.lade.lademod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs extends CreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LadeMod.MODID);

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LADE_MOD_TAB = CREATIVE_MODE_TABS.register("lade_mod_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.NETHERPEARL.get()))
                    .title(Component.translatable("creativetab.lademod"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.NETHERPEARL.get());
                        output.accept(ModItems.NETHERPEARL_DUST.get());
                        output.accept(ModBlocks.GEOTHERMAL_GENERATOR.get());
                        output.accept(ModBlocks.NETHERPEARL_BLOCK.get());
                        output.accept(ModBlocks.FLUID_CABLE_ITEM.get());
                        output.accept(ModItems.STEAM_BUCKET.get());
                        output.accept(ModItems.THICK_AIR_BUCKET);
                    }))
                    .build()

    );

}