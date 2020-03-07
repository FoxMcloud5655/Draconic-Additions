package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileEnergyInventoryBase;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;

import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.minecraft.item.ItemStack;

public class TileChaosHolderBase extends TileEnergyInventoryBase {

	private int maxChaos = 2000;

	public final ManagedInt chaos = register("chaos", new ManagedInt(0)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public int getMaxChaos() {
		return maxChaos;
	}

	public void setMaxChaos(int amount) {
		maxChaos = amount;
	}
}
