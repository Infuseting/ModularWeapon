package com.modularwarfare.common.Thirst;

import com.modularwarfare.common.heal.HealthContainer;
import com.modularwarfare.common.heal.HealthStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import static com.modularwarfare.common.Thirst.CapabilityThirst.CAPABILITY_THIRST;
import static com.modularwarfare.common.heal.CapabilityHealth.CAPABILITY_HEALTH;

public class ThirstContainerProvider implements ICapabilitySerializable<NBTBase>
{

    private ThirstContainer container ;
    public ThirstContainerProvider(final ThirstContainer container) {
        this.container = container;
    }
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CAPABILITY_THIRST;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == CAPABILITY_THIRST ? CAPABILITY_THIRST.<T> cast(this.container) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return  ((ThirstStorage)CAPABILITY_THIRST.getStorage()).writeNBT(CAPABILITY_THIRST, this.container, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        ((ThirstStorage)CAPABILITY_THIRST.getStorage()).readNBT(CAPABILITY_THIRST, this.container, null, nbt);
    }
}