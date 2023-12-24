package com.modularwarfare.common.heal;

import com.modularwarfare.ModConfig;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.fpp.basic.configs.GunRenderConfig;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.ExtraContainer;
import com.modularwarfare.common.capability.extraslots.ExtraContainerProvider;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.heal.*;
import com.modularwarfare.common.network.PacketHealSync;
import com.modularwarfare.common.network.PacketSyncExtraSlot;
import com.modularwarfare.utility.RayUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import scala.util.Left;
import scala.util.parsing.combinator.PackratParsers;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = ModularWarfare.MOD_ID)
public class CapabilityHealth {

    @CapabilityInject(IHealth.class)
    public static final Capability<IHealth> CAPABILITY_HEALTH;

    static {
        CAPABILITY_HEALTH = null;
    }

    @SubscribeEvent
    public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(ModularWarfare.MOD_ID, "health"), new HealthContainerProvider(new HealthContainer((EntityPlayer) event.getObject())));

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
            resetStats(player);
            sync(player);
        }

    }
    public static void damageMost(EntityPlayer player, int damage) {
        if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)){
            IHealth getS = ((IHealth)player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, null));
            boolean HeadBreak = true;
            boolean BodyBreak = true;
            boolean LeftArmBreak = true;
            boolean RightArmBreak = true;
            boolean LeftLegBreak = true;
            boolean RightLegBreak = true;
            int nbrofbroken = 0;
            if (!getS.hasBreak("OBB_HEAD")) {
                HeadBreak = false;
                nbrofbroken+=1;
            }
            if (!getS.hasBreak("OBB_BODY")) {
                BodyBreak = false;
                nbrofbroken+=1;
            }
            if (!getS.hasBreak("OBB_LEFTARM")) {
                LeftArmBreak = false;
                nbrofbroken+=1;
            }
            if (!getS.hasBreak("OBB_RIGHTARM")) {
                RightArmBreak = false;
                nbrofbroken+=1;
            }
            if (!getS.hasBreak("OBB_LEFTLEG")) {
                LeftLegBreak = false;
                nbrofbroken+=1;
            }
            if (!getS.hasBreak("OBB_RIGHTLEG")) {
                RightLegBreak = false;
                nbrofbroken+=1;
            }
            if (nbrofbroken > 0) {

                int damagepermember = Math.max((damage / nbrofbroken) * 2, 1);
                if (!HeadBreak) {
                    if (getS.getHealth("OBB_HEAD") - damagepermember >= 0) {

                        getS.setHealth("OBB_HEAD", getS.getHealth("OBB_HEAD") - damagepermember);
                    } else {
                        getS.setHealth("OBB_HEAD", 0);
                        getS.setBreak("OBB_HEAD", true);
                    }
                }
                if (!BodyBreak) {
                    if (getS.getHealth("OBB_BODY") - damagepermember >= 0) {
                        getS.setHealth("OBB_BODY", getS.getHealth("OBB_BODY") - damagepermember);
                    } else {
                        getS.setHealth("OBB_BODY", 0);
                        getS.setBreak("OBB_BODY", true);
                    }
                }
                if (!LeftArmBreak) {
                    if (getS.getHealth("OBB_LEFTARM") - damagepermember >= 0) {
                        getS.setHealth("OBB_LEFTARM", getS.getHealth("OBB_LEFTARM") - damagepermember);
                    } else {
                        getS.setHealth("OBB_LEFTARM", 0);
                        getS.setBreak("OBB_LEFTARM", true);
                    }
                }
                if (!RightArmBreak) {
                    if (getS.getHealth("OBB_RIGHTARM") - damagepermember >= 0) {
                        getS.setHealth("OBB_RIGHTARM", getS.getHealth("OBB_RIGHTARM") - damagepermember);
                    } else {
                        getS.setHealth("OBB_RIGHTARM", 0);
                        getS.setBreak("OBB_RIGHTARM", true);
                    }
                }
                if (!LeftLegBreak) {
                    if (getS.getHealth("OBB_LEFTLEG") - damagepermember >= 0) {
                        getS.setHealth("OBB_LEFTLEG", getS.getHealth("OBB_LEFTLEG") - damagepermember);
                    } else {
                        getS.setHealth("OBB_LEFTLEG", 0);
                        getS.setBreak("OBB_LEFTLEG", true);
                    }
                }
                if (!RightLegBreak) {
                    if (getS.getHealth("OBB_RIGHTLEG") - damagepermember >= 0) {
                        getS.setHealth("OBB_RIGHTLEG", getS.getHealth("OBB_RIGHTLEG") - damagepermember);
                    } else {
                        getS.setHealth("OBB_RIGHTLEG", 0);
                        getS.setBreak("OBB_RIGHTLEG", true);
                    }
                }
            }

        }
    }
    public static void healPlayers() {
        int damage = 1;
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {

                IHealth getS = ((IHealth) player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, null));

                if (!getS.hasBreak("OBB_HEAD")) {

                    if (getS.getHealth("OBB_HEAD") + damage <= 35) {
                        getS.setHealth("OBB_HEAD", getS.getHealth("OBB_HEAD") + damage);
                    } else {
                        getS.setHealth("OBB_HEAD", 35);
                    }


                }
                if (!getS.hasBreak("OBB_BODY")) {

                    if (getS.getHealth("OBB_BODY") + damage <= 85) {
                        getS.setHealth("OBB_BODY", getS.getHealth("OBB_BODY") + damage);
                    } else {
                        getS.setHealth("OBB_BODY", 85);
                    }
                }
                if (!getS.hasBreak("OBB_LEFTARM")) {

                    if (getS.getHealth("OBB_LEFTARM") + damage <= 60) {
                        getS.setHealth("OBB_LEFTARM", getS.getHealth("OBB_LEFTARM") + damage);
                    } else {
                        getS.setHealth("OBB_LEFTARM", 60);
                    }
                }
                if (!getS.hasBreak("OBB_RIGHTARM")) {

                    if (getS.getHealth("OBB_RIGHTARM") + damage <= 60) {
                        getS.setHealth("OBB_RIGHTARM", getS.getHealth("OBB_HEAD") + damage);
                    } else {
                        getS.setHealth("OBB_RIGHTARM", 60);
                    }
                }
                if (!getS.hasBreak("OBB_LEFTLEG")) {
                    if (getS.getHealth("OBB_LEFTLEG") + damage <= 65) {
                        getS.setHealth("OBB_LEFTLEG", getS.getHealth("OBB_LEFTLEG") + damage);
                    } else {
                        getS.setHealth("OBB_LEFTLEG", 65);
                    }
                }
                if (!getS.hasBreak("OBB_RIGHTLEG")) {

                    if (getS.getHealth("OBB_RIGHTLEG") + damage <= 65) {
                        getS.setHealth("OBB_RIGHTLEG", getS.getHealth("OBB_RIGHTLEG") + damage);
                    } else {
                        getS.setHealth("OBB_RIGHTLEG", 65);
                    }
                }

                sync(player);
            }
        }
    }
    public static void damagePlayersHemoLourd() {
        int damage = 6;
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {

                IHealth getS = ((IHealth) player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, null));

                if (getS.hasHardBleed("OBB_HEAD")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_HEAD")) {
                        RayUtil.attackEntityWithoutKnockback(player, DamageSource.OUT_OF_WORLD.setProjectile(), 1000);
                    } else {
                        if (getS.getHealth("OBB_HEAD") - damage >= 0) {
                            getS.setHealth("OBB_HEAD", getS.getHealth("OBB_HEAD") - damage);
                        } else {
                            getS.setHealth("OBB_HEAD", 0);
                            getS.setBreak("OBB_HEAD", true);
                        }

                    }
                }
                if (getS.hasHardBleed("OBB_BODY")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_BODY")) {
                        RayUtil.attackEntityWithoutKnockback(player, DamageSource.OUT_OF_WORLD.setProjectile(), 1000);
                    } else {
                        if (getS.getHealth("OBB_BODY") - damage >= 0) {
                            getS.setHealth("OBB_BODY", getS.getHealth("OBB_BODY") - damage);
                        } else {
                            getS.setHealth("OBB_BODY", 0);
                            getS.setBreak("OBB_BODY", true);
                        }

                    }
                }
                if (getS.hasHardBleed("OBB_LEFTARM")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_LEFTARM")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_LEFTARM") - damage >= 0) {
                            getS.setHealth("OBB_LEFTARM", getS.getHealth("OBB_LEFTARM") - damage);
                        } else {
                            getS.setHealth("OBB_LEFTARM", 0);
                            getS.setBreak("OBB_LEFTARM", true);

                        }

                    }
                }
                if (getS.hasHardBleed("OBB_RIGHTARM")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_RIGHTARM")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_RIGHTARM") - damage >= 0) {
                            getS.setHealth("OBB_RIGHTARM", getS.getHealth("OBB_RIGHTARM") - damage);
                        } else {
                            getS.setHealth("OBB_RIGHTARM", 0);
                            getS.setBreak("OBB_RIGHTARM", true);
                        }

                    }
                }
                if (getS.hasHardBleed("OBB_LEFTLEG")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_LEFTLEG")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_LEFTLEG") - damage >= 0) {
                            getS.setHealth("OBB_LEFTLEG", getS.getHealth("OBB_LEFTLEG") - damage);
                        } else {
                            getS.setHealth("OBB_LEFTLEG", 0);
                            getS.setBreak("OBB_LEFTLEG", true);
                        }

                    }
                }
                if (getS.hasHardBleed("OBB_RIGHTLEG")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_RIGHTLEG")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_RIGHTLEG") - damage >= 0) {
                            getS.setHealth("OBB_RIGHTLEG", getS.getHealth("OBB_RIGHTLEG") - damage);
                        } else {
                            getS.setHealth("OBB_RIGHTLEG", 0);
                            getS.setBreak("OBB_RIGHTLEG", true);
                        }

                    }
                }
                if (getS.getHealth("OBB_HEAD") + getS.getHealth("OBB_BODY") +getS.getHealth("OBB_LEFTARM") +getS.getHealth("OBB_RIGHTARM") +getS.getHealth("OBB_LEFTLEG") +getS.getHealth("OBB_RIGHTLEG") <= 0) {
                    player.setHealth(0);
                    getS.setHealth("OBB_HEAD", 35);
                }
                sync(player);
            }
        }
    }
    public static void damagePlayersHemoLeger(){
        int damage = 3;
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
                IHealth getS = ((IHealth) player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, null));
                if (getS.hasLightBleed("OBB_HEAD")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_HEAD")) {
                        RayUtil.attackEntityWithoutKnockback(player, DamageSource.OUT_OF_WORLD.setProjectile(), 1000);
                    } else {
                        if (getS.getHealth("OBB_HEAD") - damage >= 0) {
                            getS.setHealth("OBB_HEAD", getS.getHealth("OBB_HEAD") - damage);
                        } else {
                            getS.setHealth("OBB_HEAD", 0);
                            getS.setBreak("OBB_HEAD", true);
                        }

                    }
                }
                if (getS.hasLightBleed("OBB_BODY")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_BODY")) {
                        RayUtil.attackEntityWithoutKnockback(player, DamageSource.OUT_OF_WORLD.setProjectile(), 1000);
                    } else {
                        if (getS.getHealth("OBB_BODY") - damage >= 0) {
                            getS.setHealth("OBB_BODY", getS.getHealth("OBB_BODY") - damage);
                        } else {
                            getS.setHealth("OBB_BODY", 0);
                            getS.setBreak("OBB_BODY", true);
                        }

                    }
                }
                if (getS.hasLightBleed("OBB_LEFTARM")) {
                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_LEFTARM")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_LEFTARM") - damage >= 0) {
                            getS.setHealth("OBB_LEFTARM", getS.getHealth("OBB_LEFTARM") - damage);
                        } else {
                            getS.setHealth("OBB_LEFTARM", 0);
                            getS.setBreak("OBB_LEFTARM", true);
                        }

                    }
                }
                if (getS.hasLightBleed("OBB_RIGHTARM")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_RIGHTARM")) {
                        damageMost(player, damage);
                    }else {
                        if (getS.getHealth("OBB_RIGHTARM") - damage >= 0) {
                            getS.setHealth("OBB_RIGHTARM", getS.getHealth("OBB_RIGHTARM") - damage);
                        } else {
                            getS.setHealth("OBB_RIGHTARM", 0);
                            getS.setBreak("OBB_RIGHTARM", true);
                        }
                    }
                }
                if (getS.hasLightBleed("OBB_LEFTLEG")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_LEFTLEG")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_LEFTLEG") - damage >= 0) {
                            getS.setHealth("OBB_LEFTLEG", getS.getHealth("OBB_LEFTLEG") - damage);
                        } else {
                            getS.setHealth("OBB_LEFTLEG", 0);
                            getS.setBreak("OBB_LEFTLEG", true);
                        }

                    }
                }
                if (getS.hasLightBleed("OBB_RIGHTLEG")) {

                    ModularWarfare.PROXY.addBlood(player, 10, true);
                    if (getS.hasBreak("OBB_RIGHTLEG")) {
                        damageMost(player, damage);
                    } else {
                        if (getS.getHealth("OBB_RIGHTLEG") - damage >= 0) {
                            getS.setHealth("OBB_RIGHTLEG", getS.getHealth("OBB_RIGHTLEG") - damage);
                        } else {
                            getS.setHealth("OBB_RIGHTLEG", 0);
                            getS.setBreak("OBB_RIGHTLEG", true);
                        }

                    }
                }
                if (getS.getHealth("OBB_HEAD") + getS.getHealth("OBB_BODY") +getS.getHealth("OBB_LEFTARM") +getS.getHealth("OBB_RIGHTARM") +getS.getHealth("OBB_LEFTLEG") +getS.getHealth("OBB_RIGHTLEG") <= 0) {
                    player.setHealth(0);
                    getS.setHealth("OBB_HEAD", 35);
                }
                sync(player);
            }
        }

    }
    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        IHealth oldHandler= (IHealth) event.getOriginal().getCapability((Capability) CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null);
        IHealth newHandler= (IHealth) event.getEntityPlayer().getCapability((Capability) CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null);
        if (event.getEntityPlayer().hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
            for (int i = 0; i<=5; i++) {
                String part;
                switch (i) {
                    case 0: part = "OBB_HEAD"; break;
                    case 1: part = "OBB_BODY"; break;
                    case 2: part = "OBB_LEFTARM"; break;
                    case 3: part = "OBB_RIGHTARM"; break;
                    case 4: part = "OBB_LEFTLEG"; break;
                    case 5: part = "OBB_RIGHTLEG"; break;
                    default: part = "OBB_BODY";
                }
                newHandler.setHealth(part, oldHandler.getHealth(part));
                newHandler.setLightBleed(part, oldHandler.hasLightBleed(part));
                newHandler.setHardBleed(part, oldHandler.hasHardBleed(part));
                newHandler.setBulletIn(part, oldHandler.hasBulletIn(part));
                newHandler.setBreak(part, oldHandler.hasBreak(part));

            }
        }
        sync(event.getEntityPlayer());
    }
    public static void resetStats(EntityPlayer player) {
        if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
            for (int i = 0; i<=5; i++) {
                String part;
                switch (i) {
                    case 0: part = "OBB_HEAD"; break;
                    case 1: part = "OBB_BODY"; break;
                    case 2: part = "OBB_LEFTARM"; break;
                    case 3: part = "OBB_RIGHTARM"; break;
                    case 4: part = "OBB_LEFTLEG"; break;
                    case 5: part = "OBB_RIGHTLEG"; break;
                    default: part = "OBB_BODY";
                }
                int value;
                switch(i) {
                    case 0: value = 35; break;
                    case 1: value = 85; break;
                    case 2: value = 60; break;
                    case 3: value = 60; break;
                    case 4: value = 65; break;
                    case 5: value = 65; break;
                    default: value = 85;
                }
                player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth(part, value);
                player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setLightBleed(part, false);
                player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHardBleed(part, false);
                player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBulletIn(part, false);
                player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBreak(part, false);

            }
        }

    }
    public static void sync(final EntityPlayer entity) {
        if (entity.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
            for (int i = 0; i<=5; i++) {
                String part;
                switch (i) {
                    case 0: part = "OBB_HEAD"; break;
                    case 1: part = "OBB_BODY"; break;
                    case 2: part = "OBB_LEFTARM"; break;
                    case 3: part = "OBB_RIGHTARM"; break;
                    case 4: part = "OBB_LEFTLEG"; break;
                    case 5: part = "OBB_RIGHTLEG"; break;
                    default: part = "OBB_BODY";
                }
                IHealth Sget = ((IHealth) entity.getCapability((Capability) CapabilityHealth.CAPABILITY_HEALTH, null));
                final PacketHealSync msg = new PacketHealSync(entity, i, Sget.getHealth(part), Sget.hasLightBleed(part), Sget.hasHardBleed(part),Sget.hasBreak(part), Sget.hasBulletIn(part));

                ModularWarfare.NETWORK.sendTo(msg, (EntityPlayerMP) entity);



            }
        }





    }
    public static class StorageHealth implements Capability.IStorage<IHealth> {
        @Override
        public NBTBase writeNBT(Capability<IHealth> capability, IHealth instance, EnumFacing side)
        {
            NBTTagCompound compound = new NBTTagCompound();
            for (EnumBodyPart bodyPart : EnumBodyPart.values()) {
                compound.setFloat(bodyPart.name() +  "_health",instance.getHealth(bodyPart.name()));
                compound.setBoolean(bodyPart.name() +  "_lightBleed",instance.hasLightBleed(bodyPart.name() ));
                compound.setBoolean(bodyPart.name() +  "_hardBleed",instance.hasHardBleed(bodyPart.name() ));
                compound.setBoolean(bodyPart.name() +  "_break",instance.hasBreak(bodyPart.name()));
                compound.setBoolean(bodyPart.name() +  "_bulletIn",instance.hasBulletIn(bodyPart.name() ));



            }
            return compound;

        }

        @Override
        public void readNBT(Capability<IHealth> capability, IHealth instance, EnumFacing side, NBTBase nbt)
        {
            if(nbt instanceof NBTTagCompound)
            {
                NBTTagCompound tag = (NBTTagCompound)nbt;
                for (EnumBodyPart bodyPart : EnumBodyPart.values()) {
                    float health = tag.getFloat(bodyPart.name() + "_health");
                    instance.setHealth(bodyPart.name(), health);

                    boolean lightBleed = tag.getBoolean(bodyPart.name() + "_lightBleed");
                    instance.setLightBleed(bodyPart.name(), lightBleed);

                    boolean hardBleed = tag.getBoolean(bodyPart.name() + "_hardBleed");
                    instance.setHardBleed(bodyPart.name(), hardBleed);
                    boolean breakI = tag.getBoolean(bodyPart.name() + "_break");
                    instance.setBreak(bodyPart.name(), breakI);
                    boolean bulletIn = tag.getBoolean(bodyPart.name() + "_bulletIn");
                    instance.setBulletIn(bodyPart.name(), bulletIn);



                }
            }
        }
    }
}