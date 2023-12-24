package com.modularwarfare.common.Thirst;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.common.heal.*;
import com.modularwarfare.common.network.PacketHealSync;
import com.modularwarfare.common.network.PacketThirstSync;
import com.modularwarfare.utility.RayUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModularWarfare.MOD_ID)
public class CapabilityThirst {

    @CapabilityInject(IThirst.class)
    public static final Capability<IThirst> CAPABILITY_THIRST;

    static {
        CAPABILITY_THIRST = null;
    }

    @SubscribeEvent
    public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(ModularWarfare.MOD_ID, "thirst"), new ThirstContainerProvider(new ThirstContainer((EntityPlayer) event.getObject())));

        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(final EntityJoinWorldEvent event) {

        if (event.getWorld().isRemote) {
            return;
        }
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer target = (EntityPlayer) event.getEntity();

        if (ModularWarfare.PROXY instanceof ClientProxy) {
            return;
        }
        sync(target);
    }

    @SubscribeEvent
    public static void onStartTracking(final PlayerEvent.StartTracking event) {
        if (event.getTarget().world.isRemote) {
            return;
        }
        if (!(event.getTarget() instanceof EntityPlayer)) {
            return;
        }
        if (ModularWarfare.PROXY instanceof ClientProxy) {
            return;
        }
        sync((EntityPlayer) event.getTarget());
    }

    @SubscribeEvent
    public static void playerDeath(final LivingDeathEvent event) {
        if(event.getEntity() instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) event.getEntity();
            final World world = player.world;
            if (player.hasCapability(CAPABILITY_THIRST, null)) {
                ((IThirst) player.getCapability((Capability) CAPABILITY_THIRST, null)).setThirst(144000F);
                ((IThirst) player.getCapability((Capability) CAPABILITY_THIRST, null)).setHydration(6000F);
                ((IThirst) player.getCapability((Capability) CAPABILITY_THIRST, null)).setExhaustion(0F);
            }
            sync(player);
        }

    }
    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        IThirst oldHandler= (IThirst) event.getOriginal().getCapability((Capability) CAPABILITY_THIRST, (EnumFacing) null);
        IThirst newHandler= (IThirst) event.getEntityPlayer().getCapability((Capability) CAPABILITY_THIRST, (EnumFacing) null);
        if (event.getEntityPlayer().hasCapability(CAPABILITY_THIRST, null)) {
            newHandler.setThirst(oldHandler.getThirst());
            newHandler.setHydration(oldHandler.getHydration());
            newHandler.setExhaustion(oldHandler.getExhaustion());
        }
        sync(event.getEntityPlayer());
    }
    public static void sync(final EntityPlayer entity) {
        if (entity.hasCapability(CAPABILITY_THIRST, null)) {


            IThirst Sget = ((IThirst) entity.getCapability((Capability) CAPABILITY_THIRST, null));
            final PacketThirstSync msg = new PacketThirstSync(entity, Sget.getThirst(), Sget.getHydration(), Sget.getExhaustion());

            ModularWarfare.NETWORK.sendTo(msg, (EntityPlayerMP) entity);




        }
    }





}