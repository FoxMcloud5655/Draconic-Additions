package net.foxmcloud.draconicadditions.items.armor;

import javax.annotation.Nullable;

import codechicken.lib.util.SneakyUtils;
import net.foxmcloud.draconicadditions.client.model.InfusedPotatoArmorModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InfusedPotatoArmor extends ArmorItem {

	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmor(Properties props, EquipmentSlotType slotType) {
		super(ArmorMaterial.LEATHER, slotType, props);
	}

	@OnlyIn(Dist.CLIENT)
	private BipedModel<?> model;

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		if (model == null) {
			model = new InfusedPotatoArmorModel(1F, armorSlot);
		}
		return SneakyUtils.unsafeCast(model);
	}

}
