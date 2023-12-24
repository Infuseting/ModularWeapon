package com.modularwarfare.client.gui;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.container.ContainerInventoryModified;
import com.modularwarfare.common.heal.CapabilityHealth;
import com.modularwarfare.common.heal.IHealth;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiInventoryModified extends InventoryEffectRenderer {
    public static final ResourceLocation ICONS;
    public static final ResourceLocation INVENTORY_BG;

    static {
        ICONS = new ResourceLocation(ModularWarfare.MOD_ID, "textures/gui/icons.png");
        INVENTORY_BG = new ResourceLocation(ModularWarfare.MOD_ID, "textures/gui/inventory.png");
    }

    private float oldMouseX;

    private EntityPlayer player;
    private float oldMouseY;

    public GuiInventoryModified(final EntityPlayer player) {
        super(new ContainerInventoryModified(player.inventory, !player.getEntityWorld().isRemote, player));
        this.allowUserInput = true;
        this.xSize = 176;
        /** The Y size of the inventory window in pixels. */
        this.ySize = 185;
        this.player = player;
    }

    private void resetGuiLeft() {
        this.guiLeft = (this.width - this.xSize) / 2;
    }

    public void updateScreen() {
        this.updateActivePotionEffects();
        this.resetGuiLeft();
    }

    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        this.resetGuiLeft();
    }

    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        //this.fontRenderer.drawString(I18n.format("PlayerStats:", new Object[0]), 103, 10, 20210712);
        //this.fontRenderer.drawString(I18n.format("- Deaths ", new Object[0]), 103, 19, 20210712);

        if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
            IHealth getS =  (((IHealth) player.getCapability((Capability) CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null)));
            this.fontRenderer.drawString(I18n.format("Tête", new Object[0]), -60, 0, 20210712);
            this.fontRenderer.drawString(I18n.format(((" " + getS.getHealth("OBB_HEAD") + "/35.0") ), new Object[0]), -55, 10, 0xFFFFFF);
            this.fontRenderer.drawString(I18n.format("Corps", new Object[0]), -60, 20, 20210712);
            this.fontRenderer.drawString(I18n.format(((" " + getS.getHealth("OBB_BODY")+ "/85.0")), new Object[0]), -55, 30, 0xFFFFFF);
            this.fontRenderer.drawString(I18n.format("Bras Gauche", new Object[0]), -60, 40, 20210712);
            this.fontRenderer.drawString(I18n.format(((" " + getS.getHealth("OBB_LEFTARM")+ "/60.0")), new Object[0]), -55, 50, 0xFFFFFF);
            this.fontRenderer.drawString(I18n.format("Bras Droit", new Object[0]), -60, 60, 20210712);
            this.fontRenderer.drawString(I18n.format(((" " + getS.getHealth("OBB_RIGHTARM")+ "/60.0")), new Object[0]), -55, 70, 0xFFFFFF);
            this.fontRenderer.drawString(I18n.format("Jambe Gauche", new Object[0]), -60, 80, 20210712);
            this.fontRenderer.drawString(I18n.format(((" " + getS.getHealth("OBB_LEFTLEG")+ "/65.0")), new Object[0]), -55, 90, 0xFFFFFF);
            this.fontRenderer.drawString(I18n.format("Jambe Droite", new Object[0]), -60, 100, 20210712);
            this.fontRenderer.drawString(I18n.format(((" " + getS.getHealth("OBB_RIGHTLEG")+ "/65.0")), new Object[0]), -55, 110, 0xFFFFFF);
        }
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (!(Minecraft.getMinecraft().player.openContainer instanceof ContainerInventoryModified)) {
            return;
        }
        this.drawDefaultBackground();
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        this.mc.getTextureManager().bindTexture(GuiInventoryModified.INVENTORY_BG);
        final int k = this.guiLeft;
        final int l = this.guiTop;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        final ContainerInventoryModified containter = (ContainerInventoryModified) Minecraft.getMinecraft().player.openContainer;
        final IItemHandler backpack = (IItemHandler) containter.extra;
        if (backpack.getStackInSlot(0).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing) null)) {
            final ItemStack stack = backpack.getStackInSlot(0);
            final IItemHandler backpackInv = (IItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing) null);
            int xP = 0;
            int yP = 0;
            final int x = k + 180;
            final int y = l + 18;
            this.mc.getTextureManager().bindTexture(GuiInventoryModified.ICONS);
            this.drawTexturedModalRect(x - 5, y - 18, 18, 0, 82, 18);
            this.drawTexturedModalRect(x - 5, y, 18, 5, 82, 18);
            for (int i = 0; i < backpackInv.getSlots(); ++i) {
                this.drawSlotBackground(x + xP * 18, -1 + y + yP * 18);
                if (++xP % 4 == 0) {
                    xP = 0;
                    ++yP;
                    if (i + 1 < backpackInv.getSlots()) {
                        this.drawTexturedModalRect(x - 5, y + yP * 18, 18, 5, 82, 18);
                    }
                } else if (i + 1 >= backpackInv.getSlots()) {
                    ++yP;
                }
            }
            this.drawTexturedModalRect(x - 5, -1 + y + yP * 18, 18, 33, 82, 5);

            if (stack != null) {
                ItemBackpack backpackItem = (ItemBackpack) stack.getItem();
                this.fontRenderer.drawString(backpackItem.type.displayName, x, y - 12, 16777215);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f);
        }
        if (backpack.getStackInSlot(1).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing) null)) {
            final ItemStack stack = backpack.getStackInSlot(1);
            final IItemHandler backpackInv = (IItemHandler) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing) null);
            int xP = 0;
            int yP = 0;
            final int x = k + 180;
            final int y = l + 18 + 95;
            this.mc.getTextureManager().bindTexture(GuiInventoryModified.ICONS);
            this.drawTexturedModalRect(x - 5, y - 18, 18, 0, 82, 18);
            this.drawTexturedModalRect(x - 5, y, 18, 5, 82, 18);
            for (int i = 0; i < backpackInv.getSlots(); ++i) {
                this.drawSlotBackground(x + xP * 18, -1 + y + yP * 18);
                if (++xP % 4 == 0) {
                    xP = 0;
                    ++yP;
                    if (i + 1 < backpackInv.getSlots()) {
                        this.drawTexturedModalRect(x - 5, y + yP * 18, 18, 5, 82, 18);
                    }
                } else if (i + 1 >= backpackInv.getSlots()) {
                    ++yP;
                }
            }
            this.drawTexturedModalRect(x - 5, -1 + y + yP * 18, 18, 33, 82, 5);

            if (stack != null) {
                ItemBackpack backpackItem = (ItemBackpack) stack.getItem();
                this.fontRenderer.drawString(backpackItem.type.displayName, x, y - 12 + 92, 16777215);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f);
        }
        GuiPlayerInventory.drawEntityOnScreen(k + 51, l + 75, 30, k + 51 - this.oldMouseX, l + 75 - 50 - this.oldMouseY, (EntityLivingBase) this.mc.player);
    }

    protected void actionPerformed(final GuiButton button) {
    }

    public void drawSlotBackground(final int x, final int y) {
        this.mc.getTextureManager().bindTexture(GuiInventoryModified.ICONS);
        this.drawTexturedModalRect(x, y, 0, 0, 18, 18);
    }
}
