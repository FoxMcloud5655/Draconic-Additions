package net.foxmcloud.draconicadditions.client.render.entity;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.entity.EntityChaosHeart;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderChaosHeart extends EntityRenderer<EntityChaosHeart> {

	public RenderChaosHeart(EntityRendererProvider.Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	public void doRender(EntityChaosHeart entity, double x, double y, double z, float entityYaw, float partialTicks) {
		/*
		RenderSystem.pushMatrix();
		RenderSystem.translated(x, y + (Math.cos((ClientEventHandler.elapsedTicks + partialTicks) / 20D) * 0.1) - 0.5, z);
		RenderSystem.rotatef((entity.rotation + (entity.rotationInc * partialTicks)) * 40, 0, 1, 0);
		RenderSystem.scalef(2F, 2F, 2F);

		float sine = (float) Math.abs(Math.cos(ClientEventHandler.elapsedTicks / 100D) - 0.4F);

		RenderSystem.disableCull();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderSystem.enableBlend();
		RenderSystem.disableLighting();
		RenderSystem.color4f(2f - sine * 1.3f, 1f - sine / 1.5f, 1f - sine / 1.8f, 1F - sine / 1.6f);

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buffer = tess.getBuilder();

		RenderSystem.scalef(0.55f, 0.55f, 0.55f);
		RenderSystem.translated(-0.5, 0.47, 0.061);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.vertex(0, 1, 0).uv(0, 0).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 1).endVertex();
		buffer.vertex(1, 0, 0).uv(1, 1).endVertex();
		buffer.vertex(1, 1, 0).uv(1, 0).endVertex();
		tess.end();

		RenderSystem.translated(0, 0, -0.12);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.vertex(0, 1, 0).uv(0, 0).endVertex();
		buffer.vertex(0, 0, 0).uv(0, 1).endVertex();
		buffer.vertex(1, 0, 0).uv(1, 1).endVertex();
		buffer.vertex(1, 1, 0).uv(1, 0).endVertex();
		tess.end();

		RenderSystem.disableBlend();
		RenderSystem.enableLighting();

		RenderSystem.popMatrix();
		*/
	}

	@Override
	public ResourceLocation getTextureLocation(EntityChaosHeart entity) {
		return new ResourceLocation(DraconicAdditions.MODID, "item/chaos_heart");
	}
}

