package net.lade.lademod.registries;

import net.lade.lademod.LadeMod;
import net.lade.lademod.gui.GeoGenMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, LadeMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<GeoGenMenu>> GEOGEN_MENU_TYPE = registerMenuType("geo_gen_menu", GeoGenMenu::new);


    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, MenuType.MenuSupplier<T> factory) {
        return MENU_TYPES.register(name, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
