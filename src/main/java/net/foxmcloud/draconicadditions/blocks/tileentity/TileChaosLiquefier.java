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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class TileChaosLiquefier extends TileChaosHolderBase implements IChangeListener, IRSSwitchable, IInteractTile, MenuProvider {

	private int chargeRate = 10000000;
	public int maxCharge = 200;

	public final ManagedInt charge = register(new ManagedInt("charge", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedInt chargeTo = register(new ManagedInt("chargeTo", maxCharge, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public TileChaosLiquefier(BlockPos pos, BlockState state) {
		super(DAContent.tileChaosLiquefier, pos, state);
		itemHandler = new TileItemStackHandler(2);
		opStorage = new OPStorage(2000000000, 20000000, 20000000);
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
					level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.beam, SoundSource.BLOCKS, 0.2F, beamPitch, false);
					// charge.get() += 1;
				}
				else {
					level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.boom, SoundSource.BLOCKS, 1.0F, 2.0F, false);
					// charge.get() = 0;
				}
			}
		}
		else {
			active.set(charge.get() > 0);
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (!stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.get() <= getMaxChaos() - calcChaos(stack) && isTileEnabled()) {
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
	public AbstractContainerMenu createMenu(int currentWindowIndex, Inventory playerInventory, Player player) {
		return new ContainerBCTile<>(DAContent.containerChaosLiquefier, currentWindowIndex, player.getInventory(), this, GUILayoutFactories.CHAOS_LIQUEFIER_LAYOUT);
	}

	@Override
	public boolean onBlockActivated(BlockState state, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (player instanceof ServerPlayer) {
			NetworkHooks.openGui((ServerPlayer) player, this, worldPosition);
		}
		return true;
	}
}