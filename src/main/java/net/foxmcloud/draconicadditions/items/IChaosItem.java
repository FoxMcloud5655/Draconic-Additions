package net.foxmcloud.draconicadditions.items;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import net.minecraft.item.ItemStack;

public interface IChaosItem {
	
    public default boolean isChaosStable(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "isStable", false);
    }
    
    public default void setChaosStable(ItemStack stack, boolean stable) {
		ItemNBTHelper.setBoolean(stack, "isStable", stable);
    }
}
