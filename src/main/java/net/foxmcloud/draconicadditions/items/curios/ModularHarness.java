package net.foxmcloud.draconicadditions.items.curios;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.api.IInvCharge;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.PropertyProvider;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty.IntegerFormatter;
import com.brandon3055.draconicevolution.api.config.IntegerProperty;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.TechProperties;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;

import net.foxmcloud.draconicadditions.CommonMethods.BlockStorage;
import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.items.ModularEnergyItem;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.foxmcloud.draconicadditions.modules.TickAccelData;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ModularHarness extends ModularEnergyItem implements IInvCharge {
	public static final ModuleCategory HARNESS = new ModuleCategory();
	private static final String receive = "receive_energy_from_machine";
	private static final String tickAccelSpeed = "tick_accel_speed";
	public ModularHarness(TechProperties props) {
		super(props);
	}

	@Override
	public boolean canCharge(ItemStack stack, LivingEntity player, boolean isHeld) {
		return !isReceiving(stack);
	}

	@Override
	public void handleTick(ItemStack stack, LivingEntity entity, @Nullable EquipmentSlot slot, boolean inEquipModSlot) {
		boolean validEquipSlot = slot != null ? false : inEquipModSlot;
		if ((!validEquipSlot && !DAConfig.harnessTickOutOfCuriosSlot) || !hasAttachedBlockEntity(stack, entity.level)) {
			return;
		}
		Level world = entity.level;
		BlockPos pos = entity.blockPosition().above();
		BlockStorage oldBlock = new BlockStorage(world, pos, false);
		BlockEntity tile = placeAndGetBlockEntity(world, pos, entity.getRotationVector(), stack);
		if (tile == null) {
			return;
		}
		stack.getCapability(DECapabilities.OP_STORAGE).ifPresent(e -> {
			IOPStorageModifiable storage = (IOPStorageModifiable)e;
			IOPStorage teStorage = EnergyUtils.getStorage(tile, null);
			if (teStorage != null) {
				if (isReceiving(stack)) {
					storage.receiveOP(EnergyUtils.extractEnergy(tile, Math.min(storage.receiveOP(storage.maxReceive(), true), teStorage.getOPStored()), null, false), false);
					DEContent.capacitor_chaotic.handleTick(stack, entity, slot, inEquipModSlot);
				}
				else if (tile != null) {
					storage.extractOP(EnergyUtils.insertEnergy(tile, storage.extractOP(storage.maxExtract(), true), null, false), false);
				}
			}
			if (tile instanceof TickingBlockEntity) {
				TickingBlockEntity tickingTile = (TickingBlockEntity)tile;
				int ticksToProcess = getCurrentTickSpeed(stack) - 1;
				int rfCost = getRFCostForTicks(ticksToProcess);
				if (storage.extractOP(rfCost, true) >= rfCost) {
					storage.extractOP(rfCost, false);
					for (int i = 0; i < ticksToProcess; i++) {
						tickingTile.tick();
					}
				}
			}
		});
		if (tile instanceof TickingBlockEntity) {
			((TickingBlockEntity)tile).tick();
		}
		storeBlockEntity(world, pos, stack, entity, false);
		oldBlock.restoreBlock(null, false);
	}

	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, techLevel.index, 2, "harness", removeInvalidModules);
		host.addCategories(ModuleCategory.ENERGY, HARNESS);
		host.addPropertyBuilder(props -> {
			props.add(new BooleanProperty(receive, false));
			props.add(new BooleanProperty("charge_held_item", false));
			props.add(new BooleanProperty("charge_armor", false));
			props.add(new BooleanProperty("charge_hot_bar", false));
			props.add(new BooleanProperty("charge_main", false));
			if (EquipmentManager.equipModLoaded()) {
				props.add(new BooleanProperty("charge_" + EquipmentManager.equipModID(), false));
			}
			TickAccelData speed = host.getModuleData(ModuleTypes.TICK_ACCEL);
			if (speed != null) {
				Supplier<Integer> speedGetter = () -> {
					TickAccelData data = host.getModuleData(ModuleTypes.TICK_ACCEL);
					return data == null ? 0 : data.getSpeed();
				};
				props.add(new IntegerProperty(tickAccelSpeed, speedGetter.get()).min(0).max(speedGetter).setFormatter(IntegerFormatter.RAW));
			}
		});
		return host;
	}

	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		long capacity = (long)(EquipCfg.getBaseEnergy(techLevel) * DAConfig.harnessCapacityMultiplier);
		return new ModularOPStorage(host, capacity, capacity / 64, true);
	}

	@Override
	public boolean canEquip(LivingEntity livingEntity, String identifier) {
		return identifier.equals(SlotTypePreset.BACK.getIdentifier());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		String name = getAttachedName(stack);
		if (name != null && name != "") {
			tooltip.add(new TranslatableComponent("info.da.modular_harness.storedBlock").withStyle(ChatFormatting.GOLD).append(new TextComponent(getAttachedName(stack)).withStyle(ChatFormatting.GRAY)));
			String rf = Utils.formatNumber(getRFCostForTicks(getCurrentTickSpeed(stack)));
			tooltip.add(new TranslatableComponent("info.da.opCost", rf).withStyle(ChatFormatting.GRAY));
		}
		EnergyUtils.addEnergyInfo(stack, tooltip);
	}

	public static int getCurrentTickSpeed(ItemStack stack) {
		PropertyProvider props = stack.getCapability(DECapabilities.PROPERTY_PROVIDER_CAPABILITY).orElse(null);
		int ticks = 1 + (props != null && props.getInt(tickAccelSpeed) != null ? props.getInt(tickAccelSpeed).getValue() : 0);
		return ticks;
	}

	public static int getRFCostForTicks(int ticks) {
		return ticks > 1 ? (int)Math.pow(400, ticks * 0.25 + 0.25) : 0;
	}

	public static boolean storeBlockEntity(Level world, BlockPos pos, ItemStack stack, LivingEntity entity, boolean removeBlock) {
		if (world instanceof ServerLevel) { //TODO: Review if this is needed.
			if (world.getBlockEntity(pos) != null) {
				BlockStorage block = new BlockStorage(world, pos, removeBlock);
				block.storeBlockInTag(stack.getOrCreateTag());
			}
			else {
				entity.sendMessage(new TranslatableComponent("info.da.modular_harness.cantmove"), Util.NIL_UUID);
				return false;
			}
		}
		return true;
	}

	public static BlockEntity placeAndGetBlockEntity(Level world, BlockPos pos, @Nullable Vec2 rotation, ItemStack stack) {
		if (BlockStorage.restoreBlockFromTag(world, pos, rotation, stack.getTag(), false, false)) {
			return world.getBlockEntity(pos);
		}
		return null;
	}


	public static String getAttachedName(ItemStack stack) {
		if (stack.getTag() != null) {
			Block block = BlockStorage.getBlockFromTag(stack.getTag());
			return block == null || block == Blocks.AIR ? null : block.getName().getString();
		}
		return null;
	}

	public static boolean hasAttachedBlockEntity(ItemStack stack, Level world) {
		return stack.getOrCreateTag().contains("storedBlockState");
	}

	public static boolean isReceiving(ItemStack stack) {
		BooleanProperty isReceiving = new BooleanProperty(null, false);
		stack.getCapability(DECapabilities.PROPERTY_PROVIDER_CAPABILITY).ifPresent(props -> {
			isReceiving.setValue(props.getBool(receive).getValue());
		});
		return isReceiving.getValue();
	}
}
