package net.foxmcloud.draconicadditions.blocks.reactor.tileentity;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.utils.EnergyUtils;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class TileFakeReactorStabilizer extends TileFakeReactorComponent {

	private boolean disableCachedCore = false;

	public TileFakeReactorStabilizer(BlockPos pos, BlockState state) {
		super(DAContent.tileFakeReactorStabilizer.get(), pos, state);
		OPExtractor opExtractor = new OPExtractor();
		capManager.set(CapabilityOP.BLOCK, opExtractor);
		capManager.setCapSideValidator(opExtractor, face -> face == this.facing.get().getOpposite());
	}
	
	public static void register(RegisterCapabilitiesEvent event) {
        energyCapability(event, DAContent.tileFakeReactorStabilizer);
    }

	@Override
	public void tick() {
		disableCachedCore = true;
		super.tick();
		disableCachedCore = false;

		if (level.isClientSide) {
			return;
		}

		TileFakeReactorCore tile = getCachedCore();

		if (tile != null && tile.reactorState.get() == TileFakeReactorCore.ReactorState.RUNNING) {
			BlockEntity output = level.getBlockEntity(worldPosition.relative(facing.get().getOpposite()));
			if (output != null && EnergyUtils.canReceiveEnergy(output, facing.get())) {
				long sent = EnergyUtils.insertEnergy(output, tile.saturation.get(), facing.get(), true);
				tile.saturation.subtract(sent);
			}
		}
	}

	@Override
	public TileFakeReactorCore getCachedCore() {
		if (disableCachedCore) {
			return null;
		}
		return super.getCachedCore();
	}

	private class OPExtractor implements IOPStorage {
		public OPExtractor() {}

		@Override
		public long extractOP(long maxExtract, boolean simulate) {
			TileFakeReactorCore core = getCachedCore();
			if (core != null && core.reactorState.get() == TileFakeReactorCore.ReactorState.RUNNING) {
				long subtracted = Math.min(core.saturation.get(), maxExtract);
				if (!simulate) {
					core.saturation.subtract(subtracted);
				}
				return 0;
			}
			return 0;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return (int) extractOP(maxExtract, simulate);
		}

		@Override
		public long getMaxOPStored() {
			return Long.MAX_VALUE;
		}

		@Override
		public int getEnergyStored() {
			return 0;
		}

		@Override
		public int getMaxEnergyStored() {
			return Integer.MAX_VALUE;
		}

		@Override
		public long modifyEnergyStored(long amount) {
			return 0;
		}

		@Override
		public boolean canExtract() {
			return true;
		}

		@Override
		public boolean canReceive() {
			return false;
		}
	}
}
