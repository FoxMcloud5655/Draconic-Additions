package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileEnergyInventoryBase;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;

import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.minecraft.item.ItemStack;

public class TileChaosHolderBase extends TileEnergyInventoryBase {

	private int maxChaos = 1000;

	public final ManagedInt chaos = register("chaos", new ManagedInt(0)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public int getMaxChaos() {
		return maxChaos;
	}

	public void setMaxChaos(int amount) {
		maxChaos = amount;
	}

	public void insertChaosIntoItem(ItemStack stack) {
		if (stack.getItem() instanceof ChaosContainer) {
			ChaosContainer item = (ChaosContainer) stack.getItem();
			int chaosToAdd = Math.min(item.getMaxChaos(stack) - item.getChaos(stack), chaos.value);
			item.addChaos(stack, chaosToAdd);
			chaos.value -= chaosToAdd;
		}
	}

	public void extractChaosFromItem(ItemStack stack) {
		if (stack.getItem() instanceof ChaosContainer) {
			ChaosContainer item = (ChaosContainer) stack.getItem();
			int chaosToRemove = Math.min(getMaxChaos() - chaos.value, item.getChaos(stack));
			item.removeChaos(stack, chaosToRemove);
			chaos.value += chaosToRemove;
		}
	}
}
