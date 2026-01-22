package net.foxmcloud.draconicadditions.modules.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.brandon3055.brandonscore.api.BCStreamCodec;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.handlers.ProcessHandler;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.TileModuleContext;
import com.brandon3055.draconicevolution.blocks.reactor.ProcessExplosion;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.lib.WTFException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.foxmcloud.draconicadditions.lib.DAItemData;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.util.thread.EffectiveSide;

public class StableChaosEntity extends ModuleEntity<StableChaosData> implements Comparable<StableChaosEntity> {
	private static final double rfCostLimit = 1000000;
	private int chaos = 0;
	private float instability = 0;
	
	public static final Codec<StableChaosEntity> CODEC = RecordCodecBuilder.create(builder -> builder.group(
		DEModules.codec().fieldOf("module").forGetter(ModuleEntity::getModule),
		Codec.INT.fieldOf("gridx").forGetter(ModuleEntity::getGridX),
		Codec.INT.fieldOf("gridy").forGetter(ModuleEntity::getGridY)
	).apply(builder, StableChaosEntity::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, StableChaosEntity> STREAM_CODEC = BCStreamCodec.composite(
		DEModules.streamCodec(), ModuleEntity::getModule,
		ByteBufCodecs.INT, ModuleEntity::getGridX,
		ByteBufCodecs.INT, ModuleEntity::getGridY,
		StableChaosEntity::new
	);

	public StableChaosEntity(Module<StableChaosData> module) {
		super(module);
	}
	
	@SuppressWarnings("unchecked")
	StableChaosEntity(Module<?> module, int gridX, int gridY) {
        super((Module<StableChaosData>) module, gridX, gridY);
	}
	
    @Override
    public ModuleEntity<?> copy() {
        return new StableChaosEntity(module, getGridX(), getGridY());
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
					level = (ServerLevel)stackContext.getEntity().level();
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
	public void saveEntityToStack(ItemStack stack, ModuleContext context) {
		stack.set(DAItemData.CHAOS, chaos);
		stack.set(DAItemData.INSTABILITY, instability);
	}

	@Override
	public void loadEntityFromStack(ItemStack stack, ModuleContext context) {
		chaos = stack.getOrDefault(DAItemData.CHAOS, chaos);
		instability = stack.getOrDefault(DAItemData.INSTABILITY, instability);
	}

	@Override
	public void addToolTip(List<Component> tooltip) {
		StableChaosData data = (StableChaosData)module.getData();
		tooltip.add(Component.translatable("info.da.storedChaos", chaos, data.getMaxChaos()));
		tooltip.add(Component.translatable("info.da.instability", instability, data.getMaxInstability()));
		tooltip.add(Component.translatable("info.da.opCost", getRFCost()));
	}
	
	@Override
	public int compareTo(StableChaosEntity o) {
		StableChaosData data = module.getData();
		StableChaosData otherData = o.getModule().getData();
		return data.getMaxInstability() - otherData.getMaxInstability();
	}

	public static ArrayList<StableChaosEntity> getSortedListFromStream(Stream<ModuleEntity<?>> chaosEntities) {
		ArrayList<StableChaosEntity> orderedChaosEntities = new ArrayList<>();
		chaosEntities.forEach(entity -> orderedChaosEntities.add((StableChaosEntity)entity));
		Collections.sort(orderedChaosEntities);
		return orderedChaosEntities;
	}
}
