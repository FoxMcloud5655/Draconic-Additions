package net.foxmcloud.draconicadditions.items;

import com.brandon3055.draconicevolution.api.modules.ModuleCategory;

import net.foxmcloud.draconicadditions.lib.DAItemData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface IChaosContainer {
	
	public static final ModuleCategory CHAOS_CONTAINER = new ModuleCategory();

	/**
	 * Adds chaos to the container.  Returns the amount of chaos that was not added.
	 */
	public default int addChaos(ItemStack stack, int chaos) {
		int chaosToAdd = Math.min(getMaxChaos(stack) - getChaos(stack), chaos);
		stack.set(DAItemData.CHAOS, getChaos(stack) + chaosToAdd);
		return chaos - chaosToAdd;
	}

	/**
	 * Removes chaos from the container.  Returns the amount that was removed.
	 */
	public default int removeChaos(ItemStack stack, int chaos) {
		int chaosToRemove = Math.min(getChaos(stack), chaos);
		stack.set(DAItemData.CHAOS, getChaos(stack) - chaosToRemove);
		return chaosToRemove;
	}

	public default int getChaos(ItemStack stack) {
		Integer chaos = stack.get(DAItemData.CHAOS);
		return chaos != null ? chaos : 0;
	}

	public default int getMaxChaos(ItemStack stack) {
		return 0;
	}

	public default Component getChaosInfo(ItemStack stack) {
		return Component.translatable("info.da.storedChaos", getChaos(stack), getMaxChaos(stack)).withStyle(ChatFormatting.GRAY);
	}
}