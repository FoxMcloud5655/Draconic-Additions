package net.foxmcloud.draconicadditions.lib;

import com.brandon3055.draconicevolution.lib.DESoundHandler;

import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Created by FoxMcloud5655 on 11/27/2019.
 * This stores all of the sound events for Draconic Additions.
 */
public class DASoundHandler extends DESoundHandler {
	public static final SoundEvent unplug;
	static {
		if (!Bootstrap.isRegistered()) {
			throw new RuntimeException("Accessed Sounds before Bootstrap!");
		}
		else {
			unplug = getRegisteredSoundEvent("draconicadditions:unplug");
		}
	}

	private static SoundEvent getRegisteredSoundEvent(String id) {
		SoundEvent soundevent = new SoundEvent(new ResourceLocation(id));
		SOUND_EVENTS.put(id, soundevent);
		return soundevent;
	}
}
