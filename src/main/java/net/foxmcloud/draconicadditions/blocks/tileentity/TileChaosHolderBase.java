package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileEnergyInventoryBase;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;

public class TileChaosHolderBase extends TileEnergyInventoryBase {

	private int maxChaos = 2000;

	public final ManagedInt chaos = register(new ManagedInt("chaos", 0)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public int getMaxChaos() {
		return maxChaos;
	}

	public void setMaxChaos(int amount) {
		maxChaos = amount;
	}
}
