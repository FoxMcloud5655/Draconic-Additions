package net.foxmcloud.draconicadditions.client.render.item;

import com.brandon3055.brandonscore.api.TimeKeeper;
import com.brandon3055.draconicevolution.client.render.item.RenderItemReactorComponent;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileReactorComponent;
import com.brandon3055.draconicevolution.client.render.tile.RenderTileReactorCore;
import com.mojang.blaze3d.vertex.PoseStack;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Created by FoxMcloud5655 on 4/25/2026.
 */
public class RenderItemFakeReactorComponent extends RenderItemReactorComponent {
    public RenderItemFakeReactorComponent() {}

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack mStack, MultiBufferSource getter, int packedLight, int packedOverlay) {
        Matrix4 mat = new Matrix4(mStack);
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();
        ccrs.brightness = packedLight;
        ccrs.overlay = packedOverlay;
        Item item = stack.getItem();

        DeltaTracker delta = Minecraft.getInstance().getTimer();
        if (item == DAContent.fakeReactorCore.get().asItem()) {
            mat.translate(0.5, 0.5, 0.5);
            mat.scale(1.5);
            RenderTileReactorCore.renderCore(mat, ccrs, (TimeKeeper.getClientTick() + delta.getGameTimeDeltaPartialTick(false)) / 100F, 0F, 0.F, 0.5F, 0, getter);
        } else if (item == DAContent.fakeReactorStabilizer.get().asItem()) {
            float coreRotation = (TimeKeeper.getClientTick() + delta.getGameTimeDeltaPartialTick(false)) * 5F;
            mat.translate(0.5, 0, 0.5);
            RenderTileReactorComponent.renderStabilizer(ccrs, mat, getter, coreRotation, 1F, packedLight, packedOverlay);
        } else if (item == DAContent.fakeReactorInjector.get().asItem()) {
            mat.translate(0.5, 0.5, 0);
            mat.rotate(90 * MathHelper.torad, Vector3.X_POS);
            RenderTileReactorComponent.renderInjector(ccrs, mat, getter, 1F, packedLight, packedOverlay);
        } else {
            RenderTileReactorComponent.renderComponent(item, ccrs, mat, getter, packedLight, packedOverlay);
        }
    }
}
