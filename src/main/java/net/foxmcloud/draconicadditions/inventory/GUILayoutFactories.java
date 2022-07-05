package net.foxmcloud.draconicadditions.inventory;

import com.brandon3055.brandonscore.inventory.ContainerSlotLayout;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquefier;

public class GUILayoutFactories {
	public static final ContainerSlotLayout.LayoutFactory<TileChaosLiquefier> CHAOS_LIQUEFIER_LAYOUT = (player, tile) -> new ContainerSlotLayout().playerMain(player).allTile(tile.itemHandler);
}
