package net.foxmcloud.draconicadditions.items.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;

public class InfusedPotatoArmor extends ArmorItem {
	
	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmor(Properties props, EquipmentSlotType slotType) {
		super(ArmorMaterial.LEATHER, slotType, props);
	}
}
