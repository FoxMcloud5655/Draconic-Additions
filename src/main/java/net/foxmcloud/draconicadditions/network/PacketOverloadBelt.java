package net.foxmcloud.draconicadditions.network;

import com.brandon3055.brandonscore.BrandonsCore;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import net.foxmcloud.draconicadditions.client.keybinding.KeyBindings;
import net.foxmcloud.draconicadditions.items.baubles.OverloadBelt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOverloadBelt implements IMessage {
	
	public PacketOverloadBelt() {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<PacketOverloadBelt, IMessage> {
		
		public Handler() {
		}
		
		@Override
		public IMessage onMessage(PacketOverloadBelt message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
            	ItemStack belt = BaublesApi.getBaublesHandler(player).getStackInSlot(BaubleType.BELT.getValidSlots()[0]).copy();
            	if (belt.getItem() instanceof OverloadBelt) {
            		NBTTagCompound beltNBT = belt.getTagCompound();
            		if (beltNBT.getBoolean("Active")) beltNBT.setBoolean("Active", false);
            		else beltNBT.setBoolean("Active", true);
            		BaublesApi.getBaublesHandler(player).setStackInSlot(BaubleType.BELT.getValidSlots()[0], belt);
            	}
            });
			return null;
		}
	}
}
