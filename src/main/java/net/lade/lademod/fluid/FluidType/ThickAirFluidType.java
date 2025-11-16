//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.lade.lademod.fluid.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThickAirFluidType extends FluidType {
    public ThickAirFluidType() {
        super(Properties.create().temperature(20).pathType(PathType.BLOCKED).canConvertToSource(false).fallDistanceModifier(1.0F).canDrown(false).canPushEntity(false).density(9999).fallDistanceModifier(0.0F).motionScale(0.0).rarity(Rarity.UNCOMMON).supportsBoating(true).viscosity(900).lightLevel(0));
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 travelVector, double gravity) {
        if (entity.isSprinting()) {
            // Allow slightly faster movement when sprinting
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.95));
        } else {
            // Drastically reduce movement, simulating thick air
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.8));
        }

        // Apply gravity
        entity.setDeltaMovement(entity.getDeltaMovement().add(0, -gravity * 0.5, 0));

        return true; // We handled movement entirely
    }
}
