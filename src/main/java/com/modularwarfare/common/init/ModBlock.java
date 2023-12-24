package com.modularwarfare.common.init;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.bloc.BlocPuits;
import com.modularwarfare.common.items.ItemBandageAdhesif;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.material.Material;
import java.util.Set;

public class ModBlock {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block PUITS = new BlocPuits(Material.ROCK, "puits");

}
