package com.modularwarfare.common.network;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.heal.CapabilityHealth;
import com.modularwarfare.common.hitbox.hits.BulletHit;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import scala.Int;

import java.nio.Buffer;

import static com.modularwarfare.common.heal.CapabilityHealth.sync;

public class PacketHealSync extends PacketBase {

    int player;
    int bodyPart;
    boolean LightBleed;
    boolean HardBleed;
    boolean hasbreak;
    boolean BulletIn;
    Float Health;


    public PacketHealSync() {
    }

    public PacketHealSync(final EntityPlayer player, final int bodyPart, final Float Health, final boolean lightBleed, final boolean HardBleed, final boolean hasBreak, final boolean BulletIn) {
        this.player = player.getEntityId();
        this.bodyPart = bodyPart;
        this.Health = Health;
        this.LightBleed = lightBleed;
        this.HardBleed = HardBleed;
        this.hasbreak = hasBreak;
        this.BulletIn = BulletIn;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(this.player);
        data.writeInt(this.bodyPart);
        data.writeFloat(this.Health);
        data.writeBoolean(this.LightBleed);
        data.writeBoolean(this.HardBleed);
        data.writeBoolean(this.hasbreak);
        data.writeBoolean(this.BulletIn);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.player = data.readInt();
        this.bodyPart = data.readInt();
        this.Health = data.readFloat();
        this.LightBleed = data.readBoolean();
        this.HardBleed = data.readBoolean();
        this.hasbreak = data.readBoolean();
        this.BulletIn = data.readBoolean();
    }

    @Override
    public void handleServerSide(EntityPlayerMP playerEntity) {

    }

    @Override
    public void handleClientSide(EntityPlayer clientPlayer) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Entity p = Minecraft.getMinecraft().world.getEntityByID(player);
            if (p != null && p instanceof EntityPlayer) {
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
                p.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth(part,Health);
                p.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setLightBleed(part,LightBleed);
                p.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHardBleed(part,HardBleed);
                p.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBreak(part,hasbreak);
                p.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBulletIn(part,BulletIn);
            }

        });
    }
}