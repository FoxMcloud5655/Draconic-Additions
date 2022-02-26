package net.foxmcloud.draconicadditions.world;

import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.server.ServerWorld;

public class DADimension {
	public static final RegistryKey<DimensionType> HARNESS_DIM_TYPE = RegistryKey.create(
			Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(DraconicAdditions.MODID_PREFIX + "harnessvoid"));
	public static final RegistryKey<World> HARNESS_DIM = RegistryKey.create(
			Registry.DIMENSION_REGISTRY, new ResourceLocation(DraconicAdditions.MODID_PREFIX + "harnessvoid"));

	public static void init() {
		Registry.register(
				Registry.CHUNK_GENERATOR,
				DraconicAdditions.MODID_PREFIX + "harnessvoid",
				HarnessChunkGenerator.CODEC
		);
	}

	public static Vector3 findEmptyBlock(World world) {
		if (world instanceof ServerWorld && world.getServer() != null) {
			Vector3 pos = new Vector3(0,64,0);
			while(true) {
				BlockState state = world.getBlockState(pos.pos());
				if (state == Blocks.AIR.defaultBlockState()) {
					return pos;
				}
				pos.x += 2;
			}
		}
		return null;
	}
}
