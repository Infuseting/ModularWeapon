package com.modularwarfare.common.heal;


import com.modularwarfare.raycast.obb.OBBModelBox;

public interface IHealth {
    float getHealth(String bodyPart);
    boolean hasLightBleed(String bodyPart);
    boolean hasHardBleed(String bodyPart);
    boolean hasBreak(String bodyPart);
    boolean hasBulletIn(String bodyPart);
    void setHealth(String bodyPart, float health);
    void setLightBleed(String bodyPart, boolean Situation);
    void setHardBleed(String bodyPart, boolean Situation);
    void setBreak(String bodyPart, boolean Situation);
    void setBulletIn(String bodyPart, boolean Situation);

}