package net.foxmcloud.draconicadditions.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ChaosInBloodProvider implements ICapabilitySerializable<CompoundNBT> {

	@CapabilityInject(IChaosInBlood.class)
	public static final Capability<IChaosInBlood> PLAYER_CAP = null;
	
	private IChaosInBlood instance = PLAYER_CAP.getDefaultInstance();
	private final LazyOptional<IChaosInBlood> lazyInstance = LazyOptional.of(PLAYER_CAP::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		
		return capability == PLAYER_CAP ? lazyInstance.cast() : lazyInstance.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT)PLAYER_CAP.getStorage().writeNBT(PLAYER_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		PLAYER_CAP.getStorage().readNBT(PLAYER_CAP, this.instance, null, nbt);
	}
}

