package net.foxmcloud.draconicadditions.datagen;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class LootTableGenerator extends LootTableProvider {

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> lootTables = ImmutableList.of(Pair.of(BlockLootTables::new, LootParameterSets.BLOCK));

    public LootTableGenerator(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return lootTables;
    }

    public static class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

        protected void addTables() {}
        
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Registry.BLOCK.stream().filter(block -> Objects.requireNonNull(block.getRegistryName()).getNamespace().equals(DraconicAdditions.MODID)).collect(Collectors.toList());
        }

    }
}