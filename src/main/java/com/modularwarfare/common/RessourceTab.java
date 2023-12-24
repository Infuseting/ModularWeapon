package com.modularwarfare.common;

import com.modularwarfare.common.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RessourceTab extends CreativeTabs
{
    public RessourceTab(String label)
    {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return new ItemStack(ModItems.Bois);
    }
}
