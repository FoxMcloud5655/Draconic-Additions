package net.foxmcloud.draconicadditions.blocks.tileentity;

import java.util.ArrayList;

import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.inventory.TileItemStackHandler;
import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.IInteractTile;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.inventory.ChaosExtractorMenu;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.modules.DAModuleTypes;
import net.foxmcloud.draconicadditions.modules.entities.StableChaosEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class TileChaosExtractor extends TileChaosHolderBase implements IChangeListener, IInteractTile, MenuProvider {
	
	private int rfCost = 10000;

	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public TileChaosExtractor(BlockPos pos, BlockState state) {
		super(DAContent.tileChaosExtractor.get(), pos, state);
		itemHandler = new TileItemStackHandler(this, 2);
		opStorage = new ModularOPStorage(this, 1000000, 50000, 50000);
		capManager.setManaged("energy", CapabilityOP.BLOCK, opStorage).saveBoth().syncContainer();
		capManager.setInternalManaged("inventory", Capabilities.ItemHandler.BLOCK, itemHandler).saveBoth().syncTile();
		itemHandler.setStackValidator(this::isItemValidForSlot);
		setupPowerSlot(itemHandler, 1, opStorage, false);
		installIOTracker(opStorage);
	}
	
    public static void register(RegisterCapabilitiesEvent event) {
        energyCapability(event, DAContent.tileChaosExtractor);
        capability(event, DAContent.tileChaosExtractor, ItemHandler.BLOCK);
        capability(event, DAContent.tileChaosExtractor, DECapabilities.Host.BLOCK);
    }

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide) {
			if (active.get()) {
				level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.BEAM.get(), SoundSource.BLOCKS, 0.2F, 1.0F, false);
			}
		}
		else {
			ItemStack stack = itemHandler.getStackInSlot(0);
			boolean valid = isTileEnabled() && !stack.isEmpty() && opStorage.getEnergyStored() >= rfCost && isItemValidForSlot(0, stack) && chaos.get() <= getMaxChaos() - calcChaos(stack);
			active.set(valid);
			if (valid) {
				extractChaos();
			}
		}
	}

	public void extractChaos() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if (chaosID(stack.getItem()) == 5) {
			opStorage.extractOP(rfCost, false);
			chaos.add(calcChaos(stack));
			if (chaos.get() > getMaxChaos()) {
				chaos.set(getMaxChaos());
			}
			if (stack.getCount() > 1) {
				stack.shrink(1);
				Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), DEContent.DRAGON_HEART.get().getDefaultInstance());
			}
			else {
				stack = DEContent.DRAGON_HEART.get().getDefaultInstance();
			}
			itemHandler.setStackInSlot(0, stack);
		}
		else if (chaos.get() < getMaxChaos()) {
			StableChaosEntity entity = getFirstValidChaosEntity(stack);
			if (entity != null) {
				opStorage.extractOP(rfCost, false);
				chaos.add(-entity.modifyChaos(-1));
			}
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 1 ? EnergyUtils.isEnergyItem(stack) : chaosID(stack.getItem()) == 5 || getFirstValidChaosEntity(stack) != null;
	}
	
	private StableChaosEntity getFirstValidChaosEntity(ItemStack stack) {
		try (ModuleHost host = DECapabilities.getHost(stack)) {
            assert host != null;
			ArrayList<StableChaosEntity> entities = StableChaosEntity.getSortedListFromStream(host.getEntitiesByType(DAModuleTypes.STABLE_CHAOS));
			for (StableChaosEntity entity : entities) {
				if (entity.getChaos() > 0) {
					return entity;
				}
			}
		}
		return null;
	}

	@Override
	public AbstractContainerMenu createMenu(int currentWindowIndex, Inventory playerInventory, Player player) {
		return new ChaosExtractorMenu(currentWindowIndex, player.getInventory(), this);
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