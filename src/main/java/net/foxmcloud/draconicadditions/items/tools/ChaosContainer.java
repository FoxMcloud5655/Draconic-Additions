package net.foxmcloud.draconicadditions.items.tools;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.power.IOPStorageModifiable;
import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.api.IInvCharge;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;

import codechicken.lib.vec.Vector3;
import net.foxmcloud.draconicadditions.CommonMethods;
import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.items.ModularEnergyItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ChaosContainer extends ModularEnergyItem implements IChaosContainer, IInvCharge {
	public static final ModuleCategory CHAOSCONTAINER = new ModuleCategory();
	
	public ChaosContainer(TechPropBuilder props) {
		super(props);
	}
	
	public void upkeep(PlayerEntity player, ItemStack stack, World world) {
		if (getChaos(stack) > 0 && !player.isCreative()) { //TODO: .isEnchantable isn't right...  Is it?
			if (CommonMethods.cheatCheck(stack, world)) {
				ItemNBTHelper.setInteger(stack, "Energy", 0);
			}
			long drainedRF = extractEnergy(player, stack, getChaos(stack) * DAConfig.chaosContainerRFPerChaos);
			if (drainedRF != getChaos(stack) * DAConfig.chaosContainerRFPerChaos) {
				Vector3 pos = new Vector3(player.getX(), player.getY(), player.getZ());
				CommonMethods.explodeEntity(pos, world);
				player.hurt(CommonMethods.chaosBurst, getChaos(stack));
				player.displayClientMessage(new TranslationTextComponent("msg.da.chaosContainer.explode"), true);
				stack.shrink(1);
			}
		}
		else CommonMethods.cheatCheck(stack, world);
	}

	@Override
	public void handleTick(ItemStack stack, LivingEntity entity, @Nullable EquipmentSlotType slot, boolean inEquipModSlot) {
		if (!(entity instanceof PlayerEntity) || entity.level.isClientSide) return;
		PlayerEntity player = (PlayerEntity) entity;
		upkeep(player, stack, player.level);
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, PlayerEntity player) {
		if (getChaos(stack) > 0 && !player.isCreative()) {
			player.displayClientMessage(new TranslationTextComponent("msg.da.chaosContainer.cantdrop"), true);
			return false;
		}
		else {
			ItemNBTHelper.setLong(stack, "cheatCheck", 0);
			return true;
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
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
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		if (world.getBlockEntity(pos) instanceof TileChaosHolderBase) {
			TileChaosHolderBase tileEntity = (TileChaosHolderBase) world.getBlockEntity(pos);
			if (((ChaosContainer) stack.getItem()).getChaos(stack) > 0 && tileEntity.chaos.get() != tileEntity.getMaxChaos()) {
				int chaosToRemove = Math.min(getMaxChaos(stack) - tileEntity.chaos.get(), getChaos(stack));
				removeChaos(stack, chaosToRemove);
				tileEntity.chaos.add(chaosToRemove);
			}
			else {
				int chaosToAdd = Math.min(getMaxChaos(stack) - getChaos(stack), tileEntity.chaos.get());
				addChaos(stack, chaosToAdd);
				tileEntity.chaos.subtract(chaosToAdd);
			}
			return ActionResultType.SUCCESS;
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
		return ActionResultType.PASS;
	}
	
	/*
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		IChaosInBlood pCap = player.getCapability(ChaosInBloodProvider.PLAYER_CAP, null);
		if (pCap != null && player.isEntityAlive() && pCap.getChaos() > 0) {
			int chaosToAdd = (int)(Math.min(pCap.getChaos(), 2) * 4);
			addChaos(stack, chaosToAdd);
			pCap.removeChaos(chaosToAdd / 4.0F);
			if (pCap.getChaos() <= 0.25) {
				ItemStack chest = player.inventory.armorItemInSlot(2);
				if (chest.getItem() == DAFeatures.chaoticChest && ItemNBTHelper.getBoolean(chest, "injecting", false)) {
					ItemNBTHelper.setBoolean(chest, "injecting", false);
					player.displayClientMessage(new TranslationTextComponent("msg.da.chaosInjection.failsafe"), true);
				}
			}
			else {
				player.displayClientMessage(new TranslationTextComponent("msg.da.chaosContainer.charge"), true);
			}
			return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
		}
		return new ActionResult<ItemStack>(ActionResultType.PASS, stack);
	}
	*/
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return getChaos(oldStack) != getChaos(newStack);
	}
	
	@Override
	public ITextComponent getHighlightTip(ItemStack stack, ITextComponent displayName) {
		return new StringTextComponent(displayName.getString() + " - " + getChaosInfo(stack));
	}
	
	@Override
	public boolean canCharge(ItemStack stack, LivingEntity player, boolean isHeld) {
		return true;
	}
}
