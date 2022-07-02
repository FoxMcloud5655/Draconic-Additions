package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.api.power.OPStorage;
import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.inventory.ContainerBCTile;
import com.brandon3055.brandonscore.inventory.TileItemStackHandler;
import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.IInteractTile;
import com.brandon3055.brandonscore.lib.IRSSwitchable;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.inventory.GUILayoutFactories;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileChaosLiquefier extends TileChaosHolderBase implements ITickableTileEntity, IChangeListener, IRSSwitchable, INamedContainerProvider, IInteractTile {

	private int chargeRate = 10000000;
	public int maxCharge = 200;

	public TileItemStackHandler itemHandler = new TileItemStackHandler(2);

	public final ManagedInt charge = register(new ManagedInt("charge", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedInt chargeTo = register(new ManagedInt("chargeTo", maxCharge, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public OPStorage opStorage = new OPStorage(2000000000, 20000000, 20000000);

	public TileChaosLiquefier() {
		super(DAContent.tileChaosLiquefier);
		capManager.setManaged("energy", CapabilityOP.OP, opStorage).saveBoth().syncContainer();
		capManager.setManaged("inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, itemHandler).saveBoth().syncTile();
		itemHandler.setStackValidator(this::isItemValidForSlot);
		setupPowerSlot(itemHandler, 1, opStorage, false);
		installIOTracker(opStorage);
	}

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide) {
			if (active.get()) {
				if (charge.get() >= 0 && charge.get() < chargeTo.get() - 1) {
					float beamPitch = (1.5F * charge.get() / maxCharge) + 0.5F;
					level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.beam, SoundCategory.BLOCKS, 0.2F, beamPitch, false);
					// charge.get() += 1;
				}
				else {
					level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.boom, SoundCategory.BLOCKS, 1.0F, 2.0F, false);
					// charge.get() = 0;
				}
			}
		}
		else {
			active.set(charge.get() > 0);
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (!stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.get() < getMaxChaos()) {
				int finalCharge = calcCharge(stack);
				if (finalCharge != chargeTo.get()) {
					chargeTo.set(finalCharge);
				}
				if (opStorage.getEnergyStored() >= chargeRate) {
					charge.add(1);
					opStorage.extractOP(chargeRate, false);
					if (charge.get() >= chargeTo.get()) {
						discharge();
					}
				}
				else if (charge.get() > 0) {
					charge.subtract(1);
				}
			}
			else if (charge.get() > 0) {
				charge.subtract(1);
			}
		}
	}

	public void discharge() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		chaos.add(calcChaos(stack));
		if (chaos.get() > getMaxChaos()) {
			chaos.set(getMaxChaos());
		}
		stack.shrink(1);
		if (stack.getCount() == 0) {
			stack = ItemStack.EMPTY;
		}
		charge.set(0);
	}

	public int calcChaos(ItemStack stack) {
		if (isItemValidForSlot(0, stack)) {
			switch (chaosID(stack.getItem())) {
			case 1:
				return 1300;
			case 2:
				return 150;
			case 3:
				return 17;
			case 4:
				return 2;
			case 5:
				return 1800;
			default:
				return 0;
			}
		}
		else return 0;
	}

	public int calcCharge(ItemStack stack) {
		if (isItemValidForSlot(0, stack)) {
			switch (chaosID(stack.getItem())) {
			case 1:
				return maxCharge;
			case 2:
				return maxCharge / 2;
			case 3:
				return maxCharge / 4;
			case 4:
				return maxCharge / 8;
			case 5:
				return maxCharge;
			default:
				return 0;
			}
		}
		else return 0;
	}

	public int chaosID(Item item) {
		if (item == DEContent.chaos_shard)
			return 1;
		else if (item == DEContent.chaos_frag_large)
			return 2;
		else if (item == DEContent.chaos_frag_medium)
			return 3;
		else if (item == DEContent.chaos_frag_small)
			return 4;
		else if (item == DAContent.chaosHeart)
			return 5;
		else return 0;
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0) {
			if (chaosID(stack.getItem()) > 0) {
				return true;
			}
			else return false;
		}
		else return EnergyUtils.canExtractEnergy(stack);
	}

	@Override
	public Container createMenu(int currentWindowIndex, PlayerInventory playerInventory, PlayerEntity player) {
		return new ContainerBCTile<>(DAContent.containerChaosLiquefier, currentWindowIndex, player.inventory, this, GUILayoutFactories.CHAOS_LIQUEFIER_LAYOUT);
	}

	@Override
	public boolean onBlockActivated(BlockState state, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player instanceof ServerPlayerEntity) {
			NetworkHooks.openGui((ServerPlayerEntity) player, this, worldPosition);
		}
		return true;
	}
}