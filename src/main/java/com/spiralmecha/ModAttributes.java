package com.spiralmecha;

import com.spiralmecha.entity.MechaEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpiralMecha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModAttributes {
    @SubscribeEvent public static void attributes(EntityAttributeCreationEvent event) {
        event.put(com.spiralmecha.registry.ModEntities.MECHA.get(),
            MechaEntity.createAttributes());
    }
}
