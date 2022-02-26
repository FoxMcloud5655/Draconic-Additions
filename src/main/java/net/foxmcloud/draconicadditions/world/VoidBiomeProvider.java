package net.foxmcloud.draconicadditions.world;

import net.minecraft.world.biome.BiomeRegistry;
import net.minecraft.world.biome.provider.SingleBiomeProvider;

public class VoidBiomeProvider extends SingleBiomeProvider {
   public VoidBiomeProvider() {
      super(BiomeRegistry.THE_VOID);
   }
}