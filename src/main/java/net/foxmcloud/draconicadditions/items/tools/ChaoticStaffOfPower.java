package net.foxmcloud.draconicadditions.items.tools;

import static com.brandon3055.draconicevolution.client.model.tool.ToolTransforms.STAFF_STATE;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.lib.PairKV;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.utils.InfoHelper;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.client.model.tool.ToolOverrideList;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.items.tools.DraconicStaffOfPower;
import com.brandon3055.draconicevolution.items.tools.IAOEWeapon;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.items.IChaosItem;
import net.foxmcloud.draconicadditions.utils.DATextures;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChaoticStaffOfPower extends DraconicStaffOfPower implements IChaosItem {

	public ChaoticStaffOfPower() {
		super();
	}

	@Override
	public double getBaseMinSpeedConfig() {
		return ToolStats.CHAOTIC_STAFF_MINING_SPEED;
	}

	@Override
	public double getBaseAttackSpeedConfig() {
		return ToolStats.CHAOTIC_STAFF_ATTACK_SPEED;
	}

	@Override
	public double getBaseAttackDamageConfig() {
		return ToolStats.CHAOTIC_STAFF_ATTACK_DAMAGE;
	}

	@Override
	public int getBaseMinAOEConfig() {
		return ToolStats.BASE_CHAOTIC_MINING_AOE + 1;
	}

	@Override
	public void loadEnergyStats() {
		int capacity = ToolStats.CHAOTIC_BASE_CAPACITY * 3;
		setEnergyStats(capacity, capacity, 0);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (getChaosInfo(stack) != null) tooltip.add(getChaosInfo(stack));
        super.addInformation(stack, player, tooltip, advanced);
    }

	@Override
	public List<String> getValidUpgrades(ItemStack stack) {
		List<String> list = super.getValidUpgrades(stack);
		return list;
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
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (this instanceof IAOEWeapon && player.getCooledAttackStrength(0.5F) >= 0.95F && ((IAOEWeapon) this).getWeaponAOE(stack) > 0) {

            List<EntityLivingBase> entities = player.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(((IAOEWeapon) this).getWeaponAOE(stack), 0.25D, ((IAOEWeapon) this).getWeaponAOE(stack)));

            for (EntityLivingBase aoeEntity : entities) {
                if (aoeEntity != player && !player.isOnSameTeam(entity) && extractAttackEnergy(stack, aoeEntity, player)) {
                    aoeEntity.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                    
                    float damage = (float) (aoeEntity.getHealth()*0.3+getAttackDamage(stack));
					if(aoeEntity.getHealth()<=damage) {
						aoeEntity.setHealth(1);
						aoeEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), getAttackDamage(stack));
					}else {
						aoeEntity.setHealth(aoeEntity.getHealth()-damage);
					}
                }
            }

            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
            player.spawnSweepParticles();
        }
        
        extractAttackEnergy(stack, entity, player);
        return super.onLeftClickEntity(stack, player, entity);
    }
	protected double getMaxAttackAOE(ItemStack stack) {
		int level = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.ATTACK_AOE);
		if (level == 0) return 3;
		else if (level == 1) return 5;
		else if (level == 2) return 8;
		else if (level == 3) return 11;
		else if (level == 4) return 15;
		else return 0;
	}


	@Override
	public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
		if (isChaosStable(stack)) {
			super.getFields(stack, registry);
		}
		else addEnchantConfig(stack, registry);
		return registry;
	}

	@Override
	public double getWeaponAOE(ItemStack stack) {
		if (!isChaosStable(stack)) return getMaxAttackAOE(stack);
		else return ToolConfigHelper.getDoubleField("attackAOE", stack);
	}

	@Override
	public int getReaperLevel(ItemStack stack) {
		return 5;
	}

	@Override
	public void registerRenderer(Feature feature) {
		super.registerRenderer(feature);
		ToolOverrideList.putOverride(this, ChaoticStaffOfPower::handleTransforms);
	}

	@SideOnly(Side.CLIENT)
	private static IModelState handleTransforms(TransformType transformType, IModelState state) {
		return transformType == TransformType.FIXED || transformType == TransformType.GROUND ? STAFF_STATE : state;
	}

	@Override
	public PairKV<TextureAtlasSprite, ResourceLocation> getModels(ItemStack stack) {
		return new PairKV<>(DATextures.CHAOTIC_STAFF_OF_POWER, new ResourceLocation("draconicadditions", "models/item/tools/chaotic_staff_of_power.obj"));
	}
}
