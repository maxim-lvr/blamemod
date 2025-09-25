package net.maxou.blamemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class CubeBossModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart cube_boss;
	private final ModelPart direction_arrow;

	public CubeBossModel(ModelPart root) {
		this.cube_boss = root.getChild("cube_boss");
		this.direction_arrow = cube_boss.getChild("direction_arrow");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition cube_boss = partdefinition.addOrReplaceChild("cube_boss", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition body = cube_boss.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -32.0F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition eye1 = body.addOrReplaceChild("eye1", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -17.0F));

		PartDefinition eye1_1_r1 = eye1.addOrReplaceChild("eye1_1_r1", CubeListBuilder.create().texOffs(0, 64).addBox(-7.0F, -7.0F, -1.0F, 14.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition arrows = body.addOrReplaceChild("arrows", CubeListBuilder.create().texOffs(52, 64).addBox(-17.0F, -6.0F, -2.0F, 1.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(62, 64).addBox(16.0F, -6.0F, -2.0F, 1.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 0.0F));

		PartDefinition head_arrow2_r1 = arrows.addOrReplaceChild("head_arrow2_r1", CubeListBuilder.create().texOffs(8, 79).addBox(0.0F, 0.0F, -1.0F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 79).addBox(33.0F, 0.0F, -1.0F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, -8.0F, -0.5F, 0.7854F, 0.0F, 0.0F));

		PartDefinition head_arrow_r1 = arrows.addOrReplaceChild("head_arrow_r1", CubeListBuilder.create().texOffs(30, 75).addBox(0.0F, -2.0F, -1.0F, 1.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 75).addBox(33.0F, -2.0F, -1.0F, 1.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, -8.0F, -0.5F, -0.7854F, 0.0F, 0.0F));

		PartDefinition eye2 = body.addOrReplaceChild("eye2", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -17.0F));

		PartDefinition eye2_2_r1 = eye2.addOrReplaceChild("eye2_2_r1", CubeListBuilder.create().texOffs(30, 64).addBox(-5.0F, -5.0F, -1.0F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

		PartDefinition eye3 = body.addOrReplaceChild("eye3", CubeListBuilder.create().texOffs(54, 79).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, -19.0F));

		PartDefinition direction_arrow = cube_boss.addOrReplaceChild("direction_arrow", CubeListBuilder.create().texOffs(72, 64).addBox(-0.5F, -1.7964F, -2.2559F, 1.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.7461F, -3.2036F, -21.5947F, -3.1416F, -1.5272F, -3.1416F));

		PartDefinition head_arrow2_r2 = direction_arrow.addOrReplaceChild("head_arrow2_r2", CubeListBuilder.create().texOffs(46, 79).addBox(-0.5F, -3.9017F, 1.6517F, 1.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.8375F, 0.128F, 0.7854F, 0.0F, 0.0F));

		PartDefinition head_arrow_r2 = direction_arrow.addOrReplaceChild("head_arrow_r2", CubeListBuilder.create().texOffs(0, 79).addBox(-0.5F, -4.6517F, -4.9017F, 1.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.8375F, 0.128F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		cube_boss.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return cube_boss;
	}
}