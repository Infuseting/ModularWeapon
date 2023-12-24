package com.modularwarfare.common.container;

import com.modularwarfare.api.MWArmorType;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import com.modularwarfare.common.backpacks.ItemBackpack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotBrassard extends SlotItemHandler {
    public SlotBrassard(final IItemHandler inv, final int index, final int xPosition, final int yPosition) {
        super(inv, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    public boolean isItemValid(@Nonnull final ItemStack stack) {
        if (stack.getItem() instanceof ItemSpecialArmor) {
            ItemSpecialArmor armor = (ItemSpecialArmor) stack.getItem();
            return (armor.armorType == MWArmorType.Brassard);
        }
        return false;
    }
}

