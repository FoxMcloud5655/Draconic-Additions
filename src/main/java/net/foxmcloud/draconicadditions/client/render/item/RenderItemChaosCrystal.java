package net.foxmcloud.draconicadditions.client.render.item;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;
import com.brandon3055.draconicevolution.utils.DETextures;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.OBJParser;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateTracker;
import codechicken.lib.util.TransformUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.IModelState;

public class RenderItemChaosCrystal implements IItemRenderer {
	private static RenderItemChaosCrystal crystalRenderer = new RenderItemChaosCrystal();
	private static CCModel model = null;

	public RenderItemChaosCrystal() {
		if (model == null) {
			Map<String, CCModel> map = OBJParser.parseModels(ResourceHelperDE.getResource("models/chaos_crystal.obj"));
			model = CCModel.combine(map.values());
		}
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public void renderItem(ItemStack stack, TransformType transformType) {
		if (stack.getItem().equals(Item.getItemFromBlock(DAFeatures.chaosCrystalStable))) {
			GlStateManager.pushMatrix();
			GlStateTracker.pushState();
			CCRenderState ccrs = CCRenderState.instance();
			ResourceHelperDE.bindTexture(DETextures.CHAOS_CRYSTAL);
			ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
			Rotation rotation = new Rotation((ClientEventHandler.elapsedTicks) / 80F, 0, 1, 0);
			Matrix4 mat = RenderUtils.getMatrix(new Vector3(0.5, 0.5, 0.5), rotation, 0.34);
			model.render(ccrs, mat);
			ccrs.draw();
			GlStateTracker.popState();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public IModelState getTransforms() {
		return TransformUtils.DEFAULT_BLOCK;
	}

}
