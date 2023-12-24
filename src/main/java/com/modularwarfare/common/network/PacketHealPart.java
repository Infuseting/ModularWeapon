package com.modularwarfare.common.network;

import com.modularwarfare.client.gui.*;
import com.modularwarfare.common.heal.CapabilityHealth;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;

public class PacketHealPart extends PacketBase {

    int player;
    int target;
    int bodyPart;
    int type;
    int value;


    public PacketHealPart() {
    }

    public PacketHealPart(final EntityPlayer player, final EntityPlayer target, final int bodyPart, int type, int value) {
        this.player = player.getEntityId();
        this.target = target.getEntityId();
        this.bodyPart = bodyPart;
        this.type = type;
        this.value = value;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(this.player);
        data.writeInt(this.target);
        data.writeInt(this.bodyPart);
        data.writeInt(this.type);
        data.writeInt(this.value);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.player = data.readInt();
        this.target = data.readInt();
        this.bodyPart = data.readInt();
        this.type = data.readInt();
        this.value = data.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayerMP playerEntity) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Entity p = Minecraft.getMinecraft().world.getEntityByID(player);
            String part;
            switch (bodyPart) {
                case 0: part = "OBB_HEAD"; break;
                case 1: part = "OBB_BODY"; break;
                case 2: part = "OBB_LEFTARM"; break;
                case 3: part = "OBB_RIGHTARM"; break;
                case 4: part = "OBB_LEFTLEG"; break;
                case 5: part = "OBB_RIGHTLEG"; break;
                default: part = "OBB_BODY";
            }
            switch (type) {
                case 0: p .getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setLightBleed(part, false);
                case 1: p .getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHardBleed(part, false);
                case 2: p .getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBulletIn(part, false);
                case 3: {
                    p.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBreak(part, false);
                    p .getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth(part, 1);
                }
                case 4: p .getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth(part, value);

            }
        });

    }

    @Override
    public void handleClientSide(EntityPlayer clientPlayer) {
        if (clientPlayer != null) {
            //if (type == 0) {
            //    new GuiHealthMenuLightBleed().player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(target);
            //    Minecraft.getMinecraft().displayGuiScreen(new GuiHealthMenuLightBleed().getGuiScreen());
//
            //}
            //if (type == 1) {
            //    new GuiHealthMenuLightBleed().player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(target);
            //    Minecraft.getMinecraft().displayGuiScreen(new GuiHealthMenuHardBleed().getGuiScreen());
//
            //}
            //if (type == 2) {
            //    new GuiHealthMenuLightBleed().player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(target);
            //    Minecraft.getMinecraft().displayGuiScreen(new GuiHealthMenuBulletIn().getGuiScreen());
//
            //}
            //if (type == 3) {
            //    new GuiHealthMenuLightBleed().player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(target);
            //    Minecraft.getMinecraft().displayGuiScreen(new GuiHealthMenuBreak().getGuiScreen());
//
            //}
            //if (type == 4) {
            //    new GuiHealthMenuLightBleed().player = (EntityPlayer) Minecraft.getMinecraft().world.getEntityByID(target);
            //    Minecraft.getMinecraft().displayGuiScreen(new GuiHealthMenuHeal().getGuiScreen());
//
            //}
        }
    }
}
