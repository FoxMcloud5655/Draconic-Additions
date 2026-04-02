package net.foxmcloud.draconicadditions.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import com.brandon3055.draconicevolution.api.event.ModularItemInitEvent;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEContent;

import net.covers1624.quack.util.SneakyUtils;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.modules.entities.ChaosInjectorEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class DAEventHandler {

	public static void init() {
		//NeoForge.EVENT_BUS.addListener(DAEventHandler::rightClickBlock);
		NeoForge.EVENT_BUS.addListener(DAEventHandler::chaosInjectionDeath);
		NeoForge.EVENT_BUS.addListener(DAEventHandler::blockHealingWhenInjecting);
		NeoForge.EVENT_BUS.addListener(DAEventHandler::blockShieldDamageWhenInjecting);
		NeoForge.EVENT_BUS.addListener(DAEventHandler::addCategoriesToContainers);
		NeoForge.EVENT_BUS.addListener(DAEventHandler::onEntitySpawn);
	}

	/*
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Level world = event.getLevel();
		if (world.isClientSide) {
			return;
		}
		Player player = event.getEntity();
		boolean handsAreEmpty = player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty();
		if (!handsAreEmpty || !player.isShiftKeyDown() || !event.getPos().closerToCenterThan(player.position(), 2)) {
			return;
		}
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
		if (harness.equals(ItemStack.EMPTY)) {
			return;
		}
		BlockPos abovePos = event.getPos().above();
		BlockState aboveState = world.getBlockState(abovePos);
		if (ModularHarness.hasAttachedBlockEntity(harness)) {
			if (event.getFace() != Direction.UP || aboveState.getBlock() != Blocks.AIR) {
				return;
			}
			Vec2 pRot = player.getRotationVector();
			Vec2 rotation = new Vec2(-pRot.x, pRot.y + 180);
			String blockName = ModularHarness.getAttachedName(harness);
			if (BlockStorage.restoreBlockFromTag(world, abovePos, rotation, harness.get(ItemData.), true, true)) {
				player.displayClientMessage(Component.translatable("info.da.modular_harness.placeSuccess", blockName), true);
				event.setCanceled(true);
			}
		}
		else if (world.getBlockState(event.getPos()).hasBlockEntity()) {
			if (ModularHarness.storeBlockEntity(world, event.getPos(), harness, player, true)) {
				player.displayClientMessage(Component.translatable("info.da.modular_harness.storeSuccess", ModularHarness.getAttachedName(harness)), true);
			}
			event.setCanceled(true);
		}
	}
	*/

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static boolean chaosInjectionDeath(LivingDeathEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && injector.isChaosInBlood()) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static boolean blockHealingWhenInjecting(LivingHealEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && (injector.getRate() > 0 || injector.isChaosInBlood())) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static boolean blockShieldDamageWhenInjecting(LivingIncomingDamageEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && injector.isChaosInBlood()) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}

	@SubscribeEvent
	public static void addCategoriesToContainers(ModularItemInitEvent e) {
		ArrayList<Item> validChaosContainers = new ArrayList<Item>(Arrays.asList(
				DEContent.AXE_CHAOTIC.get(),
				DEContent.BOW_CHAOTIC.get(),
				DEContent.CHESTPIECE_CHAOTIC.get(),
				DEContent.PICKAXE_CHAOTIC.get(),
				DEContent.SHOVEL_CHAOTIC.get(),
				DEContent.STAFF_CHAOTIC.get(),
				DEContent.SWORD_CHAOTIC.get()
				));
		ItemStack stack = e.getStack();
		if (validChaosContainers.contains(stack.getItem())) {
			ModuleHostImpl host = SneakyUtils.unsafeCast(e.getHost());
			host.addCategories(IChaosContainer.CHAOS_CONTAINER);
		}
	}

	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinLevelEvent e) {
		if (!(e.getEntity() instanceof ItemEntity)) {
			return;
		}
		ItemEntity entity = (ItemEntity)e.getEntity();
		ItemStack stack = entity.getItem();
		if (stack.getItem() != DEContent.DRAGON_HEART.get()) {
			return;
		}
		CompoundTag nbt = entity.getPersistentData();
		if (nbt != null && nbt.contains("guardian_heart") && nbt.getBoolean("guardian_heart")) {
			entity.setItem(DAContent.chaosHeart.get().getDefaultInstance());
		}
	}
}
