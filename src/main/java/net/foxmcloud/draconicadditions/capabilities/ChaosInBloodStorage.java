package net.foxmcloud.draconicadditions.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ChaosInBloodStorage implements IStorage<IChaosInBlood> {

	@Override
	public NBTBase writeNBT(Capability capability, IChaosInBlood instance, EnumFacing side) {
		return new NBTTagFloat(instance.getChaos());
	}

	@Override
	public void readNBT(Capability capability, IChaosInBlood instance, EnumFacing side, NBTBase nbt) {
		instance.setChaos(((NBTTagFloat)nbt).getFloat());
	}
}
