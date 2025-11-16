package net.lade.lademod.entity.custom;

import net.lade.lademod.entity.ModEntities;
import net.lade.lademod.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class NetherPearlProjectile extends ThrowableItemProjectile {

    int entityHits = 0;
    public NetherPearlProjectile(EntityType<? extends NetherPearlProjectile> type, Level world) {
        super(type, world);
    }

    public NetherPearlProjectile(LivingEntity livingEntity, Level level) {
        super(ModEntities.NETHERPEARL_PROJECTILE.get(), level);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ModItems.NETHERPEARL.get();
    }



    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity hitEntity = entityHitResult.getEntity();
        if (hitEntity instanceof LivingEntity) {
            entityHits++;
            hitEntity.hurt(this.damageSources().thrown(this, this.getOwner()), 10f);
            this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 4f, 0.4f / (this.level().getRandom().nextFloat() * 0.4f + 0.8f));
            if (entityHits >= 2) {
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        this.playSound(SoundEvents.GLASS_BREAK, 4f, 0.4f / (this.level().getRandom().nextFloat() * 0.4f + 0.8f));
        this.discard();
        super.onHitBlock(blockHitResult);
    }



}
