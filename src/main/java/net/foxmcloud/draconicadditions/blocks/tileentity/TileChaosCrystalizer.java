package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.inventory.TileItemStackHandler;
import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.IInteractTile;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.network.BCoreNetwork;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.inventory.ChaosCrystalizerMenu;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;

public class TileChaosCrystalizer extends TileChaosHolderBase implements IInteractTile, MenuProvider {

	private int chargeRate = 10000;
	private ItemStack itemToMake = DEContent.CHAOS_FRAG_SMALL.get().getDefaultInstance();

	public final ManagedInt charge = register(new ManagedInt("charge", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedInt chargeTo = register(new ManagedInt("chargeTo", maxCharge, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public TileChaosCrystalizer(BlockPos pos, BlockState state) {
		super(DAContent.tileChaosCrystalizer.get(), pos, state);
		itemHandler = new TileItemStackHandler(this, 2);
		opStorage = new ModularOPStorage(this, 1000000, 50000, 50000);
		capManager.setManaged("energy", CapabilityOP.BLOCK, opStorage).saveBoth().syncContainer();
		capManager.setInternalManaged("inventory", Capabilities.ItemHandler.BLOCK, itemHandler).saveBoth().syncTile();
		itemHandler.setStackValidator(this::isItemValidForSlot);
		setupPowerSlot(itemHandler, 1, opStorage, false);
		installIOTracker(opStorage);
	}

	public static void register(RegisterCapabilitiesEvent event) {
		energyCapability(event, DAContent.tileChaosCrystalizer);
		capability(event, DAContent.tileChaosCrystalizer, ItemHandler.BLOCK);
		capability(event, DAContent.tileChaosCrystalizer, DECapabilities.Host.BLOCK);
	}

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide) {
			return;
		}
		else {
			ItemStack stack = itemHandler.getStackInSlot(0);
			boolean valid = isTileEnabled() && (stack.isEmpty() || stack.getCount() < stack.getItem().getMaxStackSize(stack)) && chaos.get() >= calcChaos(itemToMake);
			active.set(valid);
			if (valid) {
				int finalCharge = calcCharge(itemToMake);
				if (finalCharge != chargeTo.get()) {
					chargeTo.set(finalCharge);
				}
				if (opStorage.getEnergyStored() >= chargeRate) {
					charge.add(1);
					opStorage.extractOP(chargeRate, false);
					if (charge.get() >= chargeTo.get()) {
						discharge();
						BCoreNetwork.sendSound(level, worldPosition, DESounds.BOOM.get(), SoundSource.BLOCKS, 1.0F, 2.0F, false);
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
		chaos.subtract(calcChaos(itemToMake));
		if (stack.isEmpty()) {
			stack = itemToMake.copy();
		}
		else if (stack.getItem().equals(itemToMake.getItem())) {
			stack.grow(1);
		}
		charge.set(0);
		itemHandler.setStackInSlot(0, stack);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 1 ? EnergyUtils.isEnergyItem(stack) : false;
	}

	@Override
	public AbstractContainerMenu createMenu(int currentWindowIndex, Inventory playerInventory, Player player) {
		return new ChaosCrystalizerMenu(currentWindowIndex, player.getInventory(), this);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Player player, BlockHitResult hit) {
		if (player instanceof ServerPlayer) {
			player.openMenu(this, worldPosition);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}
}