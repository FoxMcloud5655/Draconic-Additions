package net.foxmcloud.draconicadditions.datagen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.world.DADimension;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

public class DimensionCopyProvider implements IDataProvider {
	private DataGenerator generator;
	private ResourceLocation dimRL;
	private ResourceLocation dimTypeRL;
	public String mainCodeFolder = "main";

	public DimensionCopyProvider(DataGenerator generator, ResourceLocation dimension, ResourceLocation dimensionType) {
		this.generator = generator;
		dimRL = dimension;
		dimTypeRL = dimensionType;
	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		Path inputDimPath = getPathDimension(getInputPath(), dimRL);
		Path inputDimTypePath = getPathDimensionType(getInputPath(), dimTypeRL);
		Path outputDimPath = getPathDimension(getOutputPath(), dimRL);
		Path outputDimTypePath = getPathDimensionType(getOutputPath(), dimTypeRL);
		if (Files.exists(inputDimPath)) {
			Files.createDirectories(outputDimPath.getParent());
			Files.copy(inputDimPath, outputDimPath, StandardCopyOption.REPLACE_EXISTING);
			cache.putNew(outputDimPath, String.valueOf(dimRL.hashCode()));
		}
		if (Files.exists(inputDimTypePath)) {
			Files.createDirectories(outputDimTypePath.getParent());
			Files.copy(inputDimTypePath, outputDimTypePath, StandardCopyOption.REPLACE_EXISTING);
			cache.putNew(outputDimTypePath, String.valueOf(dimTypeRL.hashCode()));
		}
	}

	protected Path getPathDimension(Path path, ResourceLocation location) {
		return path.resolve("data/" + location.getNamespace() + "/dimension/" + location.getPath() + ".json");
	}

	protected Path getPathDimensionType(Path path, ResourceLocation location) {
		return path.resolve("data/" + location.getNamespace() + "/dimension_type/" + location.getPath() + ".json");
	}
	
	protected Path getInputPath() {
		return this.generator.getOutputFolder().getParent().getParent().resolve(mainCodeFolder + "/resources");
	}
	
	protected Path getOutputPath() {
		return this.generator.getOutputFolder();
	}

	@Override
	public String getName() {
		return "Dimentions and Dimension Types";
	}

}
