package com.modularwarfare.common.heal;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.ExtraContainer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.modularwarfare.common.heal.CapabilityHealth.CAPABILITY_HEALTH;

public class HealthContainerProvider implements ICapabilitySerializable<NBTBase>
{

    private HealthContainer container ;
    public HealthContainerProvider(final HealthContainer container) {
        this.container = container;
    }
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CAPABILITY_HEALTH;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == CAPABILITY_HEALTH ? CAPABILITY_HEALTH.<T> cast(this.container) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return  ((HealthStorage)CAPABILITY_HEALTH.getStorage()).writeNBT(CAPABILITY_HEALTH, this.container, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        ((HealthStorage)CAPABILITY_HEALTH.getStorage()).readNBT(CAPABILITY_HEALTH, this.container, null, nbt);
    }
}