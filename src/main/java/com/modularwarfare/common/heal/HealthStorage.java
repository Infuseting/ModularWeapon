package com.modularwarfare.common.heal;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class HealthStorage implements Capability.IStorage<IHealth>
{

    public NBTBase writeNBT(Capability<IHealth> capability, IHealth instance, EnumFacing side)
    {
        NBTTagCompound compound = new NBTTagCompound();
        for (EnumBodyPart bodyPart : EnumBodyPart.values()) {
            compound.setFloat(bodyPart.name() +  "_health",instance.getHealth(bodyPart.name()));
            compound.setBoolean(bodyPart.name() +  "_lightBleed",instance.hasLightBleed(bodyPart.name() ));
            compound.setBoolean(bodyPart.name() +  "_hardBleed",instance.hasHardBleed(bodyPart.name() ));
            compound.setBoolean(bodyPart.name() +  "_break",instance.hasBreak(bodyPart.name() ));
            compound.setBoolean(bodyPart.name() +  "_bulletIn",instance.hasBulletIn(bodyPart.name()));



        }
        return compound;

    }
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