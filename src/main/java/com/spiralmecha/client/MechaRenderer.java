package com.spiralmecha.client;

import com.spiralmecha.entity.MechaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MechaRenderer extends GeoEntityRenderer<MechaEntity> {
    public MechaRenderer(EntityRendererProvider.Context context) {
        super(context, new MechaModel());
        this.shadowRadius = 1.4f;
    }
    @Override public void render(MechaEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                                 net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float scale = entity.isGurren() ? 0.617f : 0.755f;
        poseStack.scale(scale, scale, scale);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
