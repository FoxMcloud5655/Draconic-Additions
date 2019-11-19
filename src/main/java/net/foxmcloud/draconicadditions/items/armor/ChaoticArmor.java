package net.foxmcloud.draconicadditions.items.armor;

import static com.brandon3055.draconicevolution.api.itemconfig.IItemConfigField.EnumControlType.SLIDER;
import static net.minecraft.inventory.EntityEquipmentSlot.CHEST;
import static net.minecraft.inventory.EntityEquipmentSlot.FEET;
import static net.minecraft.inventory.EntityEquipmentSlot.HEAD;
import static net.minecraft.inventory.EntityEquipmentSlot.LEGS;

import com.brandon3055.brandonscore.lib.DelayedTask;
import com.brandon3055.brandonscore.utils.InventoryUtils;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.api.itemconfig.BooleanConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.IntegerConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.items.armor.DraconicArmor;

import codechicken.lib.math.MathHelper;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.client.model.ModelChaoticArmor;
import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ChaoticArmor extends DraconicArmor implements IChaosItem {

	private static ArmorMaterial chaoticMaterial = EnumHelper.addArmorMaterial("chaoticArmor", DraconicAdditions.MODID_PREFIX + "chaotic_armor", -1, new int[] {6, 12, 16, 6}, 0, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 6.0F);

	public ChaoticArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(chaoticMaterial, renderIndexIn, equipmentSlotIn);
	}

	public ChaoticArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Override
	public int getMaxUpgradeLevel(ItemStack stack, String upgrade) {
		return ArmorStats.CHAOTIC_UPGRADE_LEVEL;
	}

	@Override
	public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
		if (armorType == HEAD && isChaosStable(stack)) {
			registry.register(stack, new BooleanConfigField("armorNV", false, "config.field.armorNV.description"));
			registry.register(stack, new BooleanConfigField("armorNVLock", false, "config.field.armorNVLock.description"));
			registry.register(stack, new BooleanConfigField("armorAutoFeed", false, "config.field.armorAutoFeed.description"));
		}
		if (armorType == CHEST) {
			int speedLimit = MathHelper.clip(DEConfig.flightSpeedLimit != -1 ? DEConfig.flightSpeedLimit : 600, 0, 1200);
			registry.register(stack, new IntegerConfigField("armorFSpeedModifier", 0, isChaosStable(stack) ? 0 : speedLimit, speedLimit, "config.field.armorFSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
			registry.register(stack, new IntegerConfigField("armorVFSpeedModifier", 0, isChaosStable(stack) ? 0 : speedLimit, speedLimit, "config.field.armorVFSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
			registry.register(stack, new BooleanConfigField("armorInertiaCancel", false, "config.field.armorInertiaCancel.description"));
			registry.register(stack, new BooleanConfigField("armorFlightLock", false, "config.field.armorFlightLock.description"));
		}
		if (armorType == LEGS) {
			int u = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.MOVE_SPEED);
			int i = 200 + (100 * u) + (Math.max(u - 1, 0) * 100) + (Math.max(u - 2, 0) * 100);
			registry.register(stack, new IntegerConfigField("armorSpeedModifier", 0, isChaosStable(stack) ? 0 : i, i, "config.field.armorSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
			if (isChaosStable(stack)) registry.register(stack, new BooleanConfigField("armorSpeedFOVWarp", false, "config.field.armorSpeedFOVWarp.description"));
		}
		if (armorType == FEET) {
			int u = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.JUMP_BOOST);
			int i = 200 + (100 * u) + (Math.max(u - 1, 0) * 100) + (Math.max(u - 2, 0) * 100);
			registry.register(stack, new IntegerConfigField("armorJumpModifier", 0, isChaosStable(stack) ? 0 : i, i, "config.field.armorSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
			registry.register(stack, new BooleanConfigField("armorHillStep", true, "config.field.armorHillStep.description"));
		}
		if ((armorType == FEET || armorType == LEGS || armorType == CHEST) && isChaosStable(stack)) {
			registry.register(stack, new BooleanConfigField("sprintBoost", false, "config.field.sprintBoost.description"));
		}

		if (isChaosStable(stack)) {
			registry.register(stack, new BooleanConfigField("hideArmor", false, "config.field.hideArmor.description"));
		}

		return registry;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped model;

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (ToolConfigHelper.getBooleanField("hideArmor", itemStack)) {
			if (model_invisible == null) {
				model_invisible = new ModelBiped() {
					@Override
					public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {}
				};
			}

			return model_invisible;
		}

		if (DEConfig.disable3DModels) {
			return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
		}

		if (model == null) {
			if (armorType == EntityEquipmentSlot.HEAD) model = new ModelChaoticArmor(0.5F, true, false, false, false);
			else if (armorType == EntityEquipmentSlot.CHEST) model = new ModelChaoticArmor(1.5F, false, true, false, false);
			else if (armorType == EntityEquipmentSlot.LEGS) model = new ModelChaoticArmor(1.5F, false, false, true, false);
			else model = new ModelChaoticArmor(1F, false, false, false, true);
			this.model.bipedHead.showModel = (armorType == EntityEquipmentSlot.HEAD);
			this.model.bipedHeadwear.showModel = (armorType == EntityEquipmentSlot.HEAD);
			this.model.bipedBody.showModel = ((armorType == EntityEquipmentSlot.CHEST) || (armorType == EntityEquipmentSlot.LEGS));
			this.model.bipedLeftArm.showModel = (armorType == EntityEquipmentSlot.CHEST);
			this.model.bipedRightArm.showModel = (armorType == EntityEquipmentSlot.CHEST);
			this.model.bipedLeftLeg.showModel = (armorType == EntityEquipmentSlot.LEGS || armorType == EntityEquipmentSlot.FEET);
			this.model.bipedRightLeg.showModel = (armorType == EntityEquipmentSlot.LEGS || armorType == EntityEquipmentSlot.FEET);
		}

		if (entityLiving == null) {
			return model;
		}

		this.model.isSneak = entityLiving.isSneaking();
		this.model.isRiding = entityLiving.isRiding();
		this.model.isChild = entityLiving.isChild();

		this.model.bipedHeadwear.showModel = (armorType == EntityEquipmentSlot.HEAD);
		this.model.bipedBody.showModel = ((armorType == EntityEquipmentSlot.CHEST) || (armorType == EntityEquipmentSlot.LEGS));
		this.model.bipedLeftArm.showModel = (armorType == EntityEquipmentSlot.CHEST);
		this.model.bipedRightArm.showModel = (armorType == EntityEquipmentSlot.CHEST);
		this.model.bipedLeftLeg.showModel = (armorType == EntityEquipmentSlot.LEGS || armorType == EntityEquipmentSlot.FEET);
		this.model.bipedRightLeg.showModel = (armorType == EntityEquipmentSlot.LEGS || armorType == EntityEquipmentSlot.FEET);

		return model;
	}

	@Override
	public float getProtectionPoints(ItemStack stack) {
		int upgradeLevel = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.SHIELD_CAPACITY);
		float points = ArmorStats.CHAOTIC_BASE_SHIELD_CAPACITY * getProtectionShare() * (upgradeLevel + 1);
		return points;
	}

	@Override
	public float getRecoveryRate(ItemStack stack) {
		return (float) ArmorStats.CHAOTIC_SHIELD_RECOVERY * (1F + UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.SHIELD_RECOVERY));
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (stack.isEmpty()) {
			return;
		}
		if (stack.getItem() == DAFeatures.chaoticHelm) {

			if (world.isRemote) {
				return;
			}

			if (this.getEnergyStored(stack) >= 5000 && clearNegativeEffects(player)) {
				this.modifyEnergy(stack, -5000);
			}

			FoodStats foodStats = player.getFoodStats();
			if (player.ticksExisted % 100 == 0 && ToolConfigHelper.getBooleanField("armorAutoFeed", stack) && foodStats.needFood() && this.getEnergyStored(stack) >= 500) {
				IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

				if (handler != null) {
					for (int i = 0; i < handler.getSlots(); i++) {
						ItemStack candidate = handler.getStackInSlot(i);
						if (!candidate.isEmpty() && candidate.getItem() instanceof ItemFood) {
							ItemFood food = (ItemFood) candidate.getItem();
							int amount = food.getHealAmount(candidate);
							if (amount > 0 && food.getHealAmount(candidate) + foodStats.getFoodLevel() <= 20) {
								candidate = candidate.copy();
								ItemStack foodStack = handler.extractItem(i, candidate.getCount(), false);

								if (ItemStack.areItemStacksEqual(foodStack, candidate)) {
									foodStats.addStats(food, foodStack);
									foodStack = food.onItemUseFinish(foodStack, world, player);
									if (world.rand.nextInt(3) == 0) {
										DelayedTask.run(20, () -> world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, .5F, world.rand.nextFloat() * 0.1F + 0.9F));
									}

									world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.5F + 0.5F * world.rand.nextInt(2), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
									foodStack = handler.insertItem(i, foodStack, false);
									this.modifyEnergy(stack, -500);
									if (!foodStack.isEmpty()) {
										InventoryUtils.givePlayerStack(player, foodStack.copy());
									}
									break;
								}
								else {
									foodStack = handler.insertItem(i, foodStack, false);
									if (!foodStack.isEmpty()) {
										InventoryUtils.givePlayerStack(player, foodStack.copy());
									}
								}
							}
						}
					}
				}
			}

			Potion nv = Potion.getPotionFromResourceLocation("night_vision");

			if (nv == null) {
				return;
			}

			PotionEffect active = player.getActivePotionEffect(nv);
			if (ToolConfigHelper.getBooleanField("armorNV", stack) && (player.world.getLightBrightness(new BlockPos((int) Math.floor(player.posX), (int) player.posY + 1, (int) Math.floor(player.posZ))) < 0.1F || ToolConfigHelper.getBooleanField("armorNVLock", stack))) {
				player.addPotionEffect(new PotionEffect(nv, 500, 0, false, false));
			}
			else if (active != null && ToolConfigHelper.getBooleanField("armorNVLock", stack)) {
				player.removePotionEffect(nv);
			}
		}
		super.onArmorTick(world, player, stack);
	}

	@Override
	public int getEnergyPerProtectionPoint() {
		return ArmorStats.CHAOTIC_SHIELD_RECHARGE_COST;
	}

	@Override
	protected int getCapacity(ItemStack stack) {
		int level = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.RF_CAPACITY);

		if (level == 0) {
			return ArmorStats.CHAOTIC_BASE_CAPACITY;
		}
		else {
			return ArmorStats.CHAOTIC_BASE_CAPACITY * (int) Math.pow(2, level + 1);
		}
	}

	@Override
	protected int getMaxReceive(ItemStack stack) {
		return ArmorStats.CHAOTIC_MAX_RECIEVE;
	}
}
