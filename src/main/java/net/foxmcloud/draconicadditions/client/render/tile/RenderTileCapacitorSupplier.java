package net.foxmcloud.draconicadditions.client.render.tile;

import com.brandon3055.brandonscore.client.render.TESRBase;

import codechicken.lib.render.state.GlStateTracker;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileCapacitorSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by brandon3055 on 25/07/2016.
 */
public class RenderTileCapacitorSupplier extends TESRBase<TileCapacitorSupplier> {

    @Override
    public void render(TileCapacitorSupplier te, double x, double y, double z, float partialTicks, int destroyStage, float a) {
        super.render(te, x, y, z, partialTicks, destroyStage, a);
        ItemStack stack = te.getStackInSlot(0).copy();
        if (!stack.isEmpty()) {
        	GlStateManager.pushMatrix();
            GlStateTracker.pushState();
            GlStateManager.translate(x + 0.5, y + 0.9, z + 0.5);

            /*
            if (te.facing.getAxis() == EnumFacing.Axis.Y) {
                GlStateManager.rotate(90, te.facing.getFrontOffsetY(), 0, 0);
            }
            else if (te.facing.getAxis() == EnumFacing.Axis.X) {
                GlStateManager.rotate(90, 0, -te.facing.getFrontOffsetX(), 0);
            }
            else if (te.facing == EnumFacing.SOUTH) {
                GlStateManager.rotate(180, 0, 1, 0);
            }
            */

            if ((stack.getItem().isEnchantable(stack) || !(stack.getItem() instanceof ItemBlock))) {
                GlStateManager.scale(0.3F, 0.3F, 0.3F);
                GlStateManager.rotate(90, 1, 0, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            }
            else if (stack.getItem() instanceof ItemBlock) {
                float f = 0.2F;
                GlStateManager.scale(f, f, f);
                GlStateManager.rotate(90, 1, 0, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            }
            else {
                GlStateManager.scale(0.2F, 0.2F, 0.2F);
                GlStateManager.rotate(180, 1, 0, 0);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            }
            GlStateManager.popMatrix();
            GlStateTracker.popState();
        }
    }
}
