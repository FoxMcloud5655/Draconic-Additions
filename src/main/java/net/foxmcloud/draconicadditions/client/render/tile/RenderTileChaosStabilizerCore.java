package net.foxmcloud.draconicadditions.client.render.tile;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.brandon3055.brandonscore.client.render.TESRBase;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.client.render.shaders.DEShaders;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;
import com.brandon3055.draconicevolution.utils.DETextures;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.OBJParser;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.render.shader.ShaderProgram;
import codechicken.lib.render.state.GlStateTracker;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity.TileChaosStabilizerCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.MinecraftForgeClient;


/**
 * Created by brandon3055 on 6/11/2016.
 */
public class RenderTileChaosStabilizerCore extends TESRBase<TileChaosStabilizerCore> {

	private static CCModel model = null;
	private static CCModel model_no_shade;

	private static ShaderProgram reactorProgram;

	public RenderTileChaosStabilizerCore() {
		if (model == null) {
			Map<String, CCModel> map = OBJParser.parseModels(ResourceHelperDE.getResource("models/block/obj_models/reactor_core.obj"));
			model = CCModel.combine(map.values());
			map = OBJParser.parseModels(ResourceHelperDE.getResource("models/reactor_core_model.obj"));
			model_no_shade = CCModel.combine(map.values());
		}
	}

	@Override
	public void render(TileChaosStabilizerCore te, double x, double y, double z, float partialTicks, int destroyStage, float a) {
		GlStateManager.pushMatrix();
		GlStateTracker.pushState();
		GlStateManager.disableLighting();
		setLighting(100);
		double diameter = te.getCoreDiameter(); // How big to render.
		double t = te.getCoreIntensity(); // How hot to render.
		double intensity = t <= 0.2 ? Utils.map(t, 0, 0.2, 0, 0.3) : t <= 0.8 ? Utils.map(t, 0.2, 0.8, 0.3, 1) : Utils.map(t, 0.8, 1, 1, 1.3);
		if (MinecraftForgeClient.getRenderPass() == 0) {
			float animation = partialTicks / 10F;
			renderCore(x, y, z, partialTicks, (float) intensity, animation, diameter, DEShaders.useShaders());
		}

		resetLighting();
		GlStateTracker.popState();
		GlStateManager.popMatrix();
	}

	public void renderItem() {
		GlStateManager.pushMatrix();
		GlStateTracker.pushState();
		GlStateManager.disableLighting();
		setLighting(100);
		float scale = 1.3F;
		float animation = 0;
		float intensity = 0.25F;

		renderCore(0, 0, 0, 0, intensity, animation, scale, DEShaders.useShaders());

		resetLighting();
		GlStateTracker.popState();
		GlStateManager.popMatrix();
	}

	private static void renderCore(double x, double y, double z, float partialTicks, float intensity, float animation, double diameter, boolean useShader) {
		ResourceHelperDE.bindTexture(DETextures.REACTOR_CORE);
		if (useShader) {
			if (reactorProgram == null) {
				reactorProgram = new ShaderProgram();
				reactorProgram.attachShader(DEShaders.reactor);
			}
			reactorProgram.useShader(cache -> {
				cache.glUniform1F("time", animation);
				cache.glUniform1F("intensity", intensity);
			});
		}

		CCRenderState ccrs = CCRenderState.instance();
		ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
		Matrix4 mat = RenderUtils.getMatrix(new Vector3(x + 0.5, y + 0.5, z + 0.5), new Rotation((ClientEventHandler.elapsedTicks + partialTicks) / 400F, 0, 1, 0), diameter);
		model.render(ccrs, mat);
		ccrs.draw();

		if (useShader) {
			reactorProgram.releaseShader();
		}
	}

	@Override
	public boolean isGlobalRenderer(TileChaosStabilizerCore te) {
		return true;
	}
}
