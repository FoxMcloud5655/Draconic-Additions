package net.foxmcloud.draconicadditions.handlers;

import net.foxmcloud.draconicadditions.CommonMethods.BlockStorage;
import net.foxmcloud.draconicadditions.items.curios.ModularHarness;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class DAEventHandler {
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		if (world.isClientSide) {
			return;
		}
		PlayerEntity player = event.getPlayer();
		boolean handsAreEmpty = player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty();
		if (handsAreEmpty && player.isShiftKeyDown() && event.getPos().closerThan(player.position(), 2)) {
			ISlotType backSlot = CuriosApi.getSlotHelper().getSlotType(SlotTypePreset.BACK.getIdentifier()).get();
			IDynamicStackHandler stackHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElse(null).getStacksHandler(backSlot.getIdentifier()).get().getStacks();
			if (stackHandler.getSlots() == 0) {
				return;
			}
			ItemStack harness = ItemStack.EMPTY;
			for (int i = 0; i < stackHandler.getSlots(); i++) {
				if (stackHandler.getStackInSlot(i).getItem() instanceof ModularHarness) {
					harness = stackHandler.getStackInSlot(i);
					break;
				}
			}
			if (!harness.equals(ItemStack.EMPTY)) {
				BlockPos abovePos = event.getPos().above();
				BlockState aboveState = world.getBlockState(abovePos);
				if (ModularHarness.hasAttachedTileEntity(harness, world)) {
					if (event.getFace() == Direction.UP && aboveState.getBlock().isAir(aboveState, world, abovePos)) {
						Vector2f pRot = player.getRotationVector();
						Vector2f rotation = new Vector2f(-pRot.x, pRot.y + 180);
						if (BlockStorage.restoreBlockFromTag(world, abovePos, rotation, harness.getTag(), true, true)) {
							player.displayClientMessage(new TranslationTextComponent("info.da.modular_harness.placeSuccess"), true);
							event.setCanceled(true);
						}
					}
				}
				else if (world.getBlockState(event.getPos()).hasTileEntity()) {
					if (ModularHarness.storeTileEntity(world, event.getPos(), harness, player, true)) {
						player.displayClientMessage(new TranslationTextComponent("info.da.modular_harness.storeSuccess"), true);
					}
					event.setCanceled(true);
				}
			}
		}
	}
}
