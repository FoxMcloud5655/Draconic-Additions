package net.foxmcloud.draconicadditions.blocks.reactor;

import com.brandon3055.draconicevolution.blocks.reactor.ReactorCore;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FakeReactorCore extends ReactorCore {

	public FakeReactorCore(Properties properties) {
		super(properties);
		setBlockEntity(DAContent.tileFakeReactorCore::get, true);
	}
	
    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
    	level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3); // Probably not the best way to do this, but copies the original block code.
        wasExploded(level, pos, explosion);
    }
}
