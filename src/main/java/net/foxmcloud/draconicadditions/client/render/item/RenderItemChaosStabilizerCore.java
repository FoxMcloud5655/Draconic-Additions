package net.foxmcloud.draconicadditions.client.render.item;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.render.state.GlStateTracker;
import codechicken.lib.util.TransformUtils;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.client.render.tile.RenderTileChaosStabilizerCore;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.IModelState;

public class RenderItemChaosStabilizerCore implements IItemRenderer {
	private static RenderTileChaosStabilizerCore coreRenderer = new RenderTileChaosStabilizerCore();

	public RenderItemChaosStabilizerCore() {}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public IModelState getTransforms() {
		return TransformUtils.DEFAULT_BLOCK;
	}

	@Override
	public void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
		if (item.getItem().equals(Item.getItemFromBlock(DAFeatures.chaosStabilizerCore))) {
			GlStateManager.pushMatrix();
			GlStateTracker.pushState();
			coreRenderer.renderItem();
			GlStateTracker.popState();
			GlStateManager.popMatrix();
		}
	}
}
