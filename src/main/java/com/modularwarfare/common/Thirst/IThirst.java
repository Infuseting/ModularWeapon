package com.modularwarfare.common.Thirst;

public interface IThirst {
    public void setThirst(float thirst);
    public void setExhaustion(float exhaustion);
    public void setHydration(float hydration);
    public void addStats(float thirst, float hydration);
    public void removeThirst(float thirst);
    public void removeHydration(float hydration);

    public float getThirst();
    public float getExhaustion();
    public float getHydration();
}
