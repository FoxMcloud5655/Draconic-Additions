package net.foxmcloud.draconicadditions.client.keybinding;

import org.lwjgl.input.Keyboard;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

	public static KeyBinding activateOverload = new KeyBinding("key.overload", KeyConflictContext.IN_GAME, KeyModifier.SHIFT, Keyboard.KEY_F, DraconicAdditions.NAME);

	public static void init() {
		ClientRegistry.registerKeyBinding(activateOverload);
	}
}
