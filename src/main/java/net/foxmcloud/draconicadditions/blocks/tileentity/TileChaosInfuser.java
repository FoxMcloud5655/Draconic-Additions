package net.foxmcloud.draconicadditions.blocks.tileentity;

import java.util.ArrayList;
import java.util.stream.Stream;

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
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.init.DEContent;

import net.foxmcloud.draconicadditions.inventory.ChaosInfuserMenu;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.modules.DAModuleTypes;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.foxmcloud.draconicadditions.modules.entities.StableChaosEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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

public class TileChaosInfuser extends TileChaosHolderBase implements IChangeListener, IInteractTile, MenuProvider {

	private int chargeRate = 20000;
	private int rateMultiplier = 2;

	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public TileChaosInfuser(BlockPos pos, BlockState state) {
		super(DAContent.tileChaosInfuser.get(), pos, state);
		itemHandler = new TileItemStackHandler(this, 2);
		opStorage = new ModularOPStorage(this, 10000000, 100000, 100000);
		capManager.setManaged("energy", CapabilityOP.BLOCK, opStorage).saveBoth().syncContainer();
		capManager.setInternalManaged("inventory", Capabilities.ItemHandler.BLOCK, itemHandler).saveBoth().syncTile();
		itemHandler.setStackValidator(this::isItemValidForSlot);
		setupPowerSlot(itemHandler, 1, opStorage, false);
		installIOTracker(opStorage);
	}
	
    public static void register(RegisterCapabilitiesEvent event) {
        energyCapability(event, DAContent.tileChaosInfuser);
        capability(event, DAContent.tileChaosInfuser, ItemHandler.BLOCK);
        capability(event, DAContent.tileChaosInfuser, DECapabilities.Host.BLOCK);
    }

	@Override
	public void tick() {
		super.tick();
		if (level.isClientSide()) {
			if (active.get()) {
				float beamPitch = (float)(0.5F + (Math.random() * 0.1F));
				level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.BEAM.get(), SoundSource.BLOCKS, 0.2F, beamPitch, false);
			}
		}
		else {
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (isTileEnabled() && !stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.get() > 0) {
				if (isItemValidStorage(stack) && chaos.get() >= 20000 * stack.getCount()) {
					chaos.subtract(20000 * stack.getCount());
					ItemStack heart = DAContent.chaosHeart.get().getDefaultInstance();
					heart.setCount(stack.getCount());
					itemHandler.setStackInSlot(0, heart);
					active.set(false);
					return;
				}
				else try (ModuleHost host = DECapabilities.getHost(stack)) {
                    assert host != null;
                    Stream<ModuleEntity<?>> chaosEntities = host.getEntitiesByType(DAModuleTypes.STABLE_CHAOS);
    				ArrayList<StableChaosEntity> sortedChaosEntities = StableChaosEntity.getSortedListFromStream(chaosEntities);
    				if (sortedChaosEntities.size() == 0) {
    					active.set(false);
    					return;
    				}
    				int remainingChaosToTransfer = Math.min(rateMultiplier, chaos.get());
    				for (StableChaosEntity ce : sortedChaosEntities) {
    					StableChaosData data = (StableChaosData)ce.getModule().getData();
    					if (ce.getChaos() < data.getMaxChaos()) {
    						active.set(true);
    						long opRemoved = opStorage.extractOP(chargeRate * remainingChaosToTransfer, true);
    						int chaosAdded = ce.modifyChaos((int)(opRemoved / chargeRate));
    						opStorage.extractOP(chargeRate * chaosAdded, false);
    						remainingChaosToTransfer -= chaosAdded;
    						if (remainingChaosToTransfer == 0) {
    							break;
    						}
    					}
    				}
    				chaos.subtract(Math.min(rateMultiplier, chaos.get()) - remainingChaosToTransfer);
    				if (remainingChaosToTransfer == rateMultiplier) {
    					active.set(false);
    				}
				}
			}
			else active.set(false);
		}
	}
	
	private boolean isItemValidStorage(ItemStack stack) {
		return stack.getItem() == DEContent.DRAGON_HEART.get();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 1) {
			return EnergyUtils.isEnergyItem(stack);
		}
		else {
			ModuleHost host = DECapabilities.getHost(stack);
			if (host != null) {
				StableChaosData data = host.getModuleData(DAModuleTypes.STABLE_CHAOS);
				return data != null ? data.getMaxChaos() > 0 : false;
			}
			else return isItemValidStorage(stack);
		}
	}

	@Override
	public AbstractContainerMenu createMenu(int currentWindowIndex, Inventory playerInventory, Player player) {
		return new ChaosInfuserMenu(currentWindowIndex, player.getInventory(), this);
	}

    @Override
    public InteractionResult useWithoutItem(BlockState state, Player player, BlockHitResult hit) {
        if (player instanceof ServerPlayer) {
            player.openMenu(this, worldPosition);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

	/*
	public void spawnParticles(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (stateIn.getActualState(worldIn, pos).getValue(ACTIVE)) {
			EnumFacing enumfacing = stateIn.getValue(FACING);
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY() + 0.4 + rand.nextDouble() * 0.2;
			double d2 = pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.4D - 0.2D;

			switch (enumfacing) {
			case WEST:
				worldIn.spawnParticle(EnumParticleTypes.SPELL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.SPELL_INSTANT, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case EAST:
				worldIn.spawnParticle(EnumParticleTypes.SPELL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.SPELL_INSTANT, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case NORTH:
				worldIn.spawnParticle(EnumParticleTypes.SPELL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.SPELL_INSTANT, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
				break;
			case SOUTH:
				worldIn.spawnParticle(EnumParticleTypes.SPELL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
				worldIn.spawnParticle(EnumParticleTypes.SPELL_INSTANT, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
	 */
}
