package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.EnergyData;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.init.TechProperties;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.lib.DASounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class Hermal extends RecordItem implements IModularEnergyItem {

	public TechLevel techLevel;
	
	public Hermal(TechProperties props) {
		super(1, () -> DASounds.hermal, props);
		techLevel = props.getTechLevel();
	}
	
	@Override
	public TechLevel getTechLevel() {
		return techLevel;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int unknownint, boolean unknownbool) {
		if (!(entity instanceof Player) || entity.level.isClientSide) {
			return;
		}
		stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).ifPresent(host -> {
			EnergyData data = host.getModuleData(ModuleTypes.ENERGY_STORAGE);
			if (data != null && data.getCapacity() == DEModules.chaoticEnergy.getData().getCapacity()) {
				Player player = (Player)entity;
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
		ModuleHostImpl host = new ModuleHostImpl(getTechLevel(), 1, 1, "hermal", removeInvalidModules);
		host.addCategories(ModuleCategory.ENERGY);
		return host;
	}
	
	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		return null;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entityLiving) {
		if (!world.isClientSide) {
			EntityType.LIGHTNING_BOLT.spawn((ServerLevel)world, stack, null, entityLiving.blockPosition(), MobSpawnType.COMMAND, true, true);
			entityLiving.hurt(new DamageSource("administrative.kill").bypassInvul().bypassArmor().bypassMagic(), Float.MAX_VALUE);
		}
		else {
			entityLiving.sendMessage(new TranslatableComponent("info.da.hermal.eat.success"), null);
		}
		stack.shrink(1);
		return stack;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
		player.displayClientMessage(new TranslatableComponent("info.da.hermal.eat.attempt"), true);
		return super.use(world, player, hand);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		player.displayClientMessage(new TranslatableComponent("info.da.hermal.craft"), true);
	}

	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag) {
		components.add(new TranslatableComponent("info.da.hermal.lore").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
	}

	@Override
	public boolean isEnchantable(ItemStack p_41456_) {
		return false;
	}
}
