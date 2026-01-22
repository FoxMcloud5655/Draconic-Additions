/*
package net.foxmcloud.draconicadditions.modules.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.config.IntegerProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ProjectileData;
import com.brandon3055.draconicevolution.api.modules.lib.EntityOverridesItemUse;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;

import net.foxmcloud.draconicadditions.entity.EntityBombProjectile;
import net.foxmcloud.draconicadditions.modules.data.BombProjectileData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

public class BombProjectileEntity extends ModuleEntity<BombProjectileData> implements EntityOverridesItemUse, Comparable {

	private static final long costPerRadius = 1000;
	private static final int chargePerTick = 5;

	private int charge = 0;

	private IntegerProperty radius;

	public BombProjectileEntity(Module<BombProjectileData> module) {
		super(module);
		addProperty(radius = new IntegerProperty("bomb_projectile.radius", 1).setFormatter(ConfigProperty.IntegerFormatter.AOE)
				.range(0, module.getData().getRadius()));
		this.savePropertiesToItem = true;
	}

	public long getRFCost() {
		int blockRadius = radius.getValue() * 2 + 1;
		return blockRadius * blockRadius * costPerRadius;
	}

	@Override
	public void onEntityUseItem(LivingEntityUseItemEvent useEvent) {
		if (useEvent.isCanceled()) return;
		if (useEvent instanceof LivingEntityUseItemEvent.Start event) {
			event.setDuration(72000);
		} else if (useEvent instanceof LivingEntityUseItemEvent.Tick event) {
			useTick(event);
		} else if (useEvent instanceof LivingEntityUseItemEvent.Stop || useEvent instanceof LivingEntityUseItemEvent.Finish) {
			endUse(useEvent);
		}
	}

	private void useTick(LivingEntityUseItemEvent.Tick event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		ItemStack stack = event.getItem();
		LazyOptional<IOPStorage> optional = stack.getCapability(DECapabilities.OP_STORAGE);
		if (!optional.isPresent()) {
			return;
		}
		IOPStorage storage = optional.orElseThrow(IllegalStateException::new);
		if (charge < 100 && storage.extractOP(getRFCost(), true) >= getRFCost()) {
			storage.extractOP(getRFCost(), false);
			charge += chargePerTick;
			if (charge > 100) {
				charge = 100;
			}
		}

	}

	private void endUse(LivingEntityUseItemEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			ItemStack stack = event.getItem();
			if (charge < 0) {
				charge = 0;
				return;
			}
			ModuleHost host = DECapabilities.getHost(stack);
			ProjectileData projData = host.getModuleData(ModuleTypes.PROJ_MODIFIER, new ProjectileData(0, 0, 0, 0, 0));
			float velocity = (charge / 100F) * (projData.velocity() + 1);
			double xAng = player.getLookAngle().x;
			double yAng = player.getLookAngle().y;
			double zAng = player.getLookAngle().z;
			EntityBombProjectile projectile = new EntityBombProjectile(EntityType.FIREBALL, player, xAng, yAng, zAng, player.level());
			projectile.xPower = velocity * xAng;
			projectile.yPower = velocity * yAng;
			projectile.zPower = velocity * zAng;
		}
	}

	@Override
	public void writeToItemStack(ItemStack stack, ModuleContext context) {
		super.writeToItemStack(stack, context);
		stack.getOrCreateTag().putInt("charge", charge);
	}

	@Override
	public void readFromItemStack(ItemStack stack, ModuleContext context) {
		super.readFromItemStack(stack, context);
		if (stack.hasTag()) {
			charge = stack.getOrCreateTag().getInt("charge");
		}
	}

	@Override
	public void writeToNBT(CompoundTag compound) {
		super.writeToNBT(compound);
		compound.putInt("charge", charge);
	}

	@Override
	public void readFromNBT(CompoundTag compound) {
		super.readFromNBT(compound);
		charge = compound.getInt("charge");
	}

	@Override
	public void addToolTip(List<Component> tooltip) {
		BombProjectileData data = (BombProjectileData)module.getData();
		tooltip.add(Component.translatable("info.da.radius", radius.getValue()));
		tooltip.add(Component.translatable("info.da.opCost", getRFCost()));
	}

	@Override
	public int compareTo(@NotNull Object o) {
		BombProjectileData data = (BombProjectileData)module.getData();
		BombProjectileData otherData = (BombProjectileData)((BombProjectileEntity)o).getModule().getData();
		return data.getRadius() - otherData.getRadius();
	}

	public static ArrayList<BombProjectileEntity> getSortedListFromStream(Stream<ModuleEntity<?>> bombEntities) {
		ArrayList<BombProjectileEntity> orderedBombEntities = new ArrayList<>();
		bombEntities.forEach(entity -> orderedBombEntities.add((BombProjectileEntity)entity));
		Collections.sort(orderedBombEntities);
		return orderedBombEntities;
	}
}
*/
