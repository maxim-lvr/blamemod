package net.maxou.blamemod.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.maxou.blamemod.entity.animations.ModAnimationDefinitions;
import net.maxou.blamemod.entity.custom.DroneEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class DroneModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart drone;
	private final ModelPart body;
	private final ModelPart gun;

	public DroneModel(ModelPart root) {
		this.drone = root.getChild("drone");
		this.body = this.drone.getChild("body");
		this.gun = this.body.getChild("gun");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition drone = partdefinition.addOrReplaceChild("drone", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 19.0F, -3.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition body = drone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gun = body.addOrReplaceChild("gun", CubeListBuilder.create(), PartPose.offset(-3.0F, 2.0F, 3.0F));

		PartDefinition cube_r1 = gun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 22).addBox(-1.0329F, 0.3461F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition cube_r2 = gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(34, 2).addBox(-2.0F, -2.0F, 0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 4.0F, -1.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Aile2 = body.addOrReplaceChild("Aile2", CubeListBuilder.create().texOffs(14, 28).addBox(-5.5F, -1.0F, -0.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -9.0F, 9.0F));

		PartDefinition Aile1 = body.addOrReplaceChild("Aile1", CubeListBuilder.create().texOffs(14, 28).addBox(-5.5F, -1.0F, -0.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -9.0F, -3.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(32, 12).addBox(-3.5F, -11.0F, -3.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 12).addBox(-3.5F, -11.0F, 8.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 20).addBox(-3.0F, -6.0F, 1.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(1.0F, -7.0F, 0.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = torso.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).addBox(-13.0F, -2.0F, -1.0F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -5.0F, 9.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r4 = torso.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(34, 7).addBox(0.5F, -4.0F, -0.7F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 3.0F, -0.0223F, -0.0922F, 0.2451F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyGunRotation(netHeadYaw, headPitch, ageInTicks);

		this.animateWalk(ModAnimationDefinitions.DRONE_WALK, limbSwing, limbSwingAmount,2f,2.5f);
		this.animate(((DroneEntity) entity).idleAnimationState, ModAnimationDefinitions.DRONE_IDLE, ageInTicks, 1f);
	}


	private void applyGunRotation(float pNetGunYaw, float pHeadGunPitch, float pAgeInTicks) {
		pNetGunYaw = Mth.clamp(pNetGunYaw, -30.0F, 30.0F);
		pHeadGunPitch = Mth.clamp(pHeadGunPitch, -25.0F, 45.0F);

		this.gun.yRot = pNetGunYaw * ((float)Math.PI / 180F);
		this.gun.xRot = pHeadGunPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		drone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return drone;
	}
}