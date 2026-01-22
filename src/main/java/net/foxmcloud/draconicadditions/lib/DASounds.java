package net.foxmcloud.draconicadditions.lib;

import com.brandon3055.draconicevolution.handlers.DESounds;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Created by FoxMcloud5655 on 11/27/2019.
 * This stores all of the sound events for Draconic Additions.
 */

public class DASounds extends DESounds {
	
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, DraconicAdditions.MODID);

    public static void init(IEventBus modBus) {
        SOUNDS.register(modBus);
    }

	public static final DeferredHolder<SoundEvent, SoundEvent> hermal = SOUNDS.register("hermal", () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(DraconicAdditions.MODID, "hermal"), 16F));
	public static final DeferredHolder<SoundEvent, SoundEvent> unplug = SOUNDS.register("unplug", () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(DraconicAdditions.MODID, "unplug"), 16F));
	public static final DeferredHolder<SoundEvent, SoundEvent> boom   = SOUNDS.register("boom",   () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(DraconicAdditions.MODID, "boom"), 16F));
}
