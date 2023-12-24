package com.modularwarfare.common.handler;

import com.modularwarfare.ModConfig;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.Thirst.CapabilityThirst;
import com.modularwarfare.common.Thirst.IThirst;
import com.modularwarfare.common.entity.item.EntityItemLoot;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.heal.CapabilityHealth;
import com.modularwarfare.common.heal.HealthContainer;
import com.modularwarfare.common.heal.IHealth;
import com.modularwarfare.common.network.PacketExplosion;
import com.modularwarfare.common.network.PacketOpenGui;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.world.ModularWarfareWorldListener;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Collection;
import java.util.Random;

import static com.modularwarfare.common.heal.CapabilityHealth.*;

public class CommonEventHandler {

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @SubscribeEvent
    public void onLivingAttack(final LivingAttackEvent event) {
        if (!event.getEntityLiving().world.isRemote)
            return;
        final Entity entity = event.getEntity();
        if (entity.getEntityWorld().isRemote) {
            ModularWarfare.PROXY.addBlood(event.getEntityLiving(), 10, true);
        }
    }
    public int tickCounterHemoLeger;

    public int tickCounterHemoLourd;
    public int tickCounterSynchronize;
    public int tickLightHealing;

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {

            tickCounterHemoLourd++;
            tickCounterHemoLeger++;
            tickCounterSynchronize++;
            tickLightHealing++;
            if (tickCounterHemoLourd >= 20) {
                tickCounterHemoLourd = 0;
                damagePlayersHemoLourd();
            }
            if (tickCounterHemoLeger >= 40) {
                tickCounterHemoLeger = 0;
                damagePlayersHemoLeger();

            }
            if (tickLightHealing >= 1200) {
                tickLightHealing=0;
                healPlayers();
            }
            if (tickCounterSynchronize >= 20) {

                for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                    CapabilityThirst.sync(player);
                }
                tickCounterSynchronize = 0;
            }

            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
                if (player.hasCapability(CapabilityThirst.CAPABILITY_THIRST, null)) {
                    if (!(player.isCreative() || player.isSpectator())) {
                        IThirst getS = ((IThirst) player.getCapability(CapabilityThirst.CAPABILITY_THIRST, null));
                        if (getS.getHydration() > 0) {
                            getS.removeHydration(1);
                        } else {
                            if (getS.getThirst() > 0) {
                                getS.removeThirst(1);
                            } else {
                                player.setHealth(0);
                                getS.setThirst(144000);
                                getS.setHydration(6000);
                            }

                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player != null && event.phase == TickEvent.Phase.START) {
            if (!player.isCreative() || !player.isSpectator()) {
                if (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
                    IHealth getS = ((IHealth) player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, null));
                    if (getS.hasBreak("OBB_RIGHTLEG") && getS.hasBreak("OBB_LEFTLEG")) {
                        player.motionX = 0.0;
                        player.motionY = 0.0;
                        player.motionZ = 0.0;
                    } else if (getS.hasBreak("OBB_RIGHTLEG") || getS.hasBreak("OBB_LEFTLEG")) {

                        player.motionX = Math.min(player.motionX, 0.075);
                        player.motionY = Math.min(player.motionY, 0.075);
                        player.motionZ = Math.min(player.motionZ, 0.075);
                    }
                }
            }

        }
    }


    @SubscribeEvent
    public void onLivingHurt(final LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) event.getEntity();
            player.setHealth(20);
            float amount = event.getAmount();
            float distance = player.fallDistance;
            int BrokeMember = HealthContainer.getDestroyMember(player);
            if  (player.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
                if (event.getSource().isExplosion()) {
                    if (amount >= 20.0F){
                        amount *= 25;
                    }
                    else if (amount >= 17.5F){
                        amount *= 21.875F;
                    }
                    else if (amount >= 15.0F){
                        amount *= 18.750F;
                    }
                    else if (amount >= 12.5F){
                        amount *= 15.625F;
                    }
                    else if (amount >= 10F){
                        amount *= 12.5;
                    }
                    else if (amount >= 7.5F){
                        amount *= 9.375;
                    }
                    else if (amount >= 5F){
                        amount *= 6.250;
                    }
                    else {
                        amount *= 3.125;
                    }
                    damageMost(player, (int)amount);
                }
                if (distance >= 2.0F) {
                    amount = 3;
                    if (distance >= 20.0F) {
                        amount*=64F;
                    }
                    else if (distance >= 17.0F) {
                        amount*=32F;
                    }
                    else if (distance >= 12.0F) {
                        amount *=16F;
                    }
                    else if (distance >= 9.0F) {
                        amount *=8F;
                    }
                    else if (distance >= 6.0F) {
                        amount*=4F;
                    }
                    else  {
                        amount*=2F;
                    }

                    ModularWarfare.PROXY.addBlood(event.getEntityLiving(), 10, true);
                    int DamagePerMembre = ((int)amount/2);
                    if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_LEFTLEG")) {
                        if (player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).getHealth("OBB_LEFTLEG") - DamagePerMembre >= 0) {
                            player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth("OBB_LEFTLEG", player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).getHealth("OBB_LEFTLEG") - DamagePerMembre);
                        } else {
                            player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth("OBB_LEFTLEG", 0);
                            player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBreak("OBB_LEFTLEG", true);

                        }
                    }
                    if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_RIGHTLEG")) {
                        if (player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).getHealth("OBB_RIGHTLEG") - DamagePerMembre >= 0) {
                            player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth("OBB_RIGHTLEG", player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).getHealth("OBB_RIGHTLEG") - DamagePerMembre);
                        } else {
                            player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setHealth("OBB_RIGHTLEG", 0);
                            player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).setBreak("OBB_RIGHTLEG", true);
                        }
                    }

                }
                else {
                    damageMost(player, (int)amount);
                }

                sync((EntityPlayerMP) player);


            }
        }
        final Entity entity = event.getEntity();
        if (entity instanceof EntityItemLoot) {
            return;
        }
    }

    private static final ModularWarfareWorldListener WORLD_LISTENER = new ModularWarfareWorldListener();

    @SubscribeEvent
    public void onInitWorld(WorldEvent.Load event) {
        World world = event.getWorld();
        world.addEventListener(WORLD_LISTENER);
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        World world = event.getWorld();
        world.removeEventListener(WORLD_LISTENER);
    }

    @SubscribeEvent
    public void onEntityInteractBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (ModConfig.INSTANCE.guns.guns_interaction_hand) {
            if (event.getWorld().isRemote) {
                if (Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null) {
                    if (Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemGun) {
                        if (!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockContainer)) {
                            event.setUseBlock(Event.Result.DENY);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void explosionEvent(ExplosionEvent e) {
        Vec3d pos = e.getExplosion().getPosition();
        ModularWarfare.NETWORK.sendToAll(new PacketExplosion(pos.x, pos.y, pos.z));
    }


    @SubscribeEvent
    public void onEntityInteract(final PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof EntityItemLoot) {
            if (!event.getWorld().isRemote && event.getTarget().onGround && !event.getEntityPlayer().isSpectator()) {
                final EntityItemLoot loot = (EntityItemLoot) event.getTarget();
                if (loot.getCustomAge() > 20) {
                    final ItemStack stack = loot.getItem();
                    if (stack.getItem() != Items.AIR && event.getTarget().onGround) {
                        loot.pickup(event.getEntityPlayer());
                    }
                }
            }
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public void onEntityJoin(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        if (ModConfig.INSTANCE.drops.advanced_drops_models) {
            if (event.getEntity().getClass() == EntityItem.class) {
                final EntityItem item = (EntityItem) event.getEntity();
                if (!item.getItem().isEmpty()) {
                    if (item.getItem().getItem() instanceof BaseItem || ModConfig.INSTANCE.drops.advanced_drops_models_everything) {
                        final EntityItemLoot loot = new EntityItemLoot((EntityItem) event.getEntity());
                        event.getEntity().setDead();
                        loot.setInfinitePickupDelay();
                        event.setResult(Event.Result.DENY);
                        event.setCanceled(true);
                        event.getWorld().spawnEntity(loot);
                        return;
                    }
                }
            }
        }
    }

}
