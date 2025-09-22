package net.maxou.blamemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.maxou.blamemod.entity.animations.ModAnimationDefinitions;
import net.maxou.blamemod.entity.custom.ZombieCyborgEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ZombieCyborgModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart zombie_cyborg;
    private final ModelPart head;

    public ZombieCyborgModel(ModelPart root) {
        this.zombie_cyborg = root.getChild("zombie_cyborg");
        this.head = zombie_cyborg.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition zombie_cyborg = partdefinition.addOrReplaceChild("zombie_cyborg", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition torso = zombie_cyborg.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -19.0F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition back = torso.addOrReplaceChild("back", CubeListBuilder.create().texOffs(16, 31).addBox(2.0F, -18.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 12).addBox(-3.0F, -13.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 34).addBox(-3.0F, -18.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 34).addBox(-1.0F, -22.0F, 4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 37).addBox(2.0F, -13.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 12).addBox(-2.0F, -18.0F, 3.0F, 4.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = back.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 30).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(40, 23).addBox(-5.0F, -1.0F, -1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -17.0F, 4.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = back.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(43, 26).addBox(0.0F, -1.0F, 2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -23.0F, 5.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition head = zombie_cyborg.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.0F, 0.0F));

        PartDefinition legs = zombie_cyborg.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 31).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -8.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -8.0F, 0.0F));

        PartDefinition arms = zombie_cyborg.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, 0.0F));

        PartDefinition right_arm = arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 16).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition left_arm = arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 30).addBox(0.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(ModAnimationDefinitions.ZOMBIE_CYBORG_WALK, limbSwing, limbSwingAmount,2f,2.5f);
        this.animate(((ZombieCyborgEntity) entity).attackAnimationState, ModAnimationDefinitions.ZOMBIE_CYBORG_ATTACK, ageInTicks, 1f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        zombie_cyborg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return zombie_cyborg;
    }
}