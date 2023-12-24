package com.modularwarfare.common.items;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.init.ModItems;
import com.modularwarfare.utility.IHasModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemAcier extends Item implements IHasModel {
    public ItemAcier(String name)
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ModularWarfare.Ressource_TAB);
        this.setMaxStackSize(5);
        ModItems.ITEMS.add(this);
    }
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        final String info = TextFormatting.GRAY + I18n.format(this.getUnlocalizedName() + ".info");

        tooltip.add(info);
    }
    @Override
    public void registerModels()
    {
        ModularWarfare.PROXY.registerItemRenderer(this);
    }
}

