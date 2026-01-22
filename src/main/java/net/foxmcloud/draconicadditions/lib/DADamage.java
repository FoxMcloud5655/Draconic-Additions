package net.foxmcloud.draconicadditions.lib;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class DADamage {
	private static Map<ResourceKey<DamageType>, DamageSource> SOURCES = new HashMap<>();

	public static final ResourceKey<DamageType> injectionDeath = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(DraconicAdditions.MODID, "chaos_injection"));

	public static DamageSource injectionDeath(Level level) {
		return getSource(level, injectionDeath);
	}

	private static DamageSource getSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
		return SOURCES.computeIfAbsent(type, e -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(e), attacker));
	}

	private static DamageSource getSource(Level level, ResourceKey<DamageType> type, @Nullable Entity projectile, @Nullable Entity owner) {
		return SOURCES.computeIfAbsent(type, e -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(e), projectile, owner));
	}

	private static DamageSource getSource(Level level, ResourceKey<DamageType> type) {
		return SOURCES.computeIfAbsent(type, e -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(e)));
	}
}
