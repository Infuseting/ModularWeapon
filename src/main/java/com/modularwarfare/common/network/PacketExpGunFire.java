package com.modularwarfare.common.network;

import com.modularwarfare.ModConfig;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.armor.ArmorType;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.guns.*;
import com.modularwarfare.common.guns.manager.ShotValidation;
import com.modularwarfare.common.heal.CapabilityHealth;
import com.modularwarfare.common.heal.IHealth;
import com.modularwarfare.utility.ModularDamageSource;
import com.modularwarfare.utility.RayUtil;
import gnu.trove.impl.sync.TSynchronizedShortByteMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Random;

public class PacketExpGunFire extends PacketBase {

    public int entityId;

    public String internalname;
    public String hitboxType;

    public int fireTickDelay;

    public float recoilPitch;
    public float recoilYaw;

    public float recoilAimReducer;

    public float bulletSpread;

    private double posX;
    private double posY;
    private double posZ;
    private EnumFacing facing;

    public PacketExpGunFire() {
    }

    public PacketExpGunFire(int entityId, String internalname, String hitboxType, int fireTickDelay, float recoilPitch, float recoilYaw, float recoilAimReducer, float bulletSpread, double x, double y, double z) {
        this(entityId, internalname, hitboxType, fireTickDelay, recoilPitch, recoilYaw, recoilAimReducer, bulletSpread, x, y, z, null);
    }

    public PacketExpGunFire(int entityId, String internalname, String hitboxType, int fireTickDelay, float recoilPitch, float recoilYaw, float recoilAimReducer, float bulletSpread, double x, double y, double z,EnumFacing facing) {
        this.entityId = entityId;
        this.internalname = internalname;
        this.hitboxType = hitboxType;

        this.fireTickDelay = fireTickDelay;
        this.recoilPitch = recoilPitch;
        this.recoilYaw = recoilYaw;
        this.recoilAimReducer = recoilAimReducer;
        this.bulletSpread = bulletSpread;

        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.facing=facing;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(this.entityId);
        ByteBufUtils.writeUTF8String(data, this.internalname);
        ByteBufUtils.writeUTF8String(data, this.hitboxType);

        data.writeInt(this.fireTickDelay);
        data.writeFloat(this.recoilPitch);
        data.writeFloat(this.recoilYaw);
        data.writeFloat(this.recoilAimReducer);
        data.writeFloat(this.bulletSpread);

        data.writeDouble(this.posX);
        data.writeDouble(this.posY);
        data.writeDouble(this.posZ);
        if(this.facing==null) {
            data.writeInt(-1);
        }else {
            data.writeInt(this.facing.ordinal());
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.entityId = data.readInt();
        this.internalname = ByteBufUtils.readUTF8String(data);
        this.hitboxType = ByteBufUtils.readUTF8String(data);

        this.fireTickDelay = data.readInt();
        this.recoilPitch = data.readFloat();
        this.recoilYaw = data.readFloat();
        this.recoilAimReducer = data.readFloat();
        this.bulletSpread = data.readFloat();

        this.posX = data.readDouble();
        this.posY = data.readDouble();
        this.posZ = data.readDouble();
        int enumFacing=data.readInt();
        if(enumFacing!=-1) {
            this.facing=EnumFacing.values()[enumFacing];
        }
    }

    @Override
    /*
     * need to support hit/fire event
     * */
    public void handleServerSide(EntityPlayerMP entityPlayer) {
        IThreadListener mainThread = (WorldServer) entityPlayer.world;
        mainThread.addScheduledTask(new Runnable() {
            public void run() {
                if (entityPlayer != null) {
                    if (entityPlayer.getHeldItemMainhand() != null) {
                        if (entityPlayer.getHeldItemMainhand().getItem() instanceof ItemGun) {
                            if (ModularWarfare.gunTypes.get(internalname) != null) {
                                ItemGun itemGun = ModularWarfare.gunTypes.get(internalname);
                                
                                if(entityPlayer.getHeldItemMainhand().getItem()!=itemGun) {
                                    return;
                                }

                                if (entityPlayer.ping > ModConfig.INSTANCE.shots.maxShooterPing) {
                                    entityPlayer.sendMessage(new TextComponentString(TextFormatting.GRAY + "[" + TextFormatting.RED + "ModularWarfare" + TextFormatting.GRAY + "] Your ping is too high, shot not registered."));
                                    return;
                                }

                                if (entityId != -1) {


                                    Entity target = entityPlayer.world.getEntityByID(entityId);
                                    WeaponFireMode fireMode = GunType.getFireMode(entityPlayer.getHeldItemMainhand());
                                    if (fireMode == null)
                                        return;
                                    IExtraItemHandler extraSlots = null;
                                    ItemStack plate = null;

                                    boolean damageGood = false;
                                    if (ShotValidation.verifShot(entityPlayer, entityPlayer.getHeldItemMainhand(), itemGun, fireMode, fireTickDelay, recoilPitch, recoilYaw, recoilAimReducer, bulletSpread)) {
                                        if (target != null) {
                                            ItemBullet bulletItem = ItemGun.getUsedBullet(entityPlayer.getHeldItemMainhand(), itemGun.type);
                                            float damage = bulletItem.type.DamagePerBellet;
                                            if (target instanceof EntityPlayer && hitboxType != null) {

                                                EntityPlayer player = (EntityPlayer) target;
                                                if (player.hasCapability(CapabilityExtra.CAPABILITY_ITEM, null)) {
                                                    if (hitboxType.contains("obb_body")) {

                                                        extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
                                                        plate = extraSlots.getStackInSlot(2);
                                                        if (plate != null) {
                                                            if (plate.getItem() instanceof ItemSpecialArmor) {
                                                                ArmorType armorType = ((ItemSpecialArmor) plate.getItem()).type;
                                                                damageGood = true;
                                                            }
                                                            else {
                                                                damageGood = true;
                                                            }
                                                        }
                                                        else {
                                                            damageGood = true;

                                                        }
                                                    }
                                                    if (hitboxType.contains("obb_head")) {
                                                        extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
                                                        plate = extraSlots.getStackInSlot(3);
                                                        if (plate != null) {
                                                            double damageItem = (plate.getMaxDamage()-plate.getItemDamage())*0.15;
                                                            if (damageItem <= 1) {damageItem = 1;}
                                                            plate.attemptDamageItem( (int) damageItem,entityPlayer.getRNG(), entityPlayer);
                                                            //entityPlayer.sendMessage(new TextComponentString(plate.getItemDamage()+"/"+plate.getMaxDamage()));
                                                            if (true) {
                                                                if (plate.getItemDamage() >= plate.getMaxDamage()) {
                                                                    extraSlots.setStackInSlot(2, ItemStack.EMPTY);
                                                                } else {
                                                                    extraSlots.setStackInSlot(2, plate);
                                                                }
                                                            }
                                                            if (plate.getItem() instanceof ItemSpecialArmor) {
                                                                if (plate.getItemDamage() <= 35) {
                                                                    if ((int) ((Math.random()*100)) >= ((ArmorType)((ItemSpecialArmor) plate.getItem()).type).ricochet) {

                                                                        damageGood = true;
                                                                    }
                                                                }
                                                                else {
                                                                    damageGood = true;
                                                                }

                                                            }
                                                            else {
                                                                damageGood = true;
                                                            }
                                                        }

                                                        else {
                                                            damageGood = true;
                                                        }


                                                    }

                                                    if (hitboxType.contains("obb_leftLeg")) {
                                                        extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
                                                        damageGood = true;

                                                    }
                                                    if (hitboxType.contains("obb_rightLeg")) {
                                                        extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
                                                        damageGood = true;

                                                    }
                                                    if (hitboxType.contains("obb_leftArm")) {
                                                        extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
                                                        damageGood = true;


                                                    }
                                                    if (hitboxType.contains("obb_rightArm")) {
                                                        extraSlots = player.getCapability(CapabilityExtra.CAPABILITY_ITEM, null);
                                                        damageGood = true;

                                                    }
                                                }
                                            }
                                            
                                            //BULLET START
                                            

                                            
                                            if (target instanceof EntityLivingBase) {
                                                EntityLivingBase targetELB = (EntityLivingBase) target;
                                                if (bulletItem != null) {
                                                    if (bulletItem.type != null) {

                                                        if (bulletItem.type.bulletProperties != null) {
                                                            if (!bulletItem.type.bulletProperties.isEmpty()) {
                                                                BulletProperty bulletProperty = bulletItem.type.bulletProperties.get(targetELB.getName()) != null ? bulletItem.type.bulletProperties.get(targetELB.getName()) : bulletItem.type.bulletProperties.get("All");
                                                                if (bulletProperty.potionEffects != null) {
                                                                    for (PotionEntry potionEntry : bulletProperty.potionEffects) {
                                                                        targetELB.addPotionEffect(new PotionEffect(potionEntry.potionEffect.getPotion(), potionEntry.duration, potionEntry.level));

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            if (target instanceof EntityLivingBase && !(target instanceof EntityPlayer)) {

                                                damageGood = true;
                                            }

                                            //BULLET END
                                            boolean flag=false;

                                            DamageSource damageSource=DamageSource.causePlayerDamage(entityPlayer).setProjectile();


                                            if(bulletItem.type.isFireDamage) {
                                                damageSource.setFireDamage();
                                            }
                                            if(bulletItem.type.isAbsoluteDamage) {
                                                damageSource.setDamageIsAbsolute();
                                            }
                                            if(bulletItem.type.isBypassesArmorDamage) {
                                                damageSource.setDamageBypassesArmor();
                                            }
                                            if(bulletItem.type.isExplosionDamage) {
                                                damageSource.setExplosion();
                                            }
                                            if(bulletItem.type.isMagicDamage) {
                                                damageSource.setMagicDamage();
                                            }

                                            if (damageGood) {
                                                System.out.println(bulletItem.type.DamagePerBellet);
                                                if (target instanceof EntityPlayer) {
                                                    if (!ModConfig.INSTANCE.shots.knockback_entity_damage) {
                                                        flag=RayUtil.attackEntityWithoutKnockback(target, damageSource, 1);
                                                        ((EntityPlayer) target).setHealth(20);
                                                    } else {
                                                        flag=target.attackEntityFrom(damageSource, 1);
                                                        ((EntityPlayer) target).setHealth(20);
                                                    }
                                                }
                                                else{
                                                    if (!ModConfig.INSTANCE.shots.knockback_entity_damage) {
                                                        flag=RayUtil.attackEntityWithoutKnockback(target, damageSource, bulletItem.type.DamagePerBellet);
                                                    } else {
                                                        flag = target.attackEntityFrom(damageSource, bulletItem.type.DamagePerBellet);
                                                    }
                                                }
                                                if (target.hasCapability(CapabilityExtra.CAPABILITY_ITEM, null)) {
                                                    if (target.hasCapability(CapabilityHealth.CAPABILITY_HEALTH, null)) {
                                                        IHealth getS = (((IHealth) target.getCapability((Capability) CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null)));
                                                        if (hitboxType.contains("obb_head")) {

                                                            if (target.getCapability(CapabilityExtra.CAPABILITY_ITEM, null).getStackInSlot(3).isEmpty()) {

                                                                flag=RayUtil.attackEntityWithoutKnockback(target, damageSource, 1000);

                                                            }
                                                            else if ((target.getCapability(CapabilityExtra.CAPABILITY_ITEM, null).getStackInSlot(3) != null)) {
                                                                if (((ItemSpecialArmor)target.getCapability(CapabilityExtra.CAPABILITY_ITEM, null).getStackInSlot(3).getItem()).type.defense >= 0) {

                                                                    flag=RayUtil.attackEntityWithoutKnockback(target, damageSource, 1000);
                                                                }
                                                            }
                                                        }

                                                        if (bulletItem.type.HemoragieLeger >= ((int) (Math.random()*100))) {

                                                            if (bulletItem.type.HemoragieLourd >= ((int) (Math.random()*100))) {

                                                                getS.setHardBleed(hitboxType.toUpperCase(), true);
                                                            }
                                                            else {
                                                                getS.setLightBleed(hitboxType.toUpperCase(), true);
                                                            }
                                                        }
                                                        if (bulletItem.type.BulletIn >= ((int) (Math.random()*100))) {

                                                            getS.setBulletIn(hitboxType.toUpperCase(), true);
                                                        }
                                                        if (getS.getHealth(hitboxType.toUpperCase().toString()) - bulletItem.type.DamagePerBellet >= 0) {

                                                            getS.setHealth(hitboxType.toUpperCase(), getS.getHealth(hitboxType.toUpperCase()) - bulletItem.type.DamagePerBellet);
                                                        } else {
                                                            if (hitboxType.contains("obb_body") || hitboxType.contains("obb_head")) {
                                                                flag=RayUtil.attackEntityWithoutKnockback(target, damageSource, 1000);
                                                            } else {
                                                                getS.setHealth(hitboxType.toUpperCase(), 0);

                                                                getS.setBreak(hitboxType.toUpperCase(), true);
                                                            }
                                                        }
                                                        if (getS.getHealth("OBB_HEAD")+getS.getHealth("OBB_BODY")+getS.getHealth("OBB_LEFTARM")+getS.getHealth("OBB_RIGHTARM")+getS.getHealth("OBB_LEFTLEG")+getS.getHealth("OBB_RIGHTLEG") <= 0) {
                                                            flag=RayUtil.attackEntityWithoutKnockback(target, damageSource, 1000);
                                                            getS.setHealth("OBB_HEAD", 35);
                                                        }
                                                    }
                                                }
                                            }

                                            target.hurtResistantTime = 0;

                                            
                                            if (entityPlayer instanceof EntityPlayerMP) {
                                                ModularWarfare.NETWORK.sendTo(new PacketPlaySound(target.getPosition(), "flyby", 1f, 1f), (EntityPlayerMP) target);
                                                CapabilityHealth.sync((EntityPlayerMP) target);
                                                if (ModConfig.INSTANCE.hud.snap_fade_hit) {
                                                    ModularWarfare.NETWORK.sendTo(new PacketPlayerHit(), (EntityPlayerMP) target);
                                                }
                                            }

                                        }
                                    }
                                } else {
                                    BlockPos blockPos = new BlockPos(posX, posY, posZ);
                                    ItemGun.playImpactSound(entityPlayer.world, blockPos, itemGun.type);
                                    itemGun.type.playSoundPos(blockPos, entityPlayer.world, WeaponSoundType.Crack, entityPlayer, 1.0f);
                                    ItemGun.doHit(posX, posY, posZ,facing, entityPlayer);
                                }

                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void handleClientSide(EntityPlayer entityPlayer) {

    }

}