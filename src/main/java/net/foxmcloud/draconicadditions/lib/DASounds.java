package net.foxmcloud.draconicadditions.lib;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

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
	@ObjectHolder("unplug")
	public static SoundEvent unplug;

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(new SoundEvent(new ResourceLocation(DraconicAdditions.MODID, "unplug")).setRegistryName("unplug"));
	}
}
