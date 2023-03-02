package net.foxmcloud.draconicadditions.modules.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.config.IntegerProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;

import net.foxmcloud.draconicadditions.modules.data.ChaosInjectorData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ChaosInjectorEntity extends ModuleEntity<ChaosInjectorData> implements Comparable {
	private static final int shieldCostPerChaos = 1;
	private static final int maxChaos = 80;

	private int chaos = 0;
	private boolean injecting = false;
	private float storedHP = 0;

	private IntegerProperty rate;

	public ChaosInjectorEntity(Module<ChaosInjectorData> module) {
		super(module);

		addProperty(rate = new IntegerProperty("chaos_injector_mod.rate", 0).setFormatter(ConfigProperty.IntegerFormatter.RAW)
				.range(-module.getData().getInjectRate(), module.getData().getInjectRate()));
		this.savePropertiesToItem = true;
	}

	public int getRate() {
		return rate.getValue();
	}

	@Override
	public void tick(ModuleContext context) {
        if (context instanceof StackModuleContext stackContext) {
            LivingEntity entity = stackContext.getEntity();
			if (getRate() > 0 && entity.tickCount % Math.max(20 - getRate(), 1) == 0) {
				if (entity.getHealth() > 0 && !injecting) {
					entity.setHealth(entity.getHealth()); //Drain HP
				}
				else if (injecting) {
					//add chaos
				}
			}
			else if (getRate() < 0 && entity.tickCount % Math.max(20 - getRate(), 1) == 0) {
				if (!injecting) {
					//add HP
				}
				else if (injecting) {
					//Drain chaos into modules?  Or just vent it.
					if (chaos == 0) {
						//End wither effect and add HP immediately
					}
				}
			}
			if (injecting) {
				//Update HP
				//if ()
			}
		}
	}

	public int getChaos() {
		return chaos;
	}

	public float getStoredHP() {
		return storedHP;
	}
	
	public boolean isChaosInBlood() {
		return injecting;
	}
	
	public void startInjection() {
		injecting = true;
	}

	// Returns how much chaos was successfully pulled/pushed to/from this storage.
	public int modifyChaos(int amount) {
		int chaosToMod;
		if (amount >= 0) {
			chaosToMod = Math.min(maxChaos - chaos, amount);
		}
		else {
			chaosToMod = Math.max(-chaos, amount);
		}
		chaos += chaosToMod;
		if (chaos <= 0 && injecting) {
			injecting = false;
		}
		return chaosToMod;
	}

	@Override
	public void writeToItemStack(ItemStack stack, ModuleContext context) {
		super.writeToItemStack(stack, context);
		stack.getOrCreateTag().putInt("chaos", chaos);
		stack.getOrCreateTag().putBoolean("injecting", injecting);
		stack.getOrCreateTag().putFloat("storedHP", storedHP);
	}

	@Override
	public void readFromItemStack(ItemStack stack, ModuleContext context) {
		super.readFromItemStack(stack, context);
		if (stack.hasTag()) {
			chaos = stack.getOrCreateTag().getInt("chaos");
			injecting = stack.getOrCreateTag().getBoolean("injecting");
			storedHP = stack.getOrCreateTag().getFloat("storedHP");
		}
	}

	@Override
	public void writeToNBT(CompoundTag compound) {
		super.writeToNBT(compound);
		compound.putInt("chaos", chaos);
		compound.putBoolean("injecting", injecting);
		compound.putFloat("storedHP", storedHP);
	}

	@Override
	public void readFromNBT(CompoundTag compound) {
		super.readFromNBT(compound);
		chaos = compound.getInt("chaos");
		injecting = compound.getBoolean("injecting");
		storedHP = compound.getFloat("storedHP");
	}

	@Override
	public int compareTo(@NotNull Object o) {
		ChaosInjectorData data = (ChaosInjectorData)module.getData();
		ChaosInjectorData otherData = (ChaosInjectorData)((ChaosInjectorEntity)o).getModule().getData();
		return data.getInjectRate() - otherData.getInjectRate();
	}

	public static ArrayList<ChaosInjectorEntity> getSortedListFromStream(Stream<ModuleEntity<?>> chaosEntities) {
		ArrayList<ChaosInjectorEntity> orderedChaosEntities = new ArrayList<>();
		chaosEntities.forEach(entity -> orderedChaosEntities.add((ChaosInjectorEntity)entity));
		Collections.sort(orderedChaosEntities);
		return orderedChaosEntities;
	}
}
