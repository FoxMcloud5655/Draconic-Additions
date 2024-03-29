package net.foxmcloud.draconicadditions.modules.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.handlers.ProcessHandler;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.TileModuleContext;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.lib.WTFException;

import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.thread.EffectiveSide;

public class StableChaosEntity extends ModuleEntity<StableChaosData> implements Comparable {
	private static final double rfCostLimit = 1000000;
	private int chaos = 0;
	private float instability = 0;

	public StableChaosEntity(Module<StableChaosData> module) {
		super(module);
	}

	@Override
	public void tick(ModuleContext moduleContext) {
		if (!EffectiveSide.get().isServer()) return;
		if (chaos <= 0) {
			instability = 0;
			return;
		}
		if (instability > 0) {
			long rfCost = getRFCost();
			IOPStorage storage = moduleContext.getOpStorage();
			if (storage != null && storage.modifyEnergyStored(-rfCost) == Math.abs(rfCost)) {
				StableChaosData data = (StableChaosData)module.getData();
				instability -= instability > data.getMaxInstability() ? Math.min(10, instability - data.getMaxInstability()) : 0.25;
			}
			else {
				BlockPos expLoc;
				ServerLevel level;
				if (moduleContext instanceof StackModuleContext stackContext) {
					expLoc = stackContext.getEntity().blockPosition();
					level = (ServerLevel)stackContext.getEntity().level;
					//stackContext.getEntity().sendMessage(new TextComponent("You're lucky this didn't explode in your face."), Util.NIL_UUID);
				}
				else if (moduleContext instanceof TileModuleContext tileContext) {
					expLoc = tileContext.getTile().getBlockPos();
					level = (ServerLevel)tileContext.getTile().getLevel();
				}
				else {
					throw new WTFException("ModuleContext for StableChaosEntity wasn't in a stack or a tile?!");
				}
				ProcessExplosion explosionProcess = new ProcessExplosion(expLoc, 10, level, 0);
				ProcessHandler.addProcess(explosionProcess);
				chaos = 0;
				instability = 0;
			}
		}
	}

	public int getChaos() {
		return chaos;
	}

	public float getInstability() {
		return instability;
	}

	public long getRFCost() {
		return (long)Math.min(Math.pow(4, Math.min(instability, 1250) / 40) * (chaos / 10), rfCostLimit);
	}

	// Returns how much chaos was successfully pulled/pushed to/from this storage.
	public int modifyChaos(int amount) {
		int chaosToMod;
		StableChaosData data = (StableChaosData)module.getData();
		if (amount >= 0) {
			chaosToMod = Math.min(data.getMaxChaos() - chaos, amount);
		}
		else {
			chaosToMod = Math.max(-chaos, amount);
		}
		chaos += chaosToMod;
		instability += Math.abs(chaosToMod);
		return chaosToMod;
	}

	@Override
	public void writeToItemStack(ItemStack stack, ModuleContext context) {
		super.writeToItemStack(stack, context);
		stack.getOrCreateTag().putInt("chaos", chaos);
		stack.getOrCreateTag().putFloat("instability", instability);
	}

	@Override
	public void readFromItemStack(ItemStack stack, ModuleContext context) {
		super.readFromItemStack(stack, context);
		if (stack.hasTag()) {
			chaos = stack.getOrCreateTag().getInt("chaos");
			instability = stack.getOrCreateTag().getInt("instability");
		}
	}

	@Override
	public void writeToNBT(CompoundTag compound) {
		super.writeToNBT(compound);
		compound.putInt("chaos", chaos);
		compound.putFloat("instability", instability);
	}

	@Override
	public void readFromNBT(CompoundTag compound) {
		super.readFromNBT(compound);
		chaos = compound.getInt("chaos");
		instability = compound.getInt("instability");
	}

	@Override
	public void addToolTip(List<Component> tooltip) {
		StableChaosData data = (StableChaosData)module.getData();
		tooltip.add(new TranslatableComponent("info.da.storedChaos", chaos, data.getMaxChaos()));
		tooltip.add(new TranslatableComponent("info.da.instability", instability, data.getMaxInstability()));
		tooltip.add(new TranslatableComponent("info.da.opCost", getRFCost()));
	}

	@Override
	public int compareTo(@NotNull Object o) {
		StableChaosData data = (StableChaosData)module.getData();
		StableChaosData otherData = (StableChaosData)((StableChaosEntity)o).getModule().getData();
		return data.getMaxInstability() - otherData.getMaxInstability();
	}

	public static ArrayList<StableChaosEntity> getSortedListFromStream(Stream<ModuleEntity<?>> chaosEntities) {
		ArrayList<StableChaosEntity> orderedChaosEntities = new ArrayList<>();
		chaosEntities.forEach(entity -> orderedChaosEntities.add((StableChaosEntity)entity));
		Collections.sort(orderedChaosEntities);
		return orderedChaosEntities;
	}
}
