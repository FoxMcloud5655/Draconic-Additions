package net.foxmcloud.draconicadditions.client.render.tile;

import static codechicken.lib.math.MathHelper.torad;

import com.brandon3055.draconicevolution.client.render.tile.RenderTileReactorComponent;
import com.mojang.blaze3d.vertex.PoseStack;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorComponent;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorInjector;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorStabilizer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class RenderTileFakeReactorComponent implements BlockEntityRenderer<TileFakeReactorComponent> {
    public RenderTileFakeReactorComponent(BlockEntityRendererProvider.Context context) {
    }

    @SuppressWarnings("incomplete-switch")
	@Override
    public void render(TileFakeReactorComponent te, float partialTicks, PoseStack mStack, MultiBufferSource getter, int packedLight, int packedOverlay) {
        Matrix4 mat = new Matrix4(mStack);
        mat.translate(0.5, 0, 0.5);

        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();
        ccrs.brightness = packedLight;
        ccrs.overlay = packedOverlay;

        switch (te.facing.get()) {
            case SOUTH -> mat.rotate(180 * torad, Vector3.Y_POS);
            case EAST -> mat.rotate(-90 * torad, Vector3.Y_POS);
            case WEST -> mat.rotate(90 * torad, Vector3.Y_POS);
            case UP -> mat.apply(new Rotation(90 * torad, Vector3.X_POS).at(new Vector3(0, 0.5, 0)));
            case DOWN -> mat.apply(new Rotation(-90 * torad, Vector3.X_POS).at(new Vector3(0, 0.5, 0)));
        }

        if (te instanceof TileFakeReactorStabilizer) {
            float coreRotation = te.animRotation + (partialTicks * te.animRotationSpeed);//Remember Partial Ticks here
            RenderTileReactorComponent.renderStabilizer(ccrs, mat, getter, coreRotation, te.animRotationSpeed / 15F, packedLight, packedOverlay);
        } else if (te instanceof TileFakeReactorInjector) {
        	RenderTileReactorComponent.renderInjector(ccrs, mat, getter, te.animRotationSpeed / 15F, packedLight, packedOverlay);
        }

        ccrs.reset();
    }
}
