package net.foxmcloud.draconicadditions.items.tools;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
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
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.TechProperties;

import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.CommonMethods;
import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.items.ISimpleCountdown;
import net.foxmcloud.draconicadditions.items.ModularEnergyItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ChaosContainer extends ModularEnergyItem implements IChaosContainer, IInvCharge, ISimpleCountdown {
	public ChaosContainer(TechProperties props) {
		super(props);
	}

	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = super.createHost(stack);
		host.addCategories(CHAOS_CONTAINER);
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
				player.hurt(CommonMethods.chaosBurst, getChaos(stack));
				player.displayClientMessage(new TranslatableComponent("info.da.chaos.explode"), true);
				stack.shrink(1);
			}
			else if (shouldAlarm(stack) && hasShielding(stack)) {
				if (getCurrentCountdown(stack) <= 0 || getCountdownAmount(stack) <= getCurrentCountdown(stack)) {
					resetCountdown(stack);
				}
				if (advanceCountdown(stack)) {
					player.displayClientMessage(new TranslatableComponent("info.da.chaos.warning", getName(stack)).withStyle(ChatFormatting.RED), true);
				}
				else if (getCurrentCountdown(stack) == getCountdownAmount(stack) / 2) {
					player.displayClientMessage(new TextComponent(""), true);
				}
			}
		}
		else CommonMethods.cheatCheck(stack, world);
	}

	@Override
	public void handleTick(ItemStack stack, LivingEntity entity, @Nullable EquipmentSlot slot, boolean inEquipModSlot) {
		if (!(entity instanceof Player)) return;
		Player player = (Player) entity;
		if (player.level.isClientSide && shouldAlarm(stack) && hasShielding(stack) && !player.isCreative() && !player.isSpectator()) {
			if (getCurrentCountdown(stack) <= 0) {
				float pitch = 1.5F + ((float)(1 - (double)EnergyUtils.getEnergyStored(stack) / EnergyUtils.getMaxEnergyStored(stack)) * 0.5F);
				player.level.playSound(player, new BlockPos(player.getX(), player.getY(), player.getZ()), DESounds.beam, SoundSource.MASTER, 1.0F, pitch);
			}
		}
		upkeep(player, stack, player.level);
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, Player player) {
		if (getChaos(stack) > 0 && !player.isCreative()) {
			player.displayClientMessage(new TranslatableComponent("msg.da.chaosContainer.cantdrop"), true);
			return false;
		}
		else {
			ItemNBTHelper.setLong(stack, "cheatCheck", 0);
			return true;
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof LivingEntity) {
			LivingEntity ent = (LivingEntity) entity;
			if (getChaos(stack) > 0 && !ent.isInvulnerableTo(CommonMethods.chaosBurst)) {
				Vector3 pos = new Vector3(ent.getX(), ent.getY(), ent.getZ());
				CommonMethods.explodeEntity(pos, player.level);
				if (!player.level.isClientSide) {
					float damage = Math.min(getChaos(stack), ent.getHealth());
					entity.hurt(CommonMethods.chaosBurst, damage);
					removeChaos(stack, (int) Math.floor(damage));
				}
				return true;
			}
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
				player.displayClientMessage(new TranslatableComponent("info.da.chaos.xfer.to", removed, tileEntity.getName()), true);
				tileEntity.chaos.add(removed);
			}
			else {
				int chaosToAdd = Math.min(getMaxChaos(stack) - getChaos(stack), tileEntity.chaos.get());
				int added = chaosToAdd - addChaos(stack, chaosToAdd);
				player.displayClientMessage(new TranslatableComponent("info.da.chaos.xfer.from", added, tileEntity.getName()), true);
				tileEntity.chaos.subtract(added);
			}
			return InteractionResult.SUCCESS;
		}
		/*
		else {
			IChaosInBlood pCap = player.getCapability(ChaosInBloodProvider.PLAYER_CAP, null);
			if (pCap != null && player.isEntityAlive() && pCap.getChaos() > 0) {
				ActionResult<ItemStack> result = onItemRightClick(world, player, hand);
				stack = result.getResult();
				return result.getType();
			}
		}
		 */
		return InteractionResult.PASS;
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
		ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElse(new ModuleHostImpl(TechLevel.CHAOTIC, 4, 4, "curios", false, CHAOS_CONTAINER));
		return host.getModuleData(ModuleTypes.SHIELD_BOOST, new ShieldData(0, 0)).getShieldCapacity() * 10;
	}

	private double getRFCostPerChaos(ShieldData shielding) {
		return ((shielding.getShieldCapacity() * shielding.getShieldCapacity() * EquipCfg.shieldPassiveModifier) / shielding.getShieldRecharge()) * DAConfig.chaosContainerRFMultiplier;
	}

	private long getRFCost(ItemStack stack) {
		ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElseThrow(IllegalStateException::new);
		ShieldData shielding = host.getModuleData(ModuleTypes.SHIELD_BOOST, new ShieldData(0, 0));
		return Math.round(getRFCostPerChaos(shielding) * getChaos(stack) * (getChaos(stack) > getMaxChaos(stack) && hasShielding(stack) ? Math.pow(2, (double)getChaos(stack) / getMaxChaos(stack)) : 1D));
	}

	private boolean shouldAlarm(ItemStack stack) {
		return getChaos(stack) > getMaxChaos(stack) || getEnergyStored(stack) < EnergyUtils.getMaxEnergyStored(stack) / 2;
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
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		if (!Screen.hasShiftDown()) {
			tooltip.add(new TranslatableComponent("[Modular Item]").withStyle(ChatFormatting.BLUE));
		}
		tooltip.add(getChaosInfo(stack));
		EnergyUtils.addEnergyInfo(stack, tooltip);
		ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElse(null);
		if (host != null) {
			long rfCost = getRFCost(stack);
			if (rfCost >= 0) {
				tooltip.add(new TranslatableComponent("info.da.opCost", Utils.formatNumber(rfCost)).withStyle(ChatFormatting.GRAY));
			}
			if (shouldAlarm(stack) && hasShielding(stack)) {
				tooltip.add(getCurrentCountdown(stack) > getCountdownAmount(stack) / 2 ? new TranslatableComponent("info.da.chaos.warning", getName(stack)).withStyle(ChatFormatting.RED) : new TextComponent(""));
			}
		}
		boolean hasEnergy = EnergyUtils.getMaxEnergyStored(stack) > 0;
		if (!hasEnergy || !hasShielding(stack)) {
			if (!hasShielding(stack)) {
				tooltip.add(new TranslatableComponent("info.da.chaos.noShield").withStyle(ChatFormatting.RED));
			}
			if (!hasEnergy) {
				tooltip.add(new TranslatableComponent("modular_item.draconicevolution.requires_energy").withStyle(ChatFormatting.RED));
			}
			if (KeyBindings.toolModules != null && KeyBindings.toolModules.getTranslatedKeyMessage() != null) {
				tooltip.add(new TranslatableComponent("modular_item.draconicevolution.requires_energy_press", KeyBindings.toolModules.getTranslatedKeyMessage().getString()).withStyle(ChatFormatting.BLUE));
			}
		}
	}
}
