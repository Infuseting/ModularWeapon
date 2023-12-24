package com.modularwarfare.client.model.layers;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.model.ModelBackpack;
import com.modularwarfare.client.model.ModelCustomArmor;
import com.modularwarfare.common.armor.ArmorType;
import com.modularwarfare.common.armor.ItemMWArmor;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLayerBackpack implements LayerRenderer<EntityPlayer> {
    private final ModelRenderer modelRenderer;
    private RenderPlayer renderer;

    public RenderLayerBackpack(final RenderPlayer renderer, final ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
        this.renderer = renderer;
    }

    public void doRenderLayer(final EntityPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {

        if (player.hasCapability(CapabilityExtra.CAPABILITY_ITEM, null)) {
            final IExtraItemHandler extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
            final ItemStack itemstackBackpack = extraSlots.getStackInSlot(0);
            final ItemStack itemStackBackpack2 = extraSlots.getStackInSlot(1);
            final ItemStack itemstackHelmet = extraSlots.getStackInSlot(3);
            final ItemStack itemstackBrassard = extraSlots.getStackInSlot(4);
            if (!itemstackBackpack.isEmpty()) {

                ItemBackpack backpack = (ItemBackpack) itemstackBackpack.getItem();
                GlStateManager.pushMatrix();

                if (player.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.3f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.scale(scale, scale, scale);

                int skinId = 0;
                if (itemstackBackpack.hasTagCompound()) {
                    if (itemstackBackpack.getTagCompound().hasKey("skinId")) {
                        skinId = itemstackBackpack.getTagCompound().getInteger("skinId");
                    }
                }

                String path = skinId > 0 ? backpack.type.modelSkins[skinId].getSkin() : backpack.type.modelSkins[0].getSkin();

                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(ModularWarfare.MOD_ID, "skins/backpacks/" + path + ".png"));

                ModelBackpack model = (ModelBackpack) backpack.type.model;
                model.render("backpackModel", 1f, ((ModelBackpack) backpack.type.model).config.extra.modelScale);

                GlStateManager.disableLighting();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                model.render("backpackModel", 1f, ((ModelBackpack) backpack.type.model).config.extra.modelScale);
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
            }
            if (!itemStackBackpack2.isEmpty()) {

                ItemBackpack backpack = (ItemBackpack) itemStackBackpack2.getItem();
                GlStateManager.pushMatrix();

                if (player.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.3f, 0.0f);
                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.scale(scale, scale, scale);

                int skinId = 0;
                if (itemStackBackpack2.hasTagCompound()) {
                    if (itemStackBackpack2.getTagCompound().hasKey("skinId")) {
                        skinId = itemStackBackpack2.getTagCompound().getInteger("skinId");
                    }
                }

                String path = skinId > 0 ? backpack.type.modelSkins[skinId].getSkin() : backpack.type.modelSkins[0].getSkin();

                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(ModularWarfare.MOD_ID, "skins/backpacks/" + path + ".png"));

                ModelBackpack model = (ModelBackpack) backpack.type.model;
                model.render("backpackModel", 1f, ((ModelBackpack) backpack.type.model).config.extra.modelScale);

                GlStateManager.disableLighting();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                model.render("backpackModel", 1f, ((ModelBackpack) backpack.type.model).config.extra.modelScale);
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
            }
            if (!itemstackHelmet.isEmpty()) {
                final ItemSpecialArmor itemHelmet = (ItemSpecialArmor) (itemstackHelmet.getItem());
                final ArmorType armorType = itemHelmet.type;
                final ModelCustomArmor armorModel = (ModelCustomArmor) itemHelmet.type.bipedModel;
                GlStateManager.pushMatrix();
                if (player.isSneaking()) {
                    GlStateManager.translate(0.0f, 0.25f, 0.0f); // Ajustement vertical vers le haut
                    GlStateManager.rotate(0, 0.5f, 0.0f, 0.0f); // Inclinaison de la tête vers l'avant
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableRescaleNormal();
                final int skinId = 0;
                String path = skinId > 0 ? "skins/" + armorType.modelSkins[skinId].getSkin() : armorType.modelSkins[0].getSkin();

                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(ModularWarfare.MOD_ID, "skins/armor/" + path + ".png"));
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);

                // Appliquer la rotation de la tête au modèle d'armure
                GlStateManager.rotate(netHeadYaw, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(headPitch, 1.0f, 0.0f, 0.0f);

                armorModel.render("headModel", this.renderer.getMainModel().bipedHead, scale, 1f);

                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.popMatrix();
            }
            if (!itemstackBrassard.isEmpty()) {
                final ItemSpecialArmor itemBrassard = (ItemSpecialArmor) (itemstackBrassard.getItem());
                final ArmorType armorType = itemBrassard.type;
                final ModelCustomArmor armorModel = (ModelCustomArmor) itemBrassard.type.bipedModel;
                GlStateManager.pushMatrix();

                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableRescaleNormal();
                final int skinId = 0;
                String path = skinId > 0 ? "skins/" + armorType.modelSkins[skinId].getSkin() : armorType.modelSkins[0].getSkin();
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(ModularWarfare.MOD_ID, "skins/armor/" + path + ".png"));
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                // Rendu du bras gauche
                ModelRenderer armModel = this.renderer.getMainModel().bipedLeftArm;

                // Appliquer les transformations du bras gauche au modèle d'armure
                GlStateManager.rotate(armModel.rotateAngleX * (180.0f / (float)Math.PI), 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(armModel.rotateAngleY * (180.0f / (float)Math.PI), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(armModel.rotateAngleZ * (180.0f / (float)Math.PI), 0.0f, 0.0f, 1.0f);

                armorModel.render("armorModel", this.renderer.getMainModel().bipedBodyWear, scale, 1f);

                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.popMatrix();
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
