package net.foxmcloud.draconicadditions.items;

import net.foxmcloud.draconicadditions.lib.DAItemData;
import net.minecraft.world.item.ItemStack;

public interface ISimpleCountdown {

	public default int getCurrentCountdown(ItemStack stack) {
		Integer countdown = stack.get(DAItemData.COUNTDOWN);
		return countdown != null ? countdown : 0;
	}

	public int getCountdownAmount(ItemStack stack);

	public default boolean advanceCountdown(ItemStack stack) {
		return advanceCountdown(stack, 1);
	}

	public default boolean advanceCountdown(ItemStack stack, int amount) {
		int tickCounter = getCurrentCountdown(stack);
		stack.set(DAItemData.COUNTDOWN, tickCounter - amount);
		return tickCounter - amount <= 0;
	}

	public default void resetCountdown(ItemStack stack) {
		stack.set(DAItemData.COUNTDOWN, getCountdownAmount(stack));
	}

	public default void removeCountdown(ItemStack stack) {
		stack.remove(DAItemData.COUNTDOWN);
	}
}
