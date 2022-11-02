package net.foxmcloud.draconicadditions.handlers;

import net.foxmcloud.draconicadditions.CommonMethods.BlockStorage;
import net.foxmcloud.draconicadditions.items.curios.ModularHarness;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
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
		Level world = event.getWorld();
		if (world.isClientSide) {
			return;
		}
		Player player = event.getPlayer();
		boolean handsAreEmpty = player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty();
		if (handsAreEmpty && player.isShiftKeyDown() && event.getPos().closerToCenterThan(player.position(), 2)) {
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
				if (ModularHarness.hasAttachedBlockEntity(harness, world)) {
					if (event.getFace() == Direction.UP && aboveState.getBlock() == Blocks.AIR) {
						Vec2 pRot = player.getRotationVector();
						Vec2 rotation = new Vec2(-pRot.x, pRot.y + 180);
						if (BlockStorage.restoreBlockFromTag(world, abovePos, rotation, harness.getTag(), true, true)) {
							player.displayClientMessage(new TranslatableComponent("info.da.modular_harness.placeSuccess"), true);
							event.setCanceled(true);
						}
					}
				}
				else if (world.getBlockState(event.getPos()).hasBlockEntity()) {
					if (ModularHarness.storeBlockEntity(world, event.getPos(), harness, player, true)) {
						player.displayClientMessage(new TranslatableComponent("info.da.modular_harness.storeSuccess"), true);
					}
					event.setCanceled(true);
				}
			}
		}
	}
}
