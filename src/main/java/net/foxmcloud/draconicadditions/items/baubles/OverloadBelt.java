package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class OverloadBelt extends BasicBauble {
	
	public OverloadBelt() {
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}
	
	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		ItemNBTHelper.setBoolean(stack, "Active", false);
		super.onUnequipped(stack, player);
	}
}
