package net.foxmcloud.draconicadditions.lib;

import com.mojang.serialization.Codec;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DAItemData {
	public static final DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, DraconicAdditions.MODID);

	public static void init(IEventBus modBus) {
		DATA.register(modBus);
	}

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> CHEAT_CHECK = DATA.register("cheat_check", () -> DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CHAOS = DATA.register("chaos", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> INSTABILITY = DATA.register("instability", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COUNTDOWN = DATA.register("countdown", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> INJECTING = DATA.register("injecting", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> STORED_HP = DATA.register("stored_hp", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> PREV_HP = DATA.register("prev_hp", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PREV_RATE = DATA.register("prev_rate", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WARNING_COUNTDOWN = DATA.register("warning_countdown", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PREV_WARNING_COUNTDOWN = DATA.register("prev_warning_countdown", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
}
