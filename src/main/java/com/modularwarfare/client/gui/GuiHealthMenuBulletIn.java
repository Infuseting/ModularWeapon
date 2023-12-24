package com.modularwarfare.client.gui;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.Thirst.CapabilityThirst;
import com.modularwarfare.common.heal.CapabilityHealth;
import com.modularwarfare.common.network.PacketHealPart;
import fr.aym.acsguis.component.GuiComponent;
import fr.aym.acsguis.component.layout.GuiScaler;
import fr.aym.acsguis.component.panel.GuiFrame;
import fr.aym.acsguis.component.style.PanelStyleManager;
import fr.aym.acsguis.component.textarea.GuiLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

import static com.modularwarfare.client.gui.GuiHealthMenuLightBleed.CSSHEALTHMENU;

public class GuiHealthMenuBulletIn extends GuiFrame {
    private GuiLabel load;
    public EntityPlayer player;
    public GuiHealthMenuBulletIn() {
        super((GuiScaler)new GuiScaler.AdjustFullScreen());
        ((PanelStyleManager)this.style).setBackgroundColor(3);
        setCssClass("home");
        GuiLabel TeteHealth = new GuiLabel("Tête");
        TeteHealth.setCssClass("tete");
        if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
            if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH,null).hasBulletIn("OBB_HEAD")) {

            }
            else{
                TeteHealth.setCssId("buttonDisabled");
            }
        }
        else {
            TeteHealth.setCssId("buttonDisabled");
        }
        TeteHealth.addClickListener((x, y, bu) -> ModularWarfare.NETWORK.sendToServer(new PacketHealPart(Minecraft.getMinecraft().player,player,0, 2, 0)));
        add((GuiComponent)TeteHealth);
        GuiLabel BodyHealth = new GuiLabel("Corps");
        BodyHealth.setCssClass("Body");
        if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
            if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH,null).hasBulletIn("OBB_BODY")) {

            }

            else{
                TeteHealth.setCssId("buttonDisabled");
            }
        }
        else {
            TeteHealth.setCssId("buttonDisabled");
        }
        BodyHealth.addClickListener((x, y, bu) -> ModularWarfare.NETWORK.sendToServer(new PacketHealPart(Minecraft.getMinecraft().player,player,1, 2, 0)));
        add((GuiComponent)BodyHealth);
        GuiLabel LeftArmHealth = new GuiLabel("Bras Gauche");
        LeftArmHealth.setCssClass("LeftArm");
        if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
            if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH,null).hasBulletIn("OBB_LEFTARM")) {

            }

            else{
                TeteHealth.setCssId("buttonDisabled");
            }
        }
        else {
            TeteHealth.setCssId("buttonDisabled");
        }
        LeftArmHealth.addClickListener((x, y, bu) -> ModularWarfare.NETWORK.sendToServer(new PacketHealPart(Minecraft.getMinecraft().player,player,2, 2, 0)));
        add((GuiComponent)LeftArmHealth);
        GuiLabel RightArmHealth = new GuiLabel("Bras Droite");
        RightArmHealth.setCssClass("RightArm");
        if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
            if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH,null).hasBulletIn("OBB_RIGHTARM")) {

            }

            else{
                TeteHealth.setCssId("buttonDisabled");
            }
        }
        else {
            TeteHealth.setCssId("buttonDisabled");
        }
        RightArmHealth.addClickListener((x, y, bu) -> ModularWarfare.NETWORK.sendToServer(new PacketHealPart(Minecraft.getMinecraft().player,player,3, 2, 0)));
        add((GuiComponent)RightArmHealth);
        GuiLabel LeftLegHealth = new GuiLabel("Jambe Gauche");
        LeftLegHealth.setCssClass("LeftLeg");
        if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
            if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH,null).hasBulletIn("OBB_LEFTLEG")) {

            }

            else{
                TeteHealth.setCssId("buttonDisabled");
            }
        }
        else {
            TeteHealth.setCssId("buttonDisabled");
        }
        LeftLegHealth.addClickListener((x, y, bu) ->ModularWarfare.NETWORK.sendToServer(new PacketHealPart(Minecraft.getMinecraft().player,player,4, 2, 0)));
        add((GuiComponent)LeftLegHealth);
        GuiLabel RightLegHealth = new GuiLabel("Jambe Droite");
        RightLegHealth.setCssClass("RightLeg");
        if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
            if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH,null).hasBulletIn("OBB_RIGHTLEG")) {

            }

            else{
                TeteHealth.setCssId("buttonDisabled");
            }
        }
        else {
            TeteHealth.setCssId("buttonDisabled");
        }
        RightLegHealth.addClickListener((x, y, bu) -> ModularWarfare.NETWORK.sendToServer(new PacketHealPart(Minecraft.getMinecraft().player,player,5, 2, 0)));
        add((GuiComponent)RightLegHealth);
    }



    public List<ResourceLocation> getCssStyles() {
        return Collections.singletonList(CSSHEALTHMENU);
    }

    public boolean needsCssReload() {
        return false;
    }
}

