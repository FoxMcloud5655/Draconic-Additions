package net.foxmcloud.draconicadditions.world;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

public class HarnessChunkGenerator extends ChunkGenerator {
	public static final Codec<HarnessChunkGenerator> CODEC = RegistryLookupCodec.create(Registry.BIOME_REGISTRY)
			.xmap(HarnessChunkGenerator::new, HarnessChunkGenerator::biomes).stable().codec();
	private final Registry<Biome> biomes;
	
	public HarnessChunkGenerator(Registry<Biome> biomeRegistry) {
		super(new VoidBiomeProvider(), new DimensionStructuresSettings(false));
		biomes = biomeRegistry;
	}

	public Registry<Biome> biomes() {
		return this.biomes;
	}

	@Override
	protected Codec<HarnessChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return this;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion region, IChunk chunk) {}

	@Override
	public void fillFromNoise(IWorld world, StructureManager manager, IChunk chunk) {}

	@Override
	public int getBaseHeight(int w, int l, Type type) {
		return 0;
	}

	@Override
	public IBlockReader getBaseColumn(int w, int l) {
		return new Blockreader(new BlockState[0]);
	}
}
