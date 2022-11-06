package net.foxmcloud.draconicadditions.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import com.brandon3055.draconicevolution.api.event.ModularItemInitEvent;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEContent;

import net.covers1624.quack.util.SneakyUtils;
import net.foxmcloud.draconicadditions.CommonMethods.BlockStorage;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.items.curios.ModularHarness;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
	
	@SubscribeEvent
	public void addCategoriesToContainers(ModularItemInitEvent e) {
		ArrayList<Item> validChaosContainers = new ArrayList<Item>(Arrays.asList(
			DEContent.axe_chaotic,
			DEContent.bow_chaotic,
			DEContent.chestpiece_chaotic,
			DEContent.pickaxe_chaotic,
			DEContent.shovel_chaotic,
			DEContent.staff_chaotic,
			DEContent.sword_chaotic
		));
		ItemStack stack = e.getStack();
		if (validChaosContainers.contains(stack.getItem())) {
			ModuleHostImpl host = SneakyUtils.unsafeCast(e.getHost());
			host.addCategories(IChaosContainer.CHAOS_CONTAINER);
		}
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof ItemEntity) {
			ItemEntity entity = (ItemEntity)e.getEntity();
			ItemStack stack = entity.getItem();
			if (stack.getItem() == DEContent.dragon_heart) {
				CompoundTag nbt = stack.getTag();
				if (nbt != null && nbt.contains("guardian_heart") && nbt.getBoolean("guardian_heart")) {
					entity.setItem(new ItemStack(DAContent.chaosHeart));
				}
			}
		}
	}
}
