package com.modularwarfare.common.init;

import com.modularwarfare.common.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Set<Block> EFFECTIVE_ON = new HashSet<Block>();

    //public static final Item BANDAGEADHESIF = new ItemBandageAdhesif("Bandage_Adhesif");
    //public static final Item BANDAGECOMPRESSIF = new ItemBandageCompressif("Bandage_Compressif");
    //public static final Item CMS = new ItemCMS("CMS");
    //public static final Item PINCE = new ItemPince("Pince");
    //public static final Item TOURNIQUET = new ItemTourniquet("Tourniquet");
    public static final Item Bois = new ItemBois("bois");
    public static final Item Acier = new ItemAcier("acier");
    public static final Item Aluminium = new ItemAluminium("aluminium");
    public static final Item Tungstene = new ItemTungstene("tungstene");
    public static final Item ComposantElectronique = new ItemCompoElectro("composant_electronique");
    public static final Item CaoutchoucPetrole = new ItemPetrole("petrole");
    public static final Item Canteen = new ItemCanteen("canteen");
}
