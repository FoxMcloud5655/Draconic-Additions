package net.foxmcloud.draconicadditions.items.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;

public class InfusedPotatoArmor extends ArmorItem {

	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmor(Properties props, EquipmentSlot slotType) {
		super(ArmorMaterials.LEATHER, slotType, props);
	}
}
