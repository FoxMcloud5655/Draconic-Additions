package net.foxmcloud.draconicadditions.capabilities;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ChaosInBloodStorage implements IStorage<IChaosInBlood> {

	@Override
	public INBT writeNBT(Capability<IChaosInBlood> capability, IChaosInBlood instance, Direction side) {
		return FloatNBT.valueOf(instance.getChaos());
	}

	@Override
	public void readNBT(Capability<IChaosInBlood> capability, IChaosInBlood instance, Direction side, INBT nbt) {
		instance.setChaos(((FloatNBT)nbt).getAsFloat());
	}
}
