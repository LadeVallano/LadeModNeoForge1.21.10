package net.lade.lademod.item;

import net.lade.lademod.LadeMod;
import net.lade.lademod.fluid.ModFluids;
import net.lade.lademod.item.custom.NetherPearl;
import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LadeMod.MODID);
    
    public static final DeferredItem<Item> NETHERPEARL = ITEMS.registerItem("netherpearl",
            (properties) -> new NetherPearl(properties.stacksTo(16)));


    public static final DeferredItem<Item> NETHERPEARL_DUST = ITEMS.registerItem("netherpearl_dust",
            (properties) -> new Item(properties.stacksTo(64)));

    public static final DeferredItem<BucketItem> STEAM_BUCKET = ITEMS.registerItem("steam_bucket",
            (properties) -> new BucketItem(ModFluids.STEAM_FLUID.get(), properties.craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<BucketItem> THICK_AIR_BUCKET = ITEMS.registerItem("thick_air_bucket",
            (properties) -> new BucketItem(ModFluids.THICK_AIR_FLUID.get(), properties.craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
