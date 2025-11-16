package net.lade.lademod.gui;

import net.lade.lademod.block.ModBlocks;
import net.lade.lademod.registries.ModMenuTypes;
import net.lade.lademod.util.MenuUtil;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class GeoGenMenu extends AbstractContainerMenu {

    private final FluidStack[] clientFluid = new FluidStack[]{FluidStack.EMPTY, FluidStack.EMPTY, FluidStack.EMPTY};
    private final Level world;
    ContainerLevelAccess access;
    Container container;

    public GeoGenMenu(int id, Inventory playerInventory, ContainerLevelAccess access, Container blockEntity) {
        super(ModMenuTypes.GEOGEN_MENU_TYPE.get(), id);
        this.access = access;
        world = playerInventory.player.level();
        this.container = blockEntity;

        MenuUtil.playerInventorySlots(playerInventory, this::addSlot);
        MenuUtil.playHotBarSlots(playerInventory, this::addSlot);

        if (world.isClientSide()) return;
        Player player = playerInventory.player;
        blockEntity.startOpen(player);
    }

    public GeoGenMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL, null);
    }

    public int getClientFluidAmount(int i) {
        return clientFluid[i].getAmount();
    }

    public FluidStack getClientFluid(int i) {
        return clientFluid[i];
    }

    public void setClientFluid(int i, FluidStack stack) {
        this.clientFluid[i] = stack;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return null;
    }

    @Override
    public void removed(@NotNull Player player) {
        if (world.isClientSide()) return;
        container.stopOpen(player);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (world.isClientSide()) return true;
        return AbstractContainerMenu.stillValid(this.access, player, ModBlocks.GEOTHERMAL_GENERATOR.get());
    }
}
