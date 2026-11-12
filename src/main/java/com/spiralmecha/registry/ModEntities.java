package com.spiralmecha.registry;

import com.spiralmecha.SpiralMecha;
import com.spiralmecha.entity.MechaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SpiralMecha.MOD_ID);
    public static final RegistryObject<EntityType<MechaEntity>> MECHA = ENTITY_TYPES.register("mecha",
        () -> EntityType.Builder.<MechaEntity>of(MechaEntity::new, MobCategory.MISC)
            .sized(3.4f, 2.5f).clientTrackingRange(16).updateInterval(1).build("mecha"));
}
