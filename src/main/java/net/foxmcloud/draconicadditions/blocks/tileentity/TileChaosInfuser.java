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
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.handlers.DESounds;

import net.foxmcloud.draconicadditions.inventory.GUILayoutFactories;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.foxmcloud.draconicadditions.modules.StableChaosData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class TileChaosInfuser extends TileChaosHolderBase implements IChangeListener, IRSSwitchable, IInteractTile, MenuProvider {

	private int chargeRate = 1000000;
	public int maxCharge = 200;

	public final ManagedBool active = register(new ManagedBool("active", false, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public TileChaosInfuser(BlockPos pos, BlockState state) {
		super(DAContent.tileChaosInfuser, pos, state);
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
		if (level.isClientSide()) {
			if (active.get()) {
				float beamPitch = (float)(0.5F + (Math.random() * 0.1F));
				level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY(), worldPosition.getZ() + 0.5D, DESounds.beam, SoundSource.BLOCKS, 0.2F, beamPitch, false);
			}
		}
		else {
			ItemStack stack = itemHandler.getStackInSlot(0);
			if (!stack.isEmpty() && isItemValidForSlot(0, stack) && chaos.get() > 0 && opStorage.getOPStored() >= chargeRate) {
				ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElse(null);
				StableChaosData data = host.getModuleData(ModuleTypes.STABLE_CHAOS);
				if (data.getChaos() < data.getMaxChaos()) {
					active.set(true);
					opStorage.extractOP(chargeRate, false);
					chaos.add(data.addChaos(1) - 1);
				}
				else active.set(false);
			}
			else active.set(false);
		}
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElse(null);
		if (host != null) {
			StableChaosData data = host.getModuleData(ModuleTypes.STABLE_CHAOS);
			return data != null ? data.getMaxChaos() > 0 : false;
		}
		return false;
	}

	@Override
	public AbstractContainerMenu createMenu(int currentWindowIndex, Inventory playerInventory, Player player) {
		return new ContainerBCTile<>(DAContent.containerChaosInfuser, currentWindowIndex, player.getInventory(), this, GUILayoutFactories.CHAOS_INFUSER_LAYOUT);
	}

	@Override
	public boolean onBlockActivated(BlockState state, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (player instanceof ServerPlayer) {
			NetworkHooks.openGui((ServerPlayer) player, this, worldPosition);
		}
		return true;
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
