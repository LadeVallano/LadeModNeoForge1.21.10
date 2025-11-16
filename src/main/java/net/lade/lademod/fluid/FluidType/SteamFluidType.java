package net.lade.lademod.fluid.FluidType;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

public class SteamFluidType extends FluidType {

    public SteamFluidType( ) {
        super(Properties.create());
    }

    @Override
    public double motionScale(Entity entity) {
        return entity.level().dimensionType().ultraWarm() ? 0.007 : 0.0023333333333333335;
    }

    @Override
    public void setItemMovement(ItemEntity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        entity.setDeltaMovement(vec3.x * 0.949999988079071, vec3.y + (double)(vec3.y < 0.05999999865889549 ? 5.0E-4F : 0.0F), vec3.z * 0.949999988079071);
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 travelVector, double gravity) {
        double entityHeight = entity.getY();
        boolean flag = entity.getDeltaMovement().y <= 0.0;
        entity.moveRelative(0.02F, travelVector);
        entity.move(MoverType.SELF, entity.getDeltaMovement());
        Vec3 vec34;
        if (entity.getFluidTypeHeight(ModFluidTypes.STEAM_FLUID_TYPE.get()) <= entity.getFluidJumpThreshold()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.800000011920929, 0.5));
            vec34 = entity.getFluidFallingAdjustedMovement(gravity, flag, entity.getDeltaMovement());
            entity.setDeltaMovement(vec34);
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
        }

        if (gravity != 0.0) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -gravity / 4.0, 0.0));
        }

        vec34 = entity.getDeltaMovement();
        if (entity.horizontalCollision && entity.isFree(vec34.x, vec34.y + 0.6000000238418579 - entity.getY() + entityHeight, vec34.z)) {
            entity.setDeltaMovement(vec34.x, 0.30000001192092896, vec34.z);
        }

        return true;
    }

}


