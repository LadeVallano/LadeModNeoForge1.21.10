package net.lade.lademod.item.custom;

import net.lade.lademod.entity.custom.NetherPearlProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class NetherPearl extends Item {

    public NetherPearl(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));

        user.getCooldowns().addCooldown(itemStack, 20);
        if (!world.isClientSide()) {
            NetherPearlProjectile netherPearlProjectile = new NetherPearlProjectile(user, world);
            netherPearlProjectile.setPos(user.getX(), user.getEyeY() - 0.1, user.getZ());
            netherPearlProjectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, 2f, 1.0f);
            world.addFreshEntity(netherPearlProjectile);
            user.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!user.isCreative()) {
            itemStack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return super.onItemUseFirst(stack, context);
    }
}
