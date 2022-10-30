package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.lib.TechPropBuilder;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.EnergyData;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEModules;

import net.foxmcloud.draconicadditions.DAConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Hermal extends ModularEnergyItem {

	public Hermal(TechPropBuilder props) {
		super(props.food(new Food.Builder().alwaysEat().nutrition(0).saturationMod(0).build()));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int unknownint, boolean unknownbool) {
		if (!(entity instanceof PlayerEntity) || entity.level.isClientSide) {
			return;
		}
		stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).ifPresent(host -> {
			EnergyData data = host.getModuleData(ModuleTypes.ENERGY_STORAGE);
			if (data != null && data.getCapacity() == DEModules.chaoticEnergy.getData().getCapacity()) {
				PlayerEntity player = (PlayerEntity)entity;
				if (EnergyUtils.canReceiveEnergy(player.getMainHandItem())) {
					EnergyUtils.insertEnergy(player.getMainHandItem(), DAConfig.hermalRFAmount, false);
				}
				else if (EnergyUtils.canReceiveEnergy(player.getOffhandItem())) {
					EnergyUtils.insertEnergy(player.getOffhandItem(), DAConfig.hermalRFAmount, false);
				}
			}
		});
		stack.setTag(null);
	}
	
	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, 1, 1, "hermal", removeInvalidModules);
		host.addCategories(ModuleCategory.ENERGY);
		return host;
	}
	
	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		return null;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving)
	{
		if (!world.isClientSide && entityLiving instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)entityLiving;
			EntityType.LIGHTNING_BOLT.spawn((ServerWorld)world, stack, player, player.blockPosition(), SpawnReason.COMMAND, true, true);
			player.hurt(new DamageSource("administrative.kill").bypassInvul().bypassArmor().bypassMagic(), Float.MAX_VALUE);
		}
		else {
			PlayerEntity player = (PlayerEntity)entityLiving;
			player.sendMessage(new TranslationTextComponent("info.da.hermal.eat.success"), null);
		}
		stack.shrink(1);
		return stack;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		player.displayClientMessage(new TranslationTextComponent("info.da.hermal.eat.attempt"), true);
		return super.use(world, player, hand);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {}
}
