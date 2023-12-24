package com.modularwarfare.common.network;

import com.modularwarfare.common.Thirst.CapabilityThirst;
import com.modularwarfare.common.heal.CapabilityHealth;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;

public class PacketThirstSync extends PacketBase {

    int player;
    float Thirst;
    float Hydration;
    float Exhaustion;
    public PacketThirstSync(){

    }
    public PacketThirstSync(final EntityPlayer player, final float Thirst, final float Hydration, final float Exhaustion) {
        this.player  = player.getEntityId();
        this.Thirst = Thirst;
        this.Hydration = Hydration;
        this.Exhaustion = Exhaustion;
    }
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(this.player);
        data.writeFloat(this.Thirst);
        data.writeFloat(this.Exhaustion);
        data.writeFloat(this.Hydration);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.player = data.readInt();
        this.Thirst = data.readFloat();
        this.Hydration = data.readFloat();
        this.Exhaustion = data.readFloat();
    }
    @Override
    public void handleServerSide(EntityPlayerMP playerEntity) {

    }
    @Override
    public void handleClientSide(EntityPlayer clientPlayer) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Entity p = Minecraft.getMinecraft().world.getEntityByID(player);
            if (p != null && p instanceof EntityPlayer) {
                p.getCapability(CapabilityThirst.CAPABILITY_THIRST, null).setThirst(Thirst);
                p.getCapability(CapabilityThirst.CAPABILITY_THIRST, null).setExhaustion(Exhaustion);
                p.getCapability(CapabilityThirst.CAPABILITY_THIRST, null).setHydration(Hydration);
            }
        });
    }

}
