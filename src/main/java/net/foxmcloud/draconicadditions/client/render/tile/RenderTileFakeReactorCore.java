package net.foxmcloud.draconicadditions.client.render.tile;

import java.util.Map;

import com.brandon3055.brandonscore.api.TimeKeeper;
import com.brandon3055.brandonscore.client.render.BlockEntityRendererTransparent;
import com.brandon3055.brandonscore.client.render.RenderUtils;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.MathUtils;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.client.DEShaders;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileReactorCore;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import codechicken.lib.gui.modular.lib.GuiRender;
import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.buffer.TransformingVertexConsumer;
import codechicken.lib.render.model.OBJParser;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorComponent;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorCore;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorInjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

public class RenderTileFakeReactorCore implements BlockEntityRendererTransparent<TileFakeReactorCore> {

	private static CCModel model = null;

	public RenderTileFakeReactorCore(BlockEntityRendererProvider.Context context) {
		if (model == null) {
			Map<String, CCModel> map = new OBJParser(ResourceLocation.fromNamespaceAndPath(DraconicEvolution.MODID, "models/block/reactor/reactor_core.obj")).quads().ignoreMtl().parse();
			model = CCModel.combine(map.values());
		}
	}

	@Override
	public void render(TileFakeReactorCore te, float partialTicks, PoseStack mStack, MultiBufferSource getter, int packedLight, int packedOverlay) {
		Matrix4 mat = new Matrix4(mStack);
		CCRenderState ccrs = CCRenderState.instance();
		ccrs.reset();
		ccrs.brightness = packedLight;
		ccrs.overlay = packedOverlay;
		mat.translate(0.5, 0.5, 0.5);
		mat.scale(te.getCoreDiameter());
		mat.rotate((ClientEventHandler.elapsedTicks + partialTicks) / 400F, Vector3.Y_POS);
	}

	public static void renderGUI(GuiRender render, TileFakeReactorCore te) {
		double diameter = 100;
		float t = (float) (te.temperature.get() / TileFakeReactorCore.MAX_TEMPERATURE);
		float intensity = t <= 0.2 ? (float) MathUtils.map(t, 0, 0.2, 0, 0.3) : t <= 0.8 ? (float) MathUtils.map(t, 0.2, 0.8, 0.3, 1) : (float) MathUtils.map(t, 0.8, 1, 1, 1.3);
		float animation = (te.coreAnimation + (0 * (float) te.shaderAnimationState.get())) / 20F;
		float shieldPower = (float) (te.maxShieldCharge.get() > 0 ? te.shieldCharge.get() / te.maxShieldCharge.get() : 0);
		Minecraft mc = Minecraft.getInstance();
		Matrix4 mat = new Matrix4(render.pose());
		mat.scale(diameter);
		float partialTicks = mc.getTimer().getGameTimeDeltaPartialTick(false);
		mat.rotate((TimeKeeper.getClientTick() + partialTicks) / 400F, Vector3.Y_POS);
		CCRenderState ccrs = CCRenderState.instance();
		ccrs.reset();
		RenderSystem.depthMask(false);
		RenderTileReactorCore.renderCore(mat, ccrs, animation, te.shaderAnimationState.get(), intensity, shieldPower, partialTicks, render.buffers());
		RenderSystem.depthMask(true);
	}

	@Override
	public void renderTransparent(TileFakeReactorCore te, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
		Matrix4 mat = new Matrix4(poseStack);
		CCRenderState ccrs = CCRenderState.instance();
		ccrs.reset();
		ccrs.brightness = packedLight;
		ccrs.overlay = packedOverlay;

		double diameter = te.getCoreDiameter();
		float t = (float) (te.temperature.get() / TileFakeReactorCore.MAX_TEMPERATURE);
		float intensity = t <= 0.2 ? (float) MathUtils.map(t, 0, 0.2, 0, 0.3) : t <= 0.8 ? (float) MathUtils.map(t, 0.2, 0.8, 0.3, 1) : (float) MathUtils.map(t, 0.8, 1, 1, 1.3);
		float shieldPower = (float) (te.maxShieldCharge.get() > 0 ? te.shieldCharge.get() / te.maxShieldCharge.get() : 0);
		float animation = (te.coreAnimation + (partialTicks * (float) te.shaderAnimationState.get())) / 20F;

		mat.translate(0.5, 0.5, 0.5);
		mat.scale(diameter);
		mat.rotate((ClientEventHandler.elapsedTicks + partialTicks) / 400F, Vector3.Y_POS);

		RenderTileReactorCore.renderCore(mat, ccrs, animation, te.shaderAnimationState.get(), intensity, shieldPower, partialTicks, buffers);

		float coreSize = (float) te.getCoreDiameter() / 2.3F;
		float fxState = te.shieldAnimationState;

		for (Direction direction : Direction.values()) {
			TileFakeReactorComponent component = te.getComponent(direction.getOpposite());
			if (component == null) continue;
			Direction facing = component.facing.get();
			float dist = (float) Math.sqrt(component.getBlockPos().distSqr(te.getBlockPos()));

			Vec3D pos1 = Vec3D.getCenter(component.getBlockPos()).subtract(new Vec3D(te.getBlockPos())).offset(facing, -0.35D);

			if (component instanceof TileFakeReactorInjector) {
				Vec3D pos2 = pos1.copy().offset(facing, 0.6D);

				DEShaders.reactorBeamType.glUniformI(2);
				DEShaders.reactorBeamFade.glUniform1f(1F);
				DEShaders.reactorBeamPower.glUniform1f(fxState);
				DEShaders.reactorBeamStartup.glUniform1f(fxState);
				renderShaderBeam(ccrs, facing, fxState, buffers, poseStack, pos1, 0.1F, 0.1F, 0.6F, true, false);

				DEShaders.reactorBeamFade.glUniform1f(0F);
				renderShaderBeam(ccrs, facing, fxState, buffers, poseStack, pos2, 0.1F, coreSize / 1.5, dist - (coreSize * 1.3F), false, false);
			} else {
				Vec3D pos2 = pos1.copy().offset(facing, 0.8D);

				//Inner Inner
				DEShaders.reactorBeamType.glUniformI(1);
				DEShaders.reactorBeamFade.glUniform1f(1F);
				DEShaders.reactorBeamPower.glUniform1f((float) te.animExtractState.get());
				DEShaders.reactorBeamStartup.glUniform1f((float) te.animExtractState.get());
				renderShaderBeam(ccrs, facing, fxState, buffers, poseStack, pos1, 0.263F, 0.263F, 0.8F, true, false);

				DEShaders.reactorBeamFade.glUniform1f(0F);
				renderShaderBeam(ccrs, facing, fxState, buffers, poseStack, pos2, 0.263F, coreSize / 2, dist - (coreSize * 1.3F), false, false);

				//Draw Outer
				DEShaders.reactorBeamType.glUniformI(0);
				DEShaders.reactorBeamFade.glUniform1f(1F);
				DEShaders.reactorBeamPower.glUniform1f(fxState);
				DEShaders.reactorBeamStartup.glUniform1f(fxState);
				renderShaderBeam(ccrs, facing, fxState, buffers, poseStack, pos1, 0.355F, 0.355F, 0.8F, true, false);

				DEShaders.reactorBeamFade.glUniform1f(0F);
				renderShaderBeam(ccrs, facing, fxState, buffers, poseStack, pos2, 0.355F, coreSize, dist - coreSize, false, true);
			}
			ccrs.reset();
		}
	}

	public void renderShaderBeam(CCRenderState ccrs, Direction facing, float fxState, MultiBufferSource buffers, PoseStack poseStack, Vec3D pos, double widthStart, double widthEnd, float length, boolean fadeReverse, boolean highRes) {
		VertexConsumer buffer = new TransformingVertexConsumer(buffers.getBuffer(RenderTileReactorCore.REACTOR_BEAM_TYPE), poseStack);
		ccrs.bind(buffer, DefaultVertexFormat.POSITION_TEX_COLOR);

		float sides = highRes ? 599 : 99;
		for (int i = 0; i < sides + 1; i++) {
			double angle = (i / sides) * (Math.PI * 2);// + beamRotation;
			float sin = (float) MathHelper.sin(angle);
			float cos = (float) MathHelper.cos(angle);
			float texX = i / sides;
			Vec3D point = pos.copy().radialOffset(facing.getAxis(), sin, cos, widthStart);
			buffer.addVertex((float) point.x, (float) point.y, (float) point.z).setColor(1F, 1F, 1F, fadeReverse ? 0F : fxState).setUv(texX, (fadeReverse ? 0.1F : 1F));
			point.offset(facing, length);
			point.radialOffset(facing.getAxis(), sin, cos, widthEnd - widthStart);
			buffer.addVertex((float) point.x, (float) point.y, (float) point.z).setColor(1F, 1F, 1F, fadeReverse ? fxState : 0F).setUv(texX, 0);
		}
		RenderUtils.endBatch(buffers);
	}

	@Override
	public boolean shouldRenderOffScreen(TileFakeReactorCore te) {
		return true;
	}

	@Override
	public int getViewDistance() {
		return 256;
	}

	@Override
	public AABB getRenderBoundingBox(TileFakeReactorCore blockEntity) {
		BlockPos pos = blockEntity.getBlockPos();
		return new AABB(pos.getX() - 14, pos.getY() - 14, pos.getZ() - 14, pos.getX() + 15, pos.getY() + 15, pos.getZ() + 15);
	}
}
