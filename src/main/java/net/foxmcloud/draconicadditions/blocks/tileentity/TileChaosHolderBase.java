package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileChaosHolderBase extends TileBCore {
	private int maxChaos = 2000;

	public final ManagedInt chaos = register(new ManagedInt("chaos", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public int getMaxChaos() {
		return maxChaos;
	}

	public void setMaxChaos(int amount) {
		maxChaos = amount;
	}

	public TileChaosHolderBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
	}
}
