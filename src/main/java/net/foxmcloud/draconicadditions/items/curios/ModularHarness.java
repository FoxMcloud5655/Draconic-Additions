package net.foxmcloud.draconicadditions.items.curios;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;
import static net.minecraft.util.text.TextFormatting.GRAY;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
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
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;

import net.foxmcloud.draconicadditions.CommonMethods.BlockStorage;
import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.items.ModularEnergyItem;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.foxmcloud.draconicadditions.modules.TickAccelData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants.BlockFlags;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ModularHarness extends ModularEnergyItem implements IInvCharge {
	public static final ModuleCategory HARNESS = new ModuleCategory();
	private static final String receive = "receive_energy_from_machine";
	private static final String tickAccelSpeed = "tick_accel_speed";
	private static final int blockFlags = BlockFlags.DEFAULT_AND_RERENDER;
	public ModularHarness(TechPropBuilder props) {
		super(props);
	}

	@Override
	public boolean canCharge(ItemStack stack, LivingEntity player, boolean isHeld) {
		return !isReceiving(stack);
	}

	@Override
	public void handleTick(ItemStack stack, LivingEntity entity, @Nullable EquipmentSlotType slot, boolean inEquipModSlot) {
		boolean validEquipSlot = slot != null ? false : inEquipModSlot;
		if ((!validEquipSlot && !DAConfig.harnessTickOutOfCuriosSlot) || !hasAttachedTileEntity(stack, entity.level)) {
			return;
		}
		World world = entity.level;
		BlockPos pos = entity.blockPosition().above();
		BlockStorage oldBlock = new BlockStorage(world, pos, false);
		TileEntity tile = placeAndGetTileEntity(world, pos, entity.getRotationVector(), stack, false);
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
			if (tile instanceof ITickableTileEntity) {
				ITickableTileEntity tickingTile = (ITickableTileEntity)tile;
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
		if (tile instanceof ITickableTileEntity) {
			((ITickableTileEntity)tile).tick();
		}
		storeTileEntity(world, pos, stack, entity);
		oldBlock.restoreBlock(null);
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
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		if (getAttachedName(stack) != null && getAttachedName(stack) != "") {
			tooltip.add(new TranslationTextComponent("info.da.modular_harness.storedBlock").withStyle(TextFormatting.GOLD).append(new StringTextComponent(getAttachedName(stack)).withStyle(TextFormatting.GRAY)));
			String rf = Utils.formatNumber(getRFCostForTicks(getCurrentTickSpeed(stack)));
			tooltip.add(new TranslationTextComponent("info.da.opCost", rf).withStyle(GRAY));
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

	public static void clearData(ItemStack stack) {
		if (stack.getTag() != null) {
			CompoundNBT tag = stack.getTag();
			tag.remove("storedTileEntity");
			tag.remove("storedBlockState");
			tag.remove("storedBlockName");
		}
	}

	public static boolean storeTileEntity(World world, BlockPos pos, ItemStack stack, LivingEntity entity) {
		if (world instanceof ServerWorld) {
			if (world.getBlockEntity(pos) != null) {
				BlockStorage block = new BlockStorage(world, pos, true);
				block.storeBlockInTag(stack.getOrCreateTag());
				stack.getTag().putString("storedBlockName", block.blockState.getBlock().getName().getString());
			}
			else {
				entity.sendMessage(new TranslationTextComponent("info.da.modular_harness.cantmove"), Util.NIL_UUID);
				return false;
			}
		}
		return true;
	}

	public static TileEntity placeAndGetTileEntity(World world, BlockPos pos, @Nullable Vector2f rotation, ItemStack stack, boolean clearData) {
		if (BlockStorage.restoreBlockFromTag(world, pos, rotation, stack.getTag(), false)) {
			if (clearData) {
				clearData(stack);
			}
			return world.getBlockEntity(pos);
		}
		return null;
	}


	public static String getAttachedName(ItemStack stack) {
		return stack.getTag() != null ? stack.getTag().getString("storedBlockName") : null;
	}

	public static boolean hasAttachedTileEntity(ItemStack stack, World world) {
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
