package com.modularwarfare.client.model.layers;


import com.modularwarfare.ModularWarfare;
import com.modularwarfare.api.MWArmorType;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.model.ModelBackpack;
import com.modularwarfare.client.model.ModelCustomArmor;
import com.modularwarfare.client.fpp.basic.models.objects.CustomItemRenderType;
import com.modularwarfare.common.armor.ArmorType;
import com.modularwarfare.common.armor.ItemMWArmor;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.network.BackWeaponsManager;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;

import com.modularwarfare.loader.MWModelBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLayerHelmet implements LayerRenderer<EntityPlayer> {
    private final ModelRenderer modelRenderer;
    private RenderPlayer renderer;

    public RenderLayerHelmet(final RenderPlayer renderer, final ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
        this.renderer = renderer;
    }

    public void doRenderLayer(final EntityPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {

        if (player.hasCapability(CapabilityExtra.CAPABILITY_ITEM, null)) {
            final IExtraItemHandler extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
            final ItemStack itemstackHelmet = extraSlots.getStackInSlot(3);

            final ItemSpecialArmor itemHelmet = (ItemSpecialArmor) (itemstackHelmet.getItem());
            if (!itemstackHelmet.isEmpty()) {
                final ArmorType armorType = itemHelmet.type;
                final ModelCustomArmor armorModel = (ModelCustomArmor) itemHelmet.type.bipedModel;
                GlStateManager.pushMatrix();
                if (player.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                    GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableRescaleNormal();
                final int skinId = 0;
                String path = skinId > 0 ? "skins/" + armorType.modelSkins[skinId].getSkin() : armorType.modelSkins[0].getSkin();

                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(ModularWarfare.MOD_ID, "skins/armor/" + path + ".png"));
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                armorModel.render("armorModel", this.renderer.getMainModel().bipedHead, 0.0625f, 1f);
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.popMatrix();
            }
        }
    }



    public boolean shouldCombineTextures() {
        return true;
    }
}