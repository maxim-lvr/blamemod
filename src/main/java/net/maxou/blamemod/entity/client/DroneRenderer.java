package net.maxou.blamemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.custom.DroneEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DroneRenderer extends MobRenderer<DroneEntity, DroneModel<DroneEntity>> {
    public DroneRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DroneModel<>(pContext.bakeLayer(ModModelLayers.DRONE_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(DroneEntity pEntity) {
        return new ResourceLocation(BlameMod.MOD_ID, "textures/entity/drone.png");
    }

    @Override
    public void render(DroneEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }

}
