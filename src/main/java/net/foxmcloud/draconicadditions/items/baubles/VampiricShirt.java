package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.api.itemconfig.IConfigurableItem;
import com.brandon3055.draconicevolution.api.itemconfig.IItemConfigField.EnumControlType;
import com.brandon3055.draconicevolution.api.itemconfig.IntegerConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler.ArmorSummery;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class VampiricShirt extends BasicBauble implements IConfigurableItem {

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BODY;
	}

	@Override
	public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
		registry.register(stack, new IntegerConfigField("shirtEntropyActivate", 80, 10, 95, "config.field.shirtEntropyActivate.description", EnumControlType.SLIDER));
		registry.register(stack, new IntegerConfigField("shirtSafetyCutoff", 10, 1, 100, "config.field.shirtSafetyCutoff.description", EnumControlType.PLUS2_MINUS2));
		return registry;
	}

	@Override
	public int getProfileCount(ItemStack stack) {
		return 1;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		EntityPlayer player = (EntityPlayer) entity;
		ArmorSummery summary = new ArmorSummery().getSummery(player);
		if (summary == null || summary.protectionPoints <= 0 || summary.entropy < ToolConfigHelper.getIntegerField("shirtEntropyActivate", stack) || player.getHealth() <= ToolConfigHelper.getIntegerField("shirtSafetyCutoff", stack) || player.ticksExisted % 10 != 0) {
			return;
		}
		float newEntropy = Math.max(summary.entropy - 1, 0F);
		for (int i = 0; i < summary.allocation.length; i++) {
			if (summary.allocation[i] == 0) continue;
			ItemStack armor = summary.armorStacks.get(i);
			ItemNBTHelper.setFloat(armor, "ShieldEntropy", newEntropy);
		}
		summary.saveStacks(player);
		if (!player.isCreative()) {
			player.setHealth(player.getHealth() - 1);
			player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 1F);
		}
	}
}
