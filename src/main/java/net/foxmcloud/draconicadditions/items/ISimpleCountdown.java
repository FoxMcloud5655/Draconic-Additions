package net.foxmcloud.draconicadditions.items;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import net.minecraft.world.item.ItemStack;

public interface ISimpleCountdown {

	public default int getCurrentCountdown(ItemStack stack) {
		return ItemNBTHelper.getInteger(stack, "simplecountdown", 0);
	}

	public int getCountdownAmount(ItemStack stack);

	public default boolean advanceCountdown(ItemStack stack) {
		return advanceCountdown(stack, 1);
	}

	public default boolean advanceCountdown(ItemStack stack, int amount) {
		int tickCounter = getCurrentCountdown(stack);
		ItemNBTHelper.setInteger(stack, "simplecountdown", tickCounter - amount);
		return tickCounter - amount <= 0;
	}

	public default void resetCountdown(ItemStack stack) {
		ItemNBTHelper.setInteger(stack, "simplecountdown", getCountdownAmount(stack));
	}

	public default void removeCountdown(ItemStack stack) {
		ItemNBTHelper.getCompound(stack).remove("simplecountdown");
	}
}
