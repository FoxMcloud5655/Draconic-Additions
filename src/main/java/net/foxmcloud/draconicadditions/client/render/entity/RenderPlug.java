package net.foxmcloud.draconicadditions.client.render.entity;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.entity.EntityPlug;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPlug extends EntityRenderer<EntityPlug> {

	private EntityRendererProvider.Context context;
	
	public RenderPlug(EntityRendererProvider.Context context) {
		super(context);
		this.context = context;
	}

	public void doRender(EntityPlug entity, double x, double y, double z, float entityYaw, float partialTicks) {
		/*
		Player entityplayer = entity.getPlayer();

		if (entityplayer != null) {
			//this.bindEntityTexture(entity);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuilder();
			int k = entityplayer.getMainArm() == HandSide.RIGHT ? 1 : -1;
			ItemStack itemstack = entityplayer.getMainHandItem();
			/*
			if (!(itemstack.getItem() instanceof PortableWiredCharger)) {
				k = -k;
			}

			float f1 = entityplayer.getCurrentItemAttackStrengthDelay();//TODO: swingAmount(partialTicks);
			float f2 = MathHelper.sin(MathHelper.sqrt(f1) * (float) Math.PI);
			float f3 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292F;
			double d0 = (double) MathHelper.sin(f3);
			double d1 = (double) MathHelper.cos(f3);
			double d2 = (double) k * 0.35D;
			double d4, d5, d6, d7;

			if ((this.renderManager.options == null) && entityplayer == Minecraft.getInstance().player) {
				double f4 = this.renderManager.options.fov;
				f4 = f4 / 100.0F;
				Vector3d vec3d = new Vector3d((double)k * -0.36D * (double)f4 + (k < 0 ? -0.07D : 0), -0.045D * (double)f4, 0.4D);
				vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(f2 * 0.5F);
				vec3d = vec3d.rotatePitch(-f2 * 0.7F);
				d4 = entityplayer.xOld + (entityplayer.position().x - entityplayer.xOld) * (double) partialTicks + vec3d.x;
				d5 = -0.3D + entityplayer.yOld + (entityplayer.position().y - entityplayer.yOld) * (double) partialTicks + vec3d.y;
				d6 = entityplayer.zOld + (entityplayer.position().z - entityplayer.zOld) * (double) partialTicks + vec3d.z;
				d7 = (double) entityplayer.getEyeHeight();
			}
			else {
				d4 = entityplayer.xOld + (entityplayer.position().x - entityplayer.xOld) * (double) partialTicks - d1 * d2 - d0 * 0.8D;
				d5 = entityplayer.yOld + (double) entityplayer.getEyeHeight() + (entityplayer.position().y - entityplayer.yOld) * (double) partialTicks - 0.45D;
				d6 = entityplayer.zOld + (entityplayer.position().z - entityplayer.zOld) * (double) partialTicks - d0 * d2 + d1 * 0.8D;
				d7 = entityplayer.isSneaking() ? -0.1875D : 0.0D;
			}

			double d13 = entity.xOld + (entity.position().x - entity.xOld);
			double d8 = entity.yOld + (entity.position().y - entity.yOld) + 0.2D;
			double d9 = entity.zOld + (entity.position().z - entity.zOld);
			double d10 = (double) ((float) (d4 - d13));
			double d11 = (double) ((float) (d5 - d8)) + d7;
			double d12 = (double) ((float) (d6 - d9));
			RenderSystem.disableTexture();
			RenderSystem.disableLighting();
			bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
			int l = 16;

			for (int i1 = 0; i1 <= 16; ++i1) {
				float f11 = (float) i1 / 16.0F;
				bufferbuilder.vertex(x + d10 * (double) f11, y + d11 * (double) (f11 * f11 + f11) * 0.5D + 0.5D, z + d12 * (double) f11).color(0, 0, 0, 255).endVertex();
			}

			tessellator.end();
			RenderSystem.enableLighting();
			RenderSystem.enableTexture();

			RenderSystem.pushMatrix();
			RenderSystem.translated(x, y, z);
			RenderSystem.rotatef(entity.getRotationVector().y, 0, 1, 0);
			RenderSystem.rotatef(-entity.getRotationVector().x, 1, 0, 0);
			RenderSystem.translated(-0.5, entity.getRotationVector().x == 0 ? 0 : -0.5, entity.getRotationVector().x / 180.0F);
			RenderSystem.scaled(0.125, 0.125, 0.125);
			RenderSystem.translated(3.5, 3.5, 0.1);
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.vertex(0, 1, 0).uv(0, 0).endVertex();
			bufferbuilder.vertex(0, 0, 0).uv(0, 1).endVertex();
			bufferbuilder.vertex(1, 0, 0).uv(1, 1).endVertex();
			bufferbuilder.vertex(1, 1, 0).uv(1, 0).endVertex();
			tessellator.end();
			RenderSystem.popMatrix();
		}
		*/
	}

	@Override
	public ResourceLocation getTextureLocation(EntityPlug entity) {
		return new ResourceLocation(DraconicAdditions.MODID, "entity/plug");
	}
}
