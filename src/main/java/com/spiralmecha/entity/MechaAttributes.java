package com.spiralmecha.entity;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class MechaAttributes {
    private MechaAttributes() {}
    public static AttributeSupplier.Builder create() {
        return net.minecraft.world.entity.Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 200.0)
            .add(Attributes.MOVEMENT_SPEED, 0.26)
            .add(Attributes.ATTACK_DAMAGE, 10.0)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.65);
    }
}
