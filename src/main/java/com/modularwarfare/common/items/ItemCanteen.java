package com.modularwarfare.common.items;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.Thirst.CapabilityThirst;
import com.modularwarfare.common.Thirst.IThirst;
import com.modularwarfare.common.bloc.BlocPuits;
import com.modularwarfare.common.init.ModItems;
import com.modularwarfare.utility.IHasModel;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCanteen extends Item implements IHasModel {
    public ItemCanteen(String name)
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ModularWarfare.Tools_TAB);
        this.setMaxStackSize(1);
        this.setMaxDamage(3);
        this.setNoRepair();
        this.addPropertyOverride(new ResourceLocation("filled"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World world, EntityLivingBase entity)
            {
                if (!(stack.getItemDamage() == stack.getMaxDamage()*4)) {
                    return 1.0F;
                }
                else{
                    return 0.0F;
                }
            }
        });

        ModItems.ITEMS.add(this);
    }
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
    {
        final String info = TextFormatting.GRAY + I18n.format(this.getUnlocalizedName() + ".info");

        tooltip.add(info);
    }
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, getMetadata(1)));
            items.add(new ItemStack(this, 1, getMetadata(12)));
        }
    }
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;


            int damage = (stack.getItemDamage() >> 2) + 1;
            //Reset the canteen to its empty state
            if (damage == this.getMaxDamage())
            {
                damage = 3;
            }

            this.setDamage(stack, (damage << 2));


            IThirst thirstStats = (IThirst)player.getCapability(CapabilityThirst.CAPABILITY_THIRST, null);


            if (!world.isRemote)
            {
                thirstStats.addStats(24000, 1000);
            }
        }

        return stack;
    }
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 64;
    }
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!attemptCanteenFill(player, stack) && getTimesUsed(stack) < 3)
        {

            player.setActiveHand(hand);
        }

        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return (stack.getItemDamage() >> 2) / (double)stack.getMaxDamage();
    }

    /**
     * Attempt to fill the provided canteen stack with water.
     * @param player The player holding the canteen.
     * @param stack The canteen item stack.
     * @return true if successful, otherwise false.
     */
    private boolean attemptCanteenFill(EntityPlayer player, ItemStack stack)
    {
        World world = player.world;
        RayTraceResult movingObjectPos = this.rayTrace(world, player, true);

        if (movingObjectPos != null && movingObjectPos.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = movingObjectPos.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            Fluid fluid = FluidRegistry.lookupFluidForBlock(state.getBlock());

            if (fluid != null && fluid == FluidRegistry.WATER) //Temporary, until a registry is created
            {
                stack.setItemDamage(1);
                return true;
            }
            else if (state.getBlock() instanceof BlockCauldron)
            {
                BlockCauldron cauldron = (BlockCauldron)state.getBlock();
                int level = ((Integer)state.getValue(BlockCauldron.LEVEL));

                if (level > 0 && !world.isRemote)
                {

                    player.addStat(StatList.CAULDRON_USED);
                    stack.setItemDamage(1);
                    return true;

                }
            }
            else if (state.getBlock() instanceof BlocPuits)
            {
                BlocPuits cauldron = (BlocPuits)state.getBlock();



                player.addStat(StatList.CAULDRON_USED);
                stack.setItemDamage(1);
                return true;



            }
        }

        return false;
    }
    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }


    private int getTimesUsed(ItemStack stack)
    {
        return stack.getItemDamage() >> 2;
    }
    @Override
    public void registerModels()
    {
        ModularWarfare.PROXY.registerItemRenderer(this);
    }
}

