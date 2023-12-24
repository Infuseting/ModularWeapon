package com.modularwarfare.common.items;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.init.ModItems;
import com.modularwarfare.common.network.PacketHealPart;
import com.modularwarfare.utility.IHasModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBandageCompressif  extends Item implements IHasModel {
    public ItemBandageCompressif(String name)
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ModularWarfare.Medical_TAB);
        this.setMaxStackSize(1);
        this.setMaxDamage(2);
        ModItems.ITEMS.add(this);
    }
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        final String info = TextFormatting.GRAY + I18n.format(this.getUnlocalizedName() + ".info");

        tooltip.add(info);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {

            EntityPlayer player = (EntityPlayer)entity;
            RayTraceResult result = rayTrace(world, player, true);
            int damage = stack.getItemDamage()+ 1;
            if (damage == this.getMaxDamage())
            {
                stack.setCount(0);
            }
            else {
                this.setDamage(stack, damage);
            }

            if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) result.entityHit;

                if (target instanceof EntityPlayer && target != player) {
                    if (!world.isRemote)
                    {
                        ModularWarfare.NETWORK.sendTo(new PacketHealPart(player, (EntityPlayer) target, 0, 1, 0), (EntityPlayerMP) player);
                    }
                }
                else {
                    ModularWarfare.NETWORK.sendTo(new PacketHealPart(player, player, 0, 1, 0), (EntityPlayerMP) player);
                }




            }
            else {
                ModularWarfare.NETWORK.sendTo(new PacketHealPart(player, player, 0, 1, 0), (EntityPlayerMP) player);
            }

        }

        return stack;
    }
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 300;
    }
    @Override
    public void registerModels()
    {
        ModularWarfare.PROXY.registerItemRenderer(this);
    }
}

