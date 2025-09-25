package net.maxou.blamemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.custom.CubeBossEntity;
import net.maxou.blamemod.entity.layers.ModModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CubeBossRenderer extends MobRenderer<CubeBossEntity, CubeBossModel<CubeBossEntity>> {
    public CubeBossRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CubeBossModel<>(pContext.bakeLayer(ModModelLayers.CUBE_BOSS_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(CubeBossEntity pEntity) {
        return new ResourceLocation(BlameMod.MOD_ID, "textures/entity/cube_boss.png");
    }

    @Override
    public void render(CubeBossEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }

}
