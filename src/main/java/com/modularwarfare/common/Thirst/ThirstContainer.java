package com.modularwarfare.common.Thirst;

import net.minecraft.entity.player.EntityPlayer;

public class ThirstContainer implements IThirst {
    public EntityPlayer player;
    public float thirst;
    public float exhaustion;
    public float hydration;
    public ThirstContainer(final EntityPlayer player) {
        this.player = player;
        this.thirst = 144000F;
        this.exhaustion = 0;
        this.hydration = 6000;
    }

    public ThirstContainer() {

    }
    @Override
    public void setThirst(float thirst) {
        // Ensure Thirst is within the valid range [0, 144000]
        this.thirst = Math.min(Math.max(thirst, 0F), 144000F);
    }

    @Override
    public void setExhaustion(float exhaustion) {
        // Ensure exhaustion is non-negative
        this.exhaustion = Math.max(exhaustion, 0);
    }

    @Override
    public void setHydration(float hydration) {
        // Ensure hydration is within the valid range [0, 6000]
        this.hydration = Math.min(Math.max(hydration, 0), 6000);
    }

    @Override
    public void addStats(float thirst, float hydration) {
        // Update Thirst and hydration by adding the provided values
        this.thirst = Math.min(Math.max(this.thirst + thirst, 0F), 144000);
        this.hydration = Math.min(Math.max(this.hydration + hydration, 0), 6000);
    }

    @Override
    public void removeThirst(float thirst) {
        // Ensure Thirst doesn't go below 0
        this.thirst = Math.max(this.thirst - thirst, 0F);
    }

    @Override
    public void removeHydration(float hydration) {
        // Ensure hydration doesn't go below 0
        this.hydration = Math.max(this.hydration - hydration, 0F);
    }

    @Override
    public float getThirst() {
        return this.thirst;
    }

    @Override
    public float getExhaustion() {
        return this.exhaustion;
    }

    @Override
    public float getHydration() {
        return this.hydration;
    }



}
