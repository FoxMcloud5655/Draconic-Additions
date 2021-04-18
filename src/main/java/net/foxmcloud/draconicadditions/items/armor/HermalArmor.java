package net.foxmcloud.draconicadditions.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.items.armor.DraconicArmor;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.client.model.ModelPotatoArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HermalArmor extends DraconicArmor {

	private static ArmorMaterial hermalMaterial = EnumHelper.addArmorMaterial("hermalArmor", DraconicAdditions.MODID_PREFIX + "hermal_armor", -1, new int[] {8, 14, 20, 8}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

	public HermalArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(hermalMaterial, renderIndexIn, equipmentSlotIn);
	}

	public HermalArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}
	
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            modifyEnergy(stack, getCapacity(stack));
            subItems.add(stack);

            if (getMaxUpgradeLevel(stack, "") > 0) {
	            ItemStack uberStack = new ItemStack(this);
	
	            for (String upgrade : getValidUpgrades(uberStack)) {
	                UpgradeHelper.setUpgradeLevel(uberStack, upgrade, getMaxUpgradeLevel(uberStack, upgrade));
	            }
	
	            modifyEnergy(uberStack, getCapacity(uberStack));
	            subItems.add(uberStack);
            }
        }
    }

	@Override
	public int getMaxUpgradeLevel(ItemStack stack, String upgrade) {
		return ArmorStats.HERMAL_UPGRADE_LEVEL;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped model;

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (DEConfig.disable3DModels) {
			return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
		}

		if (model == null) {
			if (armorType == EntityEquipmentSlot.HEAD) model = new ModelPotatoArmor(0.5F, true, false, false, false);
			else if (armorType == EntityEquipmentSlot.CHEST) model = new ModelPotatoArmor(1.5F, false, true, false, false);
			else if (armorType == EntityEquipmentSlot.LEGS) model = new ModelPotatoArmor(1.5F, false, false, true, false);
			else model = new ModelPotatoArmor(1F, false, false, false, true);
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
		float points = ArmorStats.HERMAL_BASE_SHIELD_CAPACITY * getProtectionShare();
		return points;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (!stack.isEmpty()) {
			HermalArmor armor = (HermalArmor)stack.getItem();
			armor.modifyEnergy(stack, 100);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("item.draconicadditions:hermal.lore"));
		tooltip.add(I18n.format("item.draconicadditions:hermal.lore2"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	@Override
	public float getRecoveryRate(ItemStack stack) {
		return (float) ArmorStats.HERMAL_SHIELD_RECOVERY;
	}

	@Override
	public int getEnergyPerProtectionPoint() {
		return ArmorStats.HERMAL_SHIELD_RECHARGE_COST;
	}

	@Override
	protected int getCapacity(ItemStack stack) {
		return ArmorStats.HERMAL_BASE_CAPACITY;
	}

	@Override
	protected int getMaxReceive(ItemStack stack) {
		return ArmorStats.HERMAL_MAX_RECIEVE;
	}
}
