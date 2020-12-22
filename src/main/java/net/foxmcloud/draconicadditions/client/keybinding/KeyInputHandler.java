package net.foxmcloud.draconicadditions.client.keybinding;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.network.PacketChaosInjection;
import net.foxmcloud.draconicadditions.network.PacketOverloadBelt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyInputHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null) {
			onInput(player);
		}
	}

	private void onInput(EntityPlayer player) {
		if (KeyBindings.activateOverload.isPressed()) {
			DraconicAdditions.network.sendToServer(new PacketOverloadBelt());
		}
		if (KeyBindings.activateChaosInjection.isPressed()) {
			DraconicAdditions.network.sendToServer(new PacketChaosInjection());
		}
	}
}
