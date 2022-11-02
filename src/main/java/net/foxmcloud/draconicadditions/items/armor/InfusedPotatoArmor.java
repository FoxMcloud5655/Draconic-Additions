package net.foxmcloud.draconicadditions.items.armor;

import javax.annotation.Nullable;

import com.brandon3055.draconicevolution.client.model.VBOBipedModel;

import net.covers1624.quack.util.SneakyUtils;
import net.foxmcloud.draconicadditions.client.model.InfusedPotatoArmorModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class InfusedPotatoArmor extends ArmorItem {

	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmor(Properties props, EquipmentSlot slotType) {
		super(ArmorMaterials.LEATHER, slotType, props);
	}

	@OnlyIn(Dist.CLIENT)
	private VBOBipedModel<?> model;

	@Nullable
	@OnlyIn(Dist.CLIENT)
	public <A extends VBOBipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		if (model == null) {
			model = new InfusedPotatoArmorModel(1F, armorSlot);
		}
		return SneakyUtils.unsafeCast(model);
	}

}
