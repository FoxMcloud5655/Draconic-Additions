package net.foxmcloud.draconicadditions.items.armor;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.LimitedModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;

import net.foxmcloud.draconicadditions.DAConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class InfusedPotatoArmor extends ArmorItem implements IModularItem, IDEEquipment {

	protected static final TechLevel techLevel = TechLevel.WYVERN;
	protected Type slotType;

	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmor(Properties props, Type slotType) {
		super(ArmorMaterials.LEATHER, slotType, props);
		this.slotType = slotType;
	}

	@Override
	public TechLevel getTechLevel() {
		return techLevel;
	}

	@Override
	public @NotNull ModuleHostImpl instantiateHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, slotType == Type.CHESTPLATE ? 2 : 1, slotType == Type.CHESTPLATE ? 2 : 1, "infused", false);
		if (slotType == Type.CHESTPLATE) {
			host.addModule(DEModules.WYVERN_SHIELD_CONTROL.get().createEntity(), new LimitedModuleContext(ItemStack.EMPTY, null, null, null));
		}
		else {
			host.addModule(DEModules.WYVERN_SHIELD_RECOVERY.get().createEntity(), new LimitedModuleContext(ItemStack.EMPTY, null, null, null));
		}
		host.setRemoveCheck((a,b) -> false);
		return host;
	}

	@Override
	public @NotNull ModularOPStorage instantiateOPStorage(ItemStack stack, Supplier<ModuleHost> host) {
		// Redstone Dust generates 20 RF/t for 30 seconds. 36 Redstone Dust is used in the recipe, so 36 * 20 * 20 * 30 = 432,000 RF.
		long capacity = (long)(432000 * DAConfig.infusedCapacityMultiplier);
		ModularOPStorage storage = new ModularOPStorage(host, capacity, 0, capacity / 64);
		storage.setExtractOnly();
		storage.modifyEnergyStored(capacity);
		return storage;
	}

	@Override
	public void handleTick(ModuleHost host, ItemStack stack, LivingEntity entity, @Nullable EquipmentSlot slot, boolean inEquipModSlot) {
		IModularItem.super.handleTick(host, stack, entity, slot, inEquipModSlot);
		IOPStorage energy = stack.getCapability(CapabilityOP.ITEM);
		if (energy != null) {
			if (energy.getOPStored() <= 1) {
				if (entity instanceof ServerPlayer player) {
					breakArmor(player, stack);
				}
			}
			else if (!(stack.getItem() instanceof InfusedPotatoArmorChest)){
				ItemStack chest = InfusedPotatoArmorChest.getChestpiece(entity);
				if (!chest.isEmpty()) {
					IOPStorage chestEnergy = chest.getCapability(CapabilityOP.ITEM);
					if (chestEnergy != null) {
						long energyDiff = energy.getOPStored() - chestEnergy.getOPStored();
						if (energyDiff > 1) {
							chestEnergy.modifyEnergyStored(energyDiff / 2);
							energy.modifyEnergyStored(-energyDiff / 2);
							if (energy.getOPStored() < 1000 && entity instanceof ServerPlayer player) {
								breakArmor(player, stack);
							}
						}
					}
				}
			}
		}
	}

	protected void breakArmor(ServerPlayer player, ItemStack stack) {
		player.sendSystemMessage(Component.translatable("info.da.infusedArmor.break", stack.getHoverName().getString()));
		ItemStack draconiumDust = DEContent.DUST_DRACONIUM.get().getDefaultInstance();
		draconiumDust.setCount(4);
		player.getInventory().add(draconiumDust);
		player.getInventory().removeItem(stack);
	}

	@Override
	@OnlyIn (Dist.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		EnergyUtils.addEnergyInfo(stack, tooltip);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return damageBarVisible(stack);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return damageBarWidth(stack);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return damageBarColour(stack);
	}
}
