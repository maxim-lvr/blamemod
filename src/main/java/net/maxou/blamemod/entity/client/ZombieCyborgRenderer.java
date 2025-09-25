package net.maxou.blamemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.maxou.blamemod.BlameMod;
import net.maxou.blamemod.entity.custom.ZombieCyborgEntity;
import net.maxou.blamemod.entity.layers.ModModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieCyborgRenderer extends MobRenderer<ZombieCyborgEntity, ZombieCyborgModel<ZombieCyborgEntity>> {
    public ZombieCyborgRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ZombieCyborgModel<>(pContext.bakeLayer(ModModelLayers.ZOMBIE_CYBORG_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieCyborgEntity pEntity) {
        return new ResourceLocation(BlameMod.MOD_ID, "textures/entity/zombie_cyborg.png");
    }

    @Override
    public void render(ZombieCyborgEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);

    }

}
