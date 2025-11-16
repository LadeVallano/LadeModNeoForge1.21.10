package net.lade.lademod.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.function.Function;

public class MenuUtil {

    static final int PLAYER_INVENTORY_START_X = 8;
    static final int SLOT_SIZE_PIXELS = 18;
    static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    static final int HOT_BAR_SLOT_COUNT = 9;


    public static void playHotBarSlots(Inventory playerInventory, Function<Slot, Slot> addSlotFunction) {

        for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++) {
            for (int col = 0; col < HOT_BAR_SLOT_COUNT; col++) {
                int slotIndex = col + row * HOT_BAR_SLOT_COUNT + HOT_BAR_SLOT_COUNT; // Index 9 to 35
                int x = PLAYER_INVENTORY_START_X + col * SLOT_SIZE_PIXELS;
                int PLAYER_INVENTORY_START_Y = 84;
                int y = PLAYER_INVENTORY_START_Y + row * SLOT_SIZE_PIXELS;

                addSlotFunction.apply(new Slot(playerInventory, slotIndex, x, y));
            }
        }
    }


    public static void playerInventorySlots(Inventory playerInventory, Function<Slot, Slot> addSlotFunction) {
        for (int col = 0; col < HOT_BAR_SLOT_COUNT; col++) {
            int x = PLAYER_INVENTORY_START_X + col * SLOT_SIZE_PIXELS;

            int HOT_BAR_START_Y = 142;
            addSlotFunction.apply(new Slot(playerInventory, col, x, HOT_BAR_START_Y));

        }
    }
}
