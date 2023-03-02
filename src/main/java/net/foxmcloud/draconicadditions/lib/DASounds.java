package net.foxmcloud.draconicadditions.lib;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import java.lang.reflect.Field;

import com.brandon3055.draconicevolution.handlers.DESounds;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Created by FoxMcloud5655 on 11/27/2019.
 * This stores all of the sound events for Draconic Additions.
 */

@ObjectHolder(DraconicAdditions.MODID)
@Mod.EventBusSubscriber(modid = DraconicAdditions.MODID, bus = MOD)
public class DASounds extends DESounds {

	@ObjectHolder("hermal")
	public static SoundEvent hermal = getSoundEvent("hermal");
	
	@ObjectHolder("unplug")
	public static SoundEvent unplug = getSoundEvent("unplug");
	
	@ObjectHolder("unplug")
	public static SoundEvent boom = getSoundEvent("boom");
	
	public static SoundEvent getSoundEvent(final String soundName) {
		final ResourceLocation soundRL = new ResourceLocation(DraconicAdditions.MODID, soundName);
		return new SoundEvent(soundRL).setRegistryName(soundRL);
	}
	
	// A little hacky, but saves on coding.  Taken from the Minecraft mod called Alex's Mobs.
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		try {
			for (Field f : DASounds.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof SoundEvent) {
					event.getRegistry().register((SoundEvent) obj);
				}
			}
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
