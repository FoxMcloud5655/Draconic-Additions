package net.foxmcloud.draconicadditions.items.tools;

import static com.brandon3055.draconicevolution.api.itemconfig.IItemConfigField.EnumControlType.SLIDER;
import static com.brandon3055.draconicevolution.client.model.tool.ToolTransforms.STAFF_STATE;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.lib.PairKV;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.draconicevolution.api.itemconfig.BooleanConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.DoubleConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.IntegerConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.client.model.tool.ToolOverrideList;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.items.tools.DraconicBow;
import com.brandon3055.draconicevolution.items.tools.ToolStats;

import codechicken.lib.util.ItemNBTUtils;
import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.foxmcloud.draconicadditions.utils.DATextures;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChaoticBow extends DraconicBow implements IChaosItem {

	public ChaoticBow() {
		super();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		if (isInCreativeTab(tab)) {
			ItemStack uberStack = subItems.remove(subItems.size() - 1);
			this.setChaosStable(uberStack, true);
			subItems.add(uberStack);
		}
	}

	@Override
	public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
		double maxDamage = ToolStats.BOW_BASE_DAMAGE + (getToolTier(stack) * ToolStats.BOW_TIER_MULT_DAMAGE) + (UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.ARROW_DAMAGE) * ToolStats.BOW_MULT_DAMAGE);
		int maxSpeed = ToolStats.BOW_BASE_SPEED + (getToolTier(stack) * ToolStats.BOW_TIER_MULT_SPEED) + UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.ARROW_SPEED) * ToolStats.BOW_MULT_SPEED;
		registry.register(stack, new DoubleConfigField("bowArrowDamage", maxDamage, 0, maxDamage, "config.field.bowArrowDamage.description", SLIDER));
		registry.register(stack, new IntegerConfigField("bowArrowSpeedModifier", maxSpeed, 0, maxSpeed, "config.field.bowArrowSpeedModifier.description", SLIDER).setPrefix("+").setExtension("%"));
		registry.register(stack, new BooleanConfigField("bowAutoFire", false, "config.field.bowAutoFire.description"));
		registry.register(stack, new IntegerConfigField("bowZoomModifier", 0, 0, getMaxZoomModifier(stack), "config.field.bowZoomModifier.description", SLIDER));
		registry.register(stack, new DoubleConfigField("bowShockPower", 0, 0, 5, "config.field.bowShockPower.description", SLIDER));
		registry.register(stack, new BooleanConfigField("bowFireArrow", true, "config.field.bowFireArrow.description.disabled"));
		return registry;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (world.getWorldTime() % 20 == 0) {
			if (!isChaosStable(stack)) {
				double maxDamage = ToolStats.BOW_BASE_DAMAGE + (getToolTier(stack) * ToolStats.BOW_TIER_MULT_DAMAGE) + (UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.ARROW_DAMAGE) * ToolStats.BOW_MULT_DAMAGE);
				int maxSpeed = ToolStats.BOW_BASE_SPEED + (getToolTier(stack) * ToolStats.BOW_TIER_MULT_SPEED) + UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.ARROW_SPEED) * ToolStats.BOW_MULT_SPEED;
				ToolConfigHelper.setDoubleField("bowArrowDamage", stack,  maxDamage);
				ToolConfigHelper.setIntegerField("bowArrowSpeedModifier", stack,  maxSpeed);
				ToolConfigHelper.setDoubleField("bowShockPower", stack,  1);
				ToolConfigHelper.setBooleanField("bowAutoFire", stack, true);
			}
			ToolConfigHelper.setBooleanField("bowFireArrow", stack, true);
		}
	}

	@Override
	public int getProfileCount(ItemStack stack) {
		return isChaosStable(stack) ? 5 : 1;
	}

	@Override
	public void loadEnergyStats() {
		int capacity = net.foxmcloud.draconicadditions.items.tools.ToolStats.CHAOTIC_BASE_CAPACITY;
		setEnergyStats(capacity, capacity, 0);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		if (getChaosInfoStable(stack) != null) tooltip.add(getChaosInfoStable(stack));
		super.addInformation(stack, player, tooltip, advanced);
	}

	@Override
	public int getMaxUpgradeLevel(ItemStack stack, String upgrade) {
		return 4;
	}

	@Override
	public int getToolTier(ItemStack stack) {
		return 3;
	}

	@Override
	public int getReaperLevel(ItemStack stack) {
		return 3;
	}

	@Override
	public void registerRenderer(Feature feature) {
		super.registerRenderer(feature);
		ToolOverrideList.putOverride(this, ChaoticBow::handleTransforms);
	}

	@SideOnly(Side.CLIENT)
	private static IModelState handleTransforms(TransformType transformType, IModelState state) {
		return transformType == TransformType.FIXED || transformType == TransformType.GROUND ? STAFF_STATE : state;
	}

	@Override
	public PairKV<TextureAtlasSprite, ResourceLocation> getModels(ItemStack stack) {
		byte pull = ItemNBTUtils.getByte(stack, "render:bow_pull");
		return new PairKV<>(DATextures.CHAOTIC_BOW[pull], new ResourceLocation("draconicadditions", String.format("models/item/tools/chaotic_bow0%s.obj", pull)));
	}
}
