package com.modularwarfare.common.heal;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.network.PacketSyncExtraSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.Map;

public class HealthContainer implements IHealth {
    private EntityPlayer player;
    private Map<String, Float> healthMap = new HashMap<>();
    private Map<String, Boolean> lightBleedMap = new HashMap<>();
    private Map<String, Boolean> hardBleedMap = new HashMap<>();
    private Map<String, Boolean> BreakMap = new HashMap<>();
    private Map<String, Boolean> BulletInMap = new HashMap<>();
    // ... autres maps pour les autres propriétés
    public HealthContainer(final EntityPlayer player) {
        this.player = player;
        setHealth("obb_head", 35);
        setHealth("obb_body", 85);
        setHealth("obb_leftarm", 60);
        setHealth("obb_rightarm", 60);
        setHealth("obb_leftleg", 65);
        setHealth("obb_rightleg", 65);
    }

    public HealthContainer() {

    }

    @Override
    public float getHealth(String bodyPart) {
        Float value = 85F;
        switch (bodyPart) {
            case "OBB_HEAD": value = 35F; break;
            case "OBB_BODY": value = 85F; break;
            case "OBB_LEFTARM": value = 60F; break;
            case "OBB_RIGHTARM": value = 60F; break;
            case "OBB_LEFTLEG": value = 65F; break;
            case "OBB_RIGHTLEG": value = 65F; break;
        }
        return healthMap.getOrDefault(bodyPart, value);
    }

    @Override
    public boolean hasLightBleed(String bodyPart) {
        return lightBleedMap.getOrDefault(bodyPart, false);
    }
    @Override
    public boolean hasHardBleed(String bodyPart) {
        return hardBleedMap.getOrDefault(bodyPart, false);
    }
    @Override
    public boolean hasBreak(String bodyPart) {
        return BreakMap.getOrDefault(bodyPart, false);
    }
    @Override
    public boolean hasBulletIn(String bodyPart) {
        return BulletInMap.getOrDefault(bodyPart, false);
    }

    @Override
    public void setHealth(String bodyPart, float health) {
        healthMap.put(bodyPart, health);
        if (health == 0) {
            setBreak(bodyPart, true);
        }
    }
    @Override
    public void setLightBleed(String bodyPart,  boolean Situation) {
        lightBleedMap.put(bodyPart, Situation);
    }
    @Override
    public void setHardBleed(String bodyPart,  boolean Situation) {
        hardBleedMap.put(bodyPart, Situation);
    }
    @Override
    public void setBreak(String bodyPart,  boolean Situation) {
        BreakMap.put(bodyPart, Situation);
    }
    @Override
    public void setBulletIn(String bodyPart,  boolean Situation) {
        BulletInMap.put(bodyPart, Situation);
    }

    public static int getDestroyMember(EntityPlayer player) {
        int value = 0;
        if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_HEAD")) {
            value+=1;
        }
        if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_BODY")) {
            value+=1;
        }
        if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_LEFTARM")) {
            value+=1;
        }
        if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_RIGHTARM")) {
            value+=1;
        }
        if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_LEFTLEG")) {
            value+=1;
        }
        if (!player.getCapability(CapabilityHealth.CAPABILITY_HEALTH, (EnumFacing) null).hasBreak("OBB_RIGHTLEG")) {
            value+=1;
        }
        return value;
    }



}
