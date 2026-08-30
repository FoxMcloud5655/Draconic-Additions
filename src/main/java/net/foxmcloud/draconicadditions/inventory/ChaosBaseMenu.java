package net.foxmcloud.draconicadditions.inventory;

import codechicken.lib.gui.modular.lib.container.SlotGroup;
import codechicken.lib.inventory.container.modular.ModularSlot;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public abstract class ChaosBaseMenu extends DATileChaosMenu<TileChaosHolderBase> {
	public final SlotGroup main = createSlotGroup(0, 2, 1);
	public final SlotGroup hotBar = createSlotGroup(0, 2, 1);
	public final SlotGroup slot = createSlotGroup(1, 0);
	public final SlotGroup capacitor = createSlotGroup(2, 0);
	
	public ChaosBaseMenu(MenuType<ChaosBaseMenu> menuType, int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(menuType, windowId, playerInv, getClientTile(playerInv, extraData));
    }
	
	public ChaosBaseMenu(MenuType<ChaosBaseMenu> menuType, int windowId, Inventory playerInv, TileChaosHolderBase tile) {
		super(menuType, windowId, playerInv, tile);
		main.addPlayerMain(inventory);
        hotBar.addPlayerBar(inventory);
		slot.addSlot(new ModularSlot(tile.itemHandler, 0).setValidator((stack) -> {return tile.isItemValidForSlot(0, stack);}));
		capacitor.addSlot(new ModularSlot(tile.itemHandler, 1).setValidator((stack) -> {return tile.isItemValidForSlot(1, stack);}));
	}
}
