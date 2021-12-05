package net.foxmcloud.draconicadditions.network;

import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.items.ToolUpgrade;

import io.netty.buffer.ByteBuf;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChaosInjection implements IMessage {

	public PacketChaosInjection() {}

	@Override
	public void toBytes(ByteBuf buf) {}

	@Override
	public void fromBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<PacketChaosInjection, IMessage> {

		public Handler() {}

		@Override
		public IMessage onMessage(PacketChaosInjection message, MessageContext ctx) {
			PlayerEntityMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				ItemStack chest = player.inventory.armorInventory.get(2);
				if (chest.getItem() == DAFeatures.chaoticChest) {
					if (UpgradeHelper.getUpgradeLevel(chest, ToolUpgrade.ATTACK_DAMAGE) > 0) {
						NBTTagCompound chestNBT = chest.getTagCompound();
						if (chestNBT.getBoolean("injecting")) {
							player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosInjection.stop"), true);
							chestNBT.setBoolean("injecting", false);
						}
						else {
							player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosInjection.start"), true);
							chestNBT.setBoolean("injecting", true);
						}
					}
					else {
						player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosInjection.noupgrade"), true);
					}
				}
				else {
					player.sendStatusMessage(new TextComponentTranslation("msg.da.chaosInjection.incompatible"), true);
				}
			});
			return null;
		}
	}
}
