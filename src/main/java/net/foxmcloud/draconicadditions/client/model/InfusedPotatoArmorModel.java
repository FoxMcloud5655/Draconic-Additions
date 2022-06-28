package net.foxmcloud.draconicadditions.client.model;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.brandon3055.draconicevolution.client.model.VBOBipedModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.OBJParser;
import codechicken.lib.render.buffer.VBORenderType;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class InfusedPotatoArmorModel extends VBOBipedModel<LivingEntity> {

	public InfusedPotatoArmorModel(float size, EquipmentSlotType slot) {
		super(size);
        Map<String, CCModel> headModel      = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_helmet.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> bodyModel      = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_body.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> rightArmModel  = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_right_arm.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> leftArmModel   = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_left_arm.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> beltModel      = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_belt.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> rightLegModel  = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_right_leg.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> leftLegModel   = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_left_leg.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> rightBootModel = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_right_boot.obj"), GL11.GL_TRIANGLES, null);
        Map<String, CCModel> leftBootModel  = OBJParser.parseModels(new ResourceLocation(DraconicAdditions.MODID, "models/armor/potato_left_boot.obj"), GL11.GL_TRIANGLES, null);

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
		
		if (slot == EquipmentSlotType.HEAD) {
			this.bipedHead.addChild(head);
		}
		if (slot == EquipmentSlotType.CHEST) {
			this.bipedBody.addChild(body);
			this.bipedLeftArm.addChild(leftArm);
			this.bipedRightArm.addChild(rightArm);
		}
		if (slot == EquipmentSlotType.LEGS) {
			this.bipedLeftLeg.addChild(leftLeg);
			this.bipedRightLeg.addChild(rightLeg);
			//this.bipedBody.addChild(belt);
		}
		if (slot == EquipmentSlotType.FEET) {
			//this.bipedLeftLeg.addChild(leftBoot);
			//this.bipedRightLeg.addChild(rightBoot);
		}
	}
	
	@Override
	public void render(MatrixStack mStack, IRenderTypeBuffer getter, LivingEntity entity, ItemStack itemstack, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		if (entity == null || entity instanceof ArmorStandEntity) {
			crouching = false;
			riding = false;
			young = false;

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
		}

		GlStateManager._pushMatrix();

		if (entity.isCrouching()) {
			GlStateManager._translatef(0.0F, 0.2F, 0.0F);
		}

		this.bipedHead.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedRightArm.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedLeftArm.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedBody.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedRightLeg.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		this.bipedLeftLeg.render(mStack, getter, packedLight, packedOverlay, red, green, blue, alpha);
		GlStateManager._popMatrix();
	}
	
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float scale, Entity p_78087_7_) {
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
	}
}
