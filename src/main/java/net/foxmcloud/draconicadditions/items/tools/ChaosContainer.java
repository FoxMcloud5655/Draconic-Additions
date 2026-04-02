package net.foxmcloud.draconicadditions.items.tools;

import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.api.IInvCharge;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ShieldData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.client.keybinding.KeyBindings;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.init.DEDamage;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.TechProperties;

import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.CommonMethods;
import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.items.IModularEnergyItem;
import net.foxmcloud.draconicadditions.items.ISimpleCountdown;
import net.foxmcloud.draconicadditions.lib.DAItemData;
import net.foxmcloud.draconicadditions.modules.entities.ChaosInjectorEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ChaosContainer extends Item implements IModularEnergyItem, IChaosContainer, IInvCharge, ISimpleCountdown {

	private TechLevel techLevel;

	public ChaosContainer(TechProperties props) {
		super(props);
		techLevel = props.getTechLevel();
	}

	@Override
	public TechLevel getTechLevel() {
		return techLevel;
	}

	@Override
	public @NotNull ModuleHostImpl instantiateHost(ItemStack stack) {
		ModuleHostImpl host = IModularEnergyItem.super.instantiateHost(stack);

		host.addAdditionalType(ModuleTypes.SHIELD_BOOST);
		return host;
	}

	public void upkeep(Player player, ItemStack stack, Level world) {
		if (getChaos(stack) > 0 && !player.isCreative() && !player.isSpectator()) {
			if (CommonMethods.cheatCheck(stack, world) || !hasShielding(stack)) {
				extractEnergy(player, stack, Long.MAX_VALUE);
			}
			long RFToDrain = getRFCost(stack);
			if (extractEnergy(player, stack, RFToDrain) < RFToDrain) {
				Vector3 pos = new Vector3(player.getX(), player.getY(), player.getZ());
				CommonMethods.explodeEntity(pos, world);
				player.hurt(world.damageSources().source(DEDamage.CHAOS_IMPLOSION), getChaos(stack));
				player.displayClientMessage(Component.translatable("info.da.chaos.explode", getName(stack).getString()), true);
				stack.shrink(1);
			}
			else if (shouldAlarm(stack) && hasShielding(stack)) {
				if (getCurrentCountdown(stack) <= 0 || getCountdownAmount(stack) <= getCurrentCountdown(stack)) {
					resetCountdown(stack);
				}
				if (advanceCountdown(stack)) {
					player.displayClientMessage(Component.translatable("info.da.chaos.warning", getName(stack)).withStyle(ChatFormatting.RED), true);
				}
				else if (getCurrentCountdown(stack) == getCountdownAmount(stack) / 2) {
					player.displayClientMessage(Component.literal(""), true);
				}
			}
		}
		else CommonMethods.cheatCheck(stack, world);
	}

	@Override
	public void handleTick(ModuleHost host, ItemStack stack, LivingEntity entity, @Nullable EquipmentSlot slot, boolean inEquipModSlot) {
		if (!(entity instanceof Player)) return;
		Player player = (Player) entity;
		if (player.level().isClientSide && shouldAlarm(stack) && hasShielding(stack) && !player.isCreative() && !player.isSpectator()) {
			if (getCurrentCountdown(stack) <= 0) {
				float pitch = 1.5F + ((float)(1 - (double)EnergyUtils.getEnergyStored(stack) / EnergyUtils.getMaxEnergyStored(stack)) * 0.5F);
				player.level().playSound(player, player.blockPosition(), DESounds.BEAM.get(), SoundSource.MASTER, 1.0F, pitch);
			}
		}
		upkeep(player, stack, player.level());
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, Player player) {
		if (getChaos(stack) > 0 && !player.isCreative()) {
			player.displayClientMessage(Component.translatable("info.da.chaos.cantdrop", stack.getHoverName()), true);
			return false;
		}
		else {
			stack.set(DAItemData.CHEAT_CHECK, 0L);
			return true;
		}
	}

	@SuppressWarnings("unused")
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
		if (false) { // TODO: DE currently has an issue with DEDamage where it crashes the game.
			if (getChaos(stack) > 0 && !entity.isInvulnerableTo(DEDamage.chaosImplosion(entity.level()))) {
				Vector3 pos = new Vector3(entity.getX(), entity.getY(), entity.getZ());
				CommonMethods.explodeEntity(pos, player.level());
				if (!player.level().isClientSide) {
					float damage = Math.min(getChaos(stack), entity.getHealth());
					entity.hurt(DEDamage.chaosImplosion(player.level()), damage);
					removeChaos(stack, (int) Math.floor(damage));
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity ent) {
			return interactLivingEntity(stack, player, ent, InteractionHand.MAIN_HAND).consumesAction();
		}
		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		if (world.getBlockEntity(pos) instanceof TileChaosHolderBase) {
			TileChaosHolderBase tileEntity = (TileChaosHolderBase) world.getBlockEntity(pos);
			if (((ChaosContainer) stack.getItem()).getChaos(stack) > 0 && tileEntity.chaos.get() != tileEntity.getMaxChaos()) {
				int chaosToRemove = Math.min(tileEntity.getMaxChaos() - tileEntity.chaos.get(), getChaos(stack));
				int removed = removeChaos(stack, chaosToRemove);
				player.displayClientMessage(Component.translatable("info.da.chaos.xfer.to", removed, tileEntity.getName()), true);
				tileEntity.chaos.add(removed);
			}
			else {
				int chaosToAdd = Math.min(getMaxChaos(stack) - getChaos(stack), tileEntity.chaos.get());
				int added = chaosToAdd - addChaos(stack, chaosToAdd);
				player.displayClientMessage(Component.translatable("info.da.chaos.xfer.from", added, tileEntity.getName()), true);
				tileEntity.chaos.subtract(added);
			}
			return InteractionResult.SUCCESS;
		}
		return handleChaosInBlood(player, stack).getResult();
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return handleChaosInBlood(player, player.getItemInHand(hand));
	}

	private InteractionResultHolder<ItemStack> handleChaosInBlood(Player player, ItemStack stack) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(player);
		if (injector != null && injector.isChaosInBlood()) {
			removeChaos(stack, injector.modifyChaos(-1));
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return getChaos(oldStack) != getChaos(newStack);
	}

	@Override
	public boolean canCharge(ItemStack stack, LivingEntity player, boolean isHeld) {
		return true;
	}

	@Override
	public int getMaxChaos(ItemStack stack) {
		ModuleHost host = DECapabilities.getHost(stack);
		if (host == null) {
			host = new ModuleHostImpl(TechLevel.CHAOTIC, 4, 4, "curios", false, CHAOS_CONTAINER);
		}
		return host.getModuleData(ModuleTypes.SHIELD_BOOST, new ShieldData(0, 0)).shieldCapacity() * 50;
	}

	private double getRFCostPerChaos(ShieldData shielding) {
		return (((shielding.shieldCapacity() / 2) * (shielding.shieldCapacity() / 2) * EquipCfg.shieldPassiveModifier) / shielding.shieldRecharge()) * DAConfig.chaosContainerRFMultiplier;
	}

	private long getRFCost(ItemStack stack) {
		ModuleHost host = DECapabilities.getHost(stack);
		ShieldData shielding = host.getModuleData(ModuleTypes.SHIELD_BOOST, new ShieldData(0, 0));
		return Math.round(getRFCostPerChaos(shielding) * getChaos(stack) * (getChaos(stack) > getMaxChaos(stack) && hasShielding(stack) ? Math.pow(2, (double)getChaos(stack) / getMaxChaos(stack)) : 1D));
	}

	private boolean shouldAlarm(ItemStack stack) {
		return getChaos(stack) > 0 && (getChaos(stack) > getMaxChaos(stack) || getEnergyStored(stack) < EnergyUtils.getMaxEnergyStored(stack) / 2);
	}

	private boolean hasShielding(ItemStack stack) {
		return getMaxChaos(stack) > 0;
	}

	@Override
	public int getCountdownAmount(ItemStack stack) {
		return Math.max((int)Math.round(((double)EnergyUtils.getEnergyStored(stack) / EnergyUtils.getMaxEnergyStored(stack)) * 40), 2);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
		if (!Screen.hasShiftDown()) {
			tooltip.add(Component.translatable("[Modular Item]").withStyle(ChatFormatting.BLUE));
		}
		tooltip.add(getChaosInfo(stack));
		EnergyUtils.addEnergyInfo(stack, tooltip);
		try (ModuleHost host = DECapabilities.getHost(stack)) {
			assert host != null;
			long rfCost = getRFCost(stack);
			if (rfCost >= 0) {
				tooltip.add(Component.translatable("info.da.opCost", Utils.formatNumber(rfCost)).withStyle(ChatFormatting.GRAY));
			}
			if (shouldAlarm(stack) && hasShielding(stack)) {
				tooltip.add(getCurrentCountdown(stack) > getCountdownAmount(stack) / 2 ? Component.translatable("info.da.chaos.warning", getName(stack)).withStyle(ChatFormatting.RED) : Component.literal(""));
			}
		}
		boolean hasEnergy = EnergyUtils.getMaxEnergyStored(stack) > 0;
		if (!hasEnergy || !hasShielding(stack)) {
			if (!hasShielding(stack)) {
				tooltip.add(Component.translatable("info.da.chaos.noShield").withStyle(ChatFormatting.RED));
			}
			if (!hasEnergy) {
				tooltip.add(Component.translatable("modular_item.draconicevolution.requires_energy").withStyle(ChatFormatting.RED));
			}
			if (KeyBindings.toolModules != null && KeyBindings.toolModules.getTranslatedKeyMessage() != null) {
				tooltip.add(Component.translatable("modular_item.draconicevolution.requires_energy_press", KeyBindings.toolModules.getTranslatedKeyMessage().getString()).withStyle(ChatFormatting.BLUE));
			}
		}
	}

	@Override
	public boolean isEquipped(ItemStack stack, EquipmentSlot slot, boolean inEquipmentSlot) {
		return false;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return damageBarVisible(stack);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return damageBarWidth(stack);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return damageBarColour(stack);
	}
}
