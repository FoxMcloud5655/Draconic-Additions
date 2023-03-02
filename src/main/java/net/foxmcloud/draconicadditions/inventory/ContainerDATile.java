package net.foxmcloud.draconicadditions.inventory;

import org.jetbrains.annotations.Nullable;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.inventory.ContainerSlotLayout;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostContainer;
import com.brandon3055.draconicevolution.inventory.ContainerDETile;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.modules.ModuleGrid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.util.LazyOptional;

public class ContainerDATile<T extends TileBCore> extends ContainerDETile<T> implements ModuleHostContainer {
    public ContainerDATile(@Nullable MenuType<?> type, int windowId, Inventory player, FriendlyByteBuf extraData) {
        super(type, windowId, player, extraData);
    }

    public ContainerDATile(@Nullable MenuType<?> type, int windowId, Inventory player, FriendlyByteBuf extraData, ContainerSlotLayout.LayoutFactory<T> factory) {
        super(type, windowId, player, extraData, factory);
    }

    public ContainerDATile(@Nullable MenuType<?> type, int windowId, Inventory player, T tile) {
        super(type, windowId, player, tile);
    }

    public ContainerDATile(@Nullable MenuType<?> type, int windowId, Inventory player, T tile, ContainerSlotLayout.LayoutFactory<T> factory) {
        super(type, windowId, player, tile, factory);
    }
    
    @Override
    protected void initHost(T tile, Inventory player) {
        LazyOptional<ModuleHost> opt = tile.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
        opt.ifPresent(host -> {
            this.moduleHost = host;
            this.moduleGrid = new ModuleGrid(this, player, (TileChaosHolderBase) tile);
        });
    }
}
