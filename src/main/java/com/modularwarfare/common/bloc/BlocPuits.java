package com.modularwarfare.common.bloc;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.init.ModBlock;
import com.modularwarfare.common.init.ModItems;
import com.modularwarfare.utility.IHasModel;
import com.sun.tracing.dtrace.ModuleAttributes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Properties;

public class BlocPuits extends Block implements IHasModel
{
    private static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();

    public BlocPuits(Material material, String name)
    {
        super(material);
        this.setUnlocalizedName(ModularWarfare.MOD_ID + "." + name);
        this.setRegistryName(name);
        this.setBlockUnbreakable();

        ModBlock.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        // Return a custom collision box
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2.0D, 1.0D);
    }

    @Override
    public void registerModels()
    {
        ModularWarfare.PROXY.registerItemRenderer(Item.getItemFromBlock(this));
    }
}