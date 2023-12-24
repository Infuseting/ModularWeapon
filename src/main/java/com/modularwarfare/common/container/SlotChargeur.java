package com.modularwarfare.common.container;

import com.modularwarfare.common.guns.ItemAmmo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotChargeur extends SlotItemHandler {
    public SlotChargeur(final IItemHandler inv, final int index, final int xPosition, final int yPosition) {
        super(inv, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull final ItemStack stack) {
        if (stack.getItem() instanceof ItemAmmo) {
            return true;
        }
        return false;
    }
}
