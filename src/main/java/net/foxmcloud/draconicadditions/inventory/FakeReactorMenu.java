package net.foxmcloud.draconicadditions.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.inventory.DETileMenu;

import codechicken.lib.gui.modular.lib.container.SlotGroup;
import codechicken.lib.inventory.container.modular.ModularSlot;
import codechicken.lib.math.MathHelper;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorCore;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Created by brandon3055 on 10/02/2017.
 */
public class FakeReactorMenu extends DETileMenu<TileFakeReactorCore> {

	public final SlotGroup main = createSlotGroup(0);
	public final SlotGroup hotBar = createSlotGroup(0);

	public final SlotGroup input = createSlotGroup(1);
	public final SlotGroup output = createSlotGroup(2);

	public FakeReactorMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
		this(windowId, playerInv, getClientTile(playerInv, extraData));
	}

	public FakeReactorMenu(int windowId, Inventory playerInv, TileFakeReactorCore tile) {
		super(DAContent.menuFakeReactorCore.get(), windowId, playerInv, tile);
		main.addPlayerMain(playerInv);
		hotBar.addPlayerBar(playerInv);

		input.addSlots(3, 0, e -> new SlotReactor(tile, e));
		output.addSlots(3, 0, e -> new SlotReactor(tile, 3 + e));
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (getSlot(slotId) instanceof SlotReactor) {
			return;
		}
		else if (slotId <= 35) {
			super.clicked(slotId, dragType, clickTypeIn, player);
		}
	}

	public static int getFuelValue(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		else if (stack.getItem() == DEContent.AWAKENED_DRACONIUM_BLOCK.get().asItem()) {
			return stack.getCount() * 1296;
		}
		else if (stack.getItem() == DEContent.INGOT_DRACONIUM_AWAKENED.get()) {
			return stack.getCount() * 144;
		}
		else if (stack.getItem() == DEContent.NUGGET_DRACONIUM_AWAKENED.get()) {
			return stack.getCount() * 16;
		}
		return 0;
	}

	public static int getChaosValue(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		else if (stack.getItem() == DEContent.CHAOS_FRAG_LARGE.get()) {
			return stack.getCount() * 1296;
		}
		else if (stack.getItem() == DEContent.CHAOS_FRAG_MEDIUM.get()) {
			return stack.getCount() * 144;
		}
		else if (stack.getItem() == DEContent.CHAOS_FRAG_SMALL.get()) {
			return stack.getCount() * 16;
		}
		return 0;
	}

	public static class SlotReactor extends ModularSlot {
		private static Container emptyInventory = new SimpleContainer(6);
		private final TileFakeReactorCore tile;

		public SlotReactor(TileFakeReactorCore tile, int index) {
			super(emptyInventory, index);
			this.tile = tile;
		}

		@Override
		public void onQuickCraft(ItemStack before, ItemStack after) {
			if (!before.isEmpty() && !after.isEmpty()) {
				if (before.getItem() == after.getItem()) {
					int i = after.getCount() - before.getCount();

					if (i > 0) {
						this.onQuickCraft(before, i);
					}
				}
			}
		}

		@Override
		public boolean mayPlace(@Nullable ItemStack stack) {
			return false;
		}

		@NotNull
		@Override
		public ItemStack getItem() {
			int index = getSlotIndex();
			if (index < 3) {
				int fuel = MathHelper.floor(tile.reactableFuel.get());
				int block = fuel / 1296;
				int ingot = (fuel % 1296) / 144;
				int nugget = ((fuel % 1296) % 144) / 16;

				if (index == 0 && block > 0) {
					return new ItemStack(DEContent.AWAKENED_DRACONIUM_BLOCK.get(), block);
				}
				else if (index == 1 && ingot > 0) {
					return new ItemStack(DEContent.INGOT_DRACONIUM_AWAKENED.get(), ingot);
				}
				else if (index == 2 && nugget > 0) {
					return new ItemStack(DEContent.NUGGET_DRACONIUM_AWAKENED.get(), nugget);
				}
			}
			else {
				int chaos = MathHelper.floor(tile.convertedFuel.get());
				int block = chaos / 1296;
				int ingot = (chaos % 1296) / 144;
				int nugget = ((chaos % 1296) % 144) / 16;

				if (index == 3 && block > 0) {
					return new ItemStack(DEContent.CHAOS_FRAG_LARGE.get(), block);
				}
				else if (index == 4 && ingot > 0) {
					return new ItemStack(DEContent.CHAOS_FRAG_MEDIUM.get(), ingot);
				}
				else if (index == 5 && nugget > 0) {
					return new ItemStack(DEContent.CHAOS_FRAG_SMALL.get(), nugget);
				}
			}

			return ItemStack.EMPTY;
		}

		@Override
		public void set(@Nonnull ItemStack stack) {
			if (!(tile.getLevel() instanceof ServerLevel)) return;
			int index = getSlotIndex();
			if (index < 3) {
				double prevValue = getFuelValue(getItem());
				double newValue = getFuelValue(stack);
				double change = newValue - prevValue;
				tile.reactableFuel.add(change);
			} else {
				double prevValue = getChaosValue(getItem());
				double newValue = getChaosValue(stack);
				double change = newValue - prevValue;
				tile.convertedFuel.add(change);
			}
			this.setChanged();
		}

		@Override
		public void setChanged() {
			this.tile.setChanged();
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}

		@Override
		public ItemStack remove(int amount) {
			return ItemStack.EMPTY;
		}
	}
}
