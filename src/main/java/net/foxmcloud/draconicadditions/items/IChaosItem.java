package net.foxmcloud.draconicadditions.items;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public interface IChaosItem {

	public default boolean isChaosStable(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "isStable", false);
	}

	public default void setChaosStable(ItemStack stack, boolean stable) {
		ItemNBTHelper.setBoolean(stack, "isStable", stable);
    }
	
	public default String getChaosInfo(ItemStack stack) {
		if (!isChaosStable(stack)) return I18n.format("item.draconicadditions:chaosItem.lore");
		else return null;
	}
}
