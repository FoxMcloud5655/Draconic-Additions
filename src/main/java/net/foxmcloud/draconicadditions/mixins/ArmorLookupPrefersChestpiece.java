package net.foxmcloud.draconicadditions.mixins;

import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.items.equipment.IModularArmor;
import net.foxmcloud.draconicadditions.items.curios.ModularNecklace;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Necklaces implement IModularArmor, so DE's getArmor() could return a necklace from the curio slots
 * instead of a chestpiece worn in the body slot, leaving the player with "No Shield Installed".
 * A chestpiece in the curio slots now always wins; the necklace is only used when no other armor is worn.
 */
@Mixin(value = IModularArmor.class, remap = false)
public interface ArmorLookupPrefersChestpiece {
	@Inject(method = "getArmor", at = @At("HEAD"), cancellable = true)
	private static void preferChestpieceOverNecklace(LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
		if (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IModularArmor) return;
		ItemStack armor = EquipmentManager.findItem((e) -> e.getItem() instanceof IModularArmor && !(e.getItem() instanceof ModularNecklace), entity);
		if (!armor.isEmpty()) {
			cir.setReturnValue(armor);
		}
	}
}
