package net.foxmcloud.draconicadditions.inventory;

import com.brandon3055.brandonscore.inventory.ContainerBCBase;
import com.brandon3055.brandonscore.inventory.SlotCheckValid;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosInfuser;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerChaosInfuser extends ContainerBCBase<TileChaosInfuser> {

	public ContainerChaosInfuser(PlayerEntity player, TileChaosInfuser tile) {
		super(player, tile);

		IInventory invPlayer = player.inventory;

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + 18 * x, 138));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, 8 + 18 * x, 80 + y * 18));
			}
		}

		addSlotToContainer(new SlotCheckValid(tile, 0, 64, 35));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return tile.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int i) {
		Slot slot = getSlot(i);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			ItemStack result = stack.copy();

			if (i >= 36) {
				if (!mergeItemStack(stack, 0, 36, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (stack.getItem() instanceof IChaosContainer || !mergeItemStack(stack, 36, 36 + tile.getSizeInventory(), false)) {
				return ItemStack.EMPTY;
			}

			if (stack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}

			slot.onTake(player, stack);

			return result;
		}

		return ItemStack.EMPTY;
	}
}
