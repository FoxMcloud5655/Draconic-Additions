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
import com.brandon3055.brandonscore.utils.TargetPos;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.api.IInvCharge;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.PropertyProvider;
import com.brandon3055.draconicevolution.api.config.BooleanProperty;
import com.brandon3055.draconicevolution.api.config.ConfigProperty.IntegerFormatter;
import com.brandon3055.draconicevolution.api.config.IntegerProperty;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleType;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.items.tools.DraconiumCapacitor;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.items.ModularEnergyItem;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.foxmcloud.draconicadditions.modules.TickAccelData;
import net.foxmcloud.draconicadditions.world.DADimension;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ModularHarness extends ModularEnergyItem implements IInvCharge {
	public static final ModuleCategory HARNESS = new ModuleCategory();
	private static final String receive = "receive_energy_from_machine";
	private static final String tickAccelSpeed = "tick_accel_speed";
	public ModularHarness(TechPropBuilder props) {
		super(props);
	}

	@Override
	public boolean canCharge(ItemStack stack, LivingEntity player, boolean isHeld) {
		return !isReceiving(stack);
	}

	@Override
	public void handleTick(ItemStack stack, LivingEntity entity, @Nullable EquipmentSlotType slot, boolean inEquipModSlot) {
		if (!DAConfig.harnessTickOutOfCuriosSlot && !inEquipModSlot) {
			return;
		}
		TileEntity tile = getAttachedTileEntity(stack, entity.level);
		if (tile != null) {
			stack.getCapability(DECapabilities.OP_STORAGE).ifPresent(e -> {
				IOPStorageModifiable storage = (IOPStorageModifiable)e;
				IOPStorage teStorage = EnergyUtils.getStorage((TileEntity)tile, null);
				if (teStorage != null) {
					if (isReceiving(stack)) {
						storage.receiveOP(EnergyUtils.extractEnergy((TileEntity)tile, Math.min(storage.receiveOP(storage.maxReceive(), true), teStorage.getOPStored()), null, false), false);
						DEContent.capacitor_chaotic.handleTick(stack, entity, slot, inEquipModSlot);
					}
					else if (tile != null) {
						storage.extractOP(EnergyUtils.insertEnergy((TileEntity)tile, storage.extractOP(storage.maxExtract(), true), null, false), false);
					}
				}
				if (tile instanceof ITickableTileEntity) {
					ITickableTileEntity tickingTile = (ITickableTileEntity)tile;
					int ticksToProcess = getCurrentTickSpeed(stack);
					int rfCost = getRFCostForTicks(ticksToProcess);
					if (storage.extractOP(rfCost, true) >= rfCost) {
						storage.extractOP(rfCost, false);
						for (int i = 0; i < ticksToProcess; i++) {
							tickingTile.tick();
						}
					}
				}
			});
		}
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
			tooltip.add(new TranslationTextComponent("info.da.modular_harness.stored_block").withStyle(TextFormatting.GOLD).append(new StringTextComponent(getAttachedName(stack)).withStyle(TextFormatting.GRAY)));
            String rf = Utils.formatNumber(getRFCostForTicks(getCurrentTickSpeed(stack)));
			tooltip.add(new TranslationTextComponent("info.da.modular_harness.op_cost")
            	.append(new TranslationTextComponent("info.da.modular_harness.op_cost.value", rf))
            	.withStyle(GRAY));
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

	public static TargetPos getPos(ItemStack stack) {
		return DEContent.dislocator.getTargetPos(stack, null);
	}

	public static void clearPos(ItemStack stack) {
		CompoundNBT tag = stack.getTag();
		tag.remove("target");
		tag.remove("storedBlockName");
	}

	private static boolean swapBlockPos(World targetWorld, BlockPos targetPos, World destinationWorld, BlockPos destinationPos) {
		TileEntity tile = targetWorld.getBlockEntity(targetPos);
		CompoundNBT nbt = tile.serializeNBT();
		destinationWorld.setBlock(destinationPos, tile.getBlockState(), 0);
		if (destinationWorld.getBlockState(destinationPos) == Blocks.AIR.defaultBlockState()) {
			return false;
		}
		TileEntity newTile = destinationWorld.getBlockEntity(destinationPos);
		newTile.deserializeNBT(nbt);
		newTile.setLevelAndPosition(destinationWorld, destinationPos);
		targetWorld.removeBlockEntity(targetPos);
		targetWorld.removeBlock(targetPos, false);
		return true;
	}

	public static boolean storeTileEntity(ItemStack stack, LivingEntity entity, World world, BlockPos pos) {
		if (hasAttachedTileEntity(stack, world)) {
			DraconicAdditions.logger.warn("Entity " + entity.getName() + 
					" attempted to store a TileEntity at pos " + pos.toShortString() + 
					" when their " + stack.getItem().getRegistryName() + 
					" already has a " + getAttachedTileEntity(stack, world).getBlockState().getBlock().getRegistryName() + 
					" stored!");
			return false;
		}
		if (world instanceof ServerWorld) {
			ServerWorld harnessDim = world.getServer().getLevel(DADimension.HARNESS_DIM);
			TargetPos destination = new TargetPos(DADimension.findEmptyBlock(harnessDim), DADimension.HARNESS_DIM);
			if (swapBlockPos(world, pos, harnessDim, destination.getPos().pos())) {
				DEContent.dislocator.setLocation(stack, destination);
				stack.getTag().putString("storedBlockName", getAttachedBlock(stack, world).getName().getString());
			}
			else {
				entity.sendMessage(new TranslationTextComponent("info.da.modular_harness.cantmove"), Util.NIL_UUID);
				return false;
			}
		}
		return true;
	}

	public static boolean placeTileEntity(World world, BlockPos pos, ItemStack stack) {
		TileEntity tile = getAttachedTileEntity(stack, world);
		if (tile != null) {
			if (swapBlockPos(tile.getLevel(), tile.getBlockPos(), world, pos)) {
				clearPos(stack);
				return true;
			}
		}
		return false;
	}

	public static TileEntity getAttachedTileEntity(ItemStack stack, World world) {
		if (!world.isClientSide) {
			TargetPos tpos = getPos(stack);
			if (tpos != null) {
				return world.getServer().getLevel(tpos.getDimension()).getBlockEntity(tpos.getPos().pos());
			}
		}
		return null;
	}

	public static String getAttachedName(ItemStack stack) {
		return stack.getTag() != null ? stack.getTag().getString("storedBlockName") : null;
	}

	public static Block getAttachedBlock(ItemStack stack, World world) {
		TileEntity tile = getAttachedTileEntity(stack, world);
		if (tile != null) {
			return tile.getBlockState().getBlock();
		}
		return null;
	}

	public static boolean hasAttachedTileEntity(ItemStack stack, World world) {
		return getAttachedTileEntity(stack, world) != null;
	}

	public static boolean isReceiving(ItemStack stack) {
		BooleanProperty isReceiving = new BooleanProperty(null, false);
		stack.getCapability(DECapabilities.PROPERTY_PROVIDER_CAPABILITY).ifPresent(props -> {
			isReceiving.setValue(props.getBool(receive).getValue());
		});
		return isReceiving.getValue();
	}
	
	
}
