package com.modularwarfare.common.Thirst;

import com.modularwarfare.common.heal.EnumBodyPart;
import com.modularwarfare.common.heal.IHealth;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ThirstStorage implements Capability.IStorage<IThirst>
{

    public NBTBase writeNBT(Capability<IThirst> capability, IThirst instance, EnumFacing side)
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("thirst", instance.getThirst());
        compound.setFloat("hydration", instance.getHydration());
        compound.setFloat("exhaustion", instance.getExhaustion());
        return compound;

    }
    public void readNBT(Capability<IThirst> capability, IThirst instance, EnumFacing side, NBTBase nbt)
    {
        if(nbt instanceof NBTTagCompound)
        {

            NBTTagCompound tag = (NBTTagCompound)nbt;
            float thirst = tag.getFloat("thirst");
            float hydration = tag.getFloat("hydration");
            float exhaustion = tag.getFloat("exhaustion");
            instance.setThirst(thirst);
            instance.setExhaustion(exhaustion);
            instance.setHydration(hydration);

        }
    }
}
