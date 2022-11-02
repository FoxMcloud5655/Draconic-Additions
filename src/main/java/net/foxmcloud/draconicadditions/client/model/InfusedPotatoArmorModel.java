package net.foxmcloud.draconicadditions.client.model;

import java.util.Map;

import com.brandon3055.draconicevolution.client.model.VBOBipedModel;
import com.mojang.blaze3d.vertex.PoseStack;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.model.OBJParser;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;

public class InfusedPotatoArmorModel extends VBOBipedModel<LivingEntity> {

	public InfusedPotatoArmorModel(float size, EquipmentSlot slot) {
		super(new ModelPart(null, null));
		Map<String, CCModel> headModel      = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_helmet.obj")).parse();
		Map<String, CCModel> bodyModel      = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_body.obj")).parse();
		Map<String, CCModel> rightArmModel  = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_right_arm.obj")).parse();
		Map<String, CCModel> leftArmModel   = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_left_arm.obj")).parse();
		Map<String, CCModel> beltModel      = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_belt.obj")).parse();
		Map<String, CCModel> rightLegModel  = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_right_leg.obj")).parse();
		Map<String, CCModel> leftLegModel   = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_left_leg.obj")).parse();
		Map<String, CCModel> rightBootModel = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_right_boot.obj")).parse();
		Map<String, CCModel> leftBootModel  = new OBJParser(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_left_boot.obj")).parse();

		body.y = 0.755F;
		rightArm.y = 0.755F;
		leftArm.y = 0.755F;

		head.y = 0F;
		body.y = 0.755F;
		body.z = -0.03F;
		rightArm.y = 0.72F;
		rightArm.x = -0.21F;
		rightArm.z = 0.F;
		leftArm.y = 0.72F;
		leftArm.x = 0.21F;
		leftArm.z = 0F;
		rightLeg.y = 0.6F;
		rightLeg.x = -0.085F;
		leftLeg.y = 0.6F;
		leftLeg.x = 0.085F;
		//rightBoot.offsetY = 0.76F;
		//rightBoot.offsetX = -0.03F;
		//leftBoot.offsetY = 0.76F;
		//leftBoot.offsetX = 0.03F;

		//leftLeg.scale = 1F / 14F;
		//rightLeg.scale = 1F / 14F;
		//leftBoot.scale = 1F / 14F;
		//rightBoot.scale = 1F / 14F;

		//leftArm.scale = 1F / 13.7F;
		//rightArm.scale = 1F / 13.7F;

		/* TODO: Implement once DE finishes this.
		if (slot == EquipmentSlot.HEAD) {
			this.bipedHead.addChild(head);
		}
		if (slot == EquipmentSlot.CHEST) {
			this.bipedBody.addChild(body);
			this.bipedLeftArm.addChild(leftArm);
			this.bipedRightArm.addChild(rightArm);
		}
		if (slot == EquipmentSlot.LEGS) {
			this.bipedLeftLeg.addChild(leftLeg);
			this.bipedRightLeg.addChild(rightLeg);
			//this.bipedBody.addChild(belt);
		}
		if (slot == EquipmentSlot.FEET) {
			//this.bipedLeftLeg.addChild(leftBoot);
			//this.bipedRightLeg.addChild(rightBoot);
		}
		*/
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(PoseStack pose, MultiBufferSource source, LivingEntity entity, ItemStack itemstack, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (entity == null || entity instanceof ArmorStand) {
			crouching = false;
			riding = false;
			young = false;

			/*
			this.bipedRightArm.xRot = 0F;
			this.bipedRightArm.yRot = 0F;
			this.bipedRightArm.zRot = 0F;
			this.bipedLeftArm.xRot = 0F;
			this.bipedLeftArm.yRot = 0F;
			this.bipedLeftArm.zRot = 0F;

			bipedBody.xRot = 0F;
			bipedBody.yRot = 0F;
			bipedBody.zRot = 0F;

			bipedHead.xRot = 0F;
			bipedHead.yRot = 0F;
			bipedHead.zRot = 0F;

			bipedLeftLeg.xRot = 0F;
			bipedLeftLeg.yRot = 0F;
			bipedLeftLeg.zRot = 0F;

			bipedRightLeg.xRot = 0F;
			bipedRightLeg.yRot = 0F;
			bipedRightLeg.zRot = 0F;

			setRotationAngles(0, 0, 0, 0, 0, 0, null);
			*/
		}
		/*
		RenderSystem.pushMatrix();

		if (entity.isCrouching()) {
			RenderSystem.translatef(0.0F, 0.2F, 0.0F);
		}

		this.bipedHead.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedRightArm.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedLeftArm.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedBody.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedRightLeg.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedLeftLeg.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		RenderSystem.popMatrix();
		*/
	}

	public void setRotationAngles(float a, float b, float c, float d, float e, float f, Entity entity) {
		/*
		this.bipedRightArm.zRot = 0.0F;
		this.bipedLeftArm.zRot = 0.0F;
		this.bipedRightArm.zRot = 0.0F;
		this.bipedLeftArm.zRot = 0.0F;
		this.bipedRightLeg.yRot = 0.0F;
		this.bipedLeftLeg.yRot = 0.0F;
		this.bipedRightArm.yRot = 0.0F;
		this.bipedLeftArm.yRot = 0.0F;
		this.bipedBody.xRot = 0.0F;
		this.bipedRightLeg.zRot = 0.1F;
		this.bipedLeftLeg.zRot = 0.1F;
		this.bipedRightLeg.yRot = 12.0F;
		this.bipedLeftLeg.yRot = 12.0F;
		this.bipedHead.yRot = 0.0F;
		this.hat.yRot = 0.0F;
		this.leftLeg.zRot = 0F;
		this.rightLeg.zRot = 0F;
		this.bipedRightArm.zRot = 0.0F;
		this.bipedLeftArm.zRot = 0.0F;
		*/
	}
}
