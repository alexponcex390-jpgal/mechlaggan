package com.spiralmecha.client;

import com.spiralmecha.SpiralMecha;
import com.spiralmecha.entity.MechaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MechaModel extends GeoModel<MechaEntity> {
    @Override public ResourceLocation getModelResource(MechaEntity entity) {
        return SpiralMecha.id("geo/" + (entity.isGurren() ? "gurren_lagann" : "laggan") + ".geo.json");
    }
    @Override public ResourceLocation getTextureResource(MechaEntity entity) {
        return SpiralMecha.id("textures/entity/" + (entity.isGurren() ? "gurren_lagann" : "laggan") + ".png");
    }
    @Override public ResourceLocation getAnimationResource(MechaEntity entity) {
        return SpiralMecha.id("animations/" + (entity.isGurren() ? "gurren_lagann" : "laggan") + ".animation.json");
    }
}
