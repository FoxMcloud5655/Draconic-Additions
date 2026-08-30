package net.foxmcloud.draconicadditions.blocks.reactor.tileentity;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.capability.CapabilityOP;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class TileFakeReactorInjector extends TileFakeReactorComponent {

	public TileFakeReactorInjector(BlockPos pos, BlockState state) {
		super(DAContent.tileFakeReactorInjector.get(), pos, state);
		OPInjector opInjector = new OPInjector(this);
		capManager.set(CapabilityOP.BLOCK, opInjector);
		capManager.setCapSideValidator(opInjector, face -> face == this.facing.get().getOpposite());
	}

	public static void register(RegisterCapabilitiesEvent event) {
		energyCapability(event, DAContent.tileFakeReactorInjector);
	}

	private static class OPInjector implements IOPStorage {
		private TileFakeReactorInjector tile;

		public OPInjector(TileFakeReactorInjector tile) {
			this.tile = tile;
		}

		@Override
		public long receiveOP(long maxReceive, boolean simulate) {
			if (simulate) {
				return maxReceive;
			}
			TileFakeReactorCore core = tile.getCachedCore();
			if (core != null) {
				core.injectEnergy(maxReceive);
				return 0;
			}
			return maxReceive;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return (int) receiveOP(maxReceive, simulate);
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
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
			return false;
		}

		@Override
		public boolean canReceive() {
			return true;
		}
	}
}
