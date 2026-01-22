package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.inventory.TileItemStackHandler;
import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.IInteractTile;
import com.brandon3055.brandonscore.lib.IRSSwitchable;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.network.BCoreNetwork;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.inventory.ChaosLiquifierMenu;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;

public class TileChaosLiquifier extends TileChaosHolderBase implements IChangeListener, IInteractTile, MenuProvider {

	private int chargeRate = 1000000;

	public final ManagedInt charge = register(new ManagedInt("charge", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedInt chargeTo = register(new ManagedInt("chargeTo", maxCharge, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public TileChaosLiquifier(BlockPos pos, BlockState state) {
		super(DAContent.tileChaosLiquifier.get(), pos, state);
		itemHandler = new TileItemStackHandler(this, 2);
		opStorage = new ModularOPStorage(this, 200000000, 2000000, 2000000);
		capManager.setManaged("energy", CapabilityOP.BLOCK, opStorage).saveBoth().syncContainer();
		capManager.setInternalManaged("inventory", Capabilities.ItemHandler.BLOCK, itemHandler).saveBoth().syncTile();
		itemHandler.setStackValidator(this::isItemValidForSlot);
		setupPowerSlot(itemHandler, 1, opStorage, false);
		installIOTracker(opStorage);
	}
	
    public static void register(RegisterCapabilitiesEvent event) {
        energyCapability(event, DAContent.tileChaosLiquifier);
        capability(event, DAContent.tileChaosLiquifier, ItemHandler.BLOCK);
        capability(event, DAContent.tileChaosLiquifier, DECapabilities.Host.BLOCK);
    }

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide) {
			if (active.get()) {
				if (charge.get() >= 0 && charge.get() < chargeTo.get() - 1) {
					float beamPitch = (1.5F * charge.get() / maxCharge) + 0.5F;
					level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.BEAM.get(), SoundSource.BLOCKS, 0.2F, beamPitch, false);
				}
			}
		}
		else {
			active.set(charge.get() > 0);
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (isTileEnabled() && !stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.get() <= getMaxChaos() - calcChaos(stack)) {
				int finalCharge = calcCharge(stack);
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
		chaos.add(calcChaos(stack));
		if (chaos.get() > getMaxChaos()) {
			chaos.set(getMaxChaos());
		}
		stack.shrink(1);
		if (stack.getCount() == 0) {
			stack = ItemStack.EMPTY;
		}
		charge.set(0);
		itemHandler.setStackInSlot(0, stack);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 1 ? EnergyUtils.isEnergyItem(stack) : chaosID(stack.getItem()) > 0 && chaosID(stack.getItem()) < 5;
	}

	@Override
	public AbstractContainerMenu createMenu(int currentWindowIndex, Inventory playerInventory, Player player) {
		return new ChaosLiquifierMenu(currentWindowIndex, player.getInventory(), this);
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