package net.foxmcloud.draconicadditions.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.foxmcloud.draconicadditions.entity.EntityPlug;
import net.foxmcloud.draconicadditions.items.tools.PortableWiredCharger;
import net.foxmcloud.draconicadditions.utils.DATextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderPlug extends Render<EntityPlug> {

	public RenderPlug(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityPlug entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EntityPlayer entityplayer = entity.getPlayer();

		if (entityplayer != null) {
			this.bindEntityTexture(entity);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			int k = entityplayer.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
			ItemStack itemstack = entityplayer.getHeldItemMainhand();
			if (!(itemstack.getItem() instanceof PortableWiredCharger)) {
				k = -k;
			}

			float f1 = entityplayer.getSwingProgress(partialTicks);
			float f2 = MathHelper.sin(MathHelper.sqrt(f1) * (float) Math.PI);
			float f3 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292F;
			double d0 = (double) MathHelper.sin(f3);
			double d1 = (double) MathHelper.cos(f3);
			double d2 = (double) k * 0.35D;
			double d3 = 0.8D;
			double d4, d5, d6, d7;

			if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && entityplayer == Minecraft.getMinecraft().player) {
				float f4 = this.renderManager.options.fovSetting;
				f4 = f4 / 100.0F;
				Vec3d vec3d = new Vec3d((double)k * -0.36D * (double)f4, -0.045D * (double)f4, 0.4D);
				vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(f2 * 0.5F);
				vec3d = vec3d.rotatePitch(-f2 * 0.7F);
				d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) partialTicks + vec3d.x;
				d5 = -0.3D + entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double) partialTicks + vec3d.y;
				d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) partialTicks + vec3d.z;
				d7 = (double) entityplayer.getEyeHeight();
			}
			else {
				d4 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) partialTicks - d1 * d2 - d0 * 0.8D;
				d5 = entityplayer.prevPosY + (double) entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * (double) partialTicks - 0.45D;
				d6 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) partialTicks - d0 * d2 + d1 * 0.8D;
				d7 = entityplayer.isSneaking() ? -0.1875D : 0.0D;
			}

			double d13 = entity.prevPosX + (entity.posX - entity.prevPosX);
			double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) + 0.2D;
			double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ);
			double d10 = (double) ((float) (d4 - d13));
			double d11 = (double) ((float) (d5 - d8)) + d7;
			double d12 = (double) ((float) (d6 - d9));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
			int l = 16;

			for (int i1 = 0; i1 <= 16; ++i1) {
				float f11 = (float) i1 / 16.0F;
				bufferbuilder.pos(x + d10 * (double) f11, y + d11 * (double) (f11 * f11 + f11) * 0.5D + 0.5D, z + d12 * (double) f11).color(0, 0, 0, 255).endVertex();
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(entity.getPitchYaw().y, 0, 1, 0);
			GlStateManager.rotate(-entity.getPitchYaw().x, 1, 0, 0);
			GlStateManager.translate(-0.5, entity.getPitchYaw().x == 0 ? 0 : -0.5, entity.getPitchYaw().x / 180.0F);
			GlStateManager.scale(0.125, 0.125, 0.125);
			GlStateManager.translate(3.5, 3.5, 0);
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos(0, 1, 0).tex(0, 0).endVertex();
			bufferbuilder.pos(0, 0, 0).tex(0, 1).endVertex();
			bufferbuilder.pos(1, 0, 0).tex(1, 1).endVertex();
			bufferbuilder.pos(1, 1, 0).tex(1, 0).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPlug entity) {
		return DATextures.ENTITY_PLUG;
	}

}
