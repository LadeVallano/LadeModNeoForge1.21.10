package net.lade.lademod.entity;


import net.lade.lademod.LadeMod;
import net.lade.lademod.entity.custom.NetherPearlProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class  ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, LadeMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<NetherPearlProjectile>> NETHERPEARL_PROJECTILE =
            ENTITY_TYPES.register("netherpearl_projectile",
                    registryName -> EntityType.Builder.<NetherPearlProjectile>of(NetherPearlProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .build(ResourceKey.create(Registries.ENTITY_TYPE, registryName))
            );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
