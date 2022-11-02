package net.foxmcloud.draconicadditions.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ChaosInBloodProvider<T extends INBTSerializable<CompoundTag>> implements ICapabilitySerializable<CompoundTag> {

	public static final Capability<IChaosInBlood> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>() {});
	private T capability;
	
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return capability == PLAYER_CAP ? LazyOptional.of(() -> capability).cast() : LazyOptional.empty();
	}

    @Override
    public CompoundTag serializeNBT() {
        return capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        capability.deserializeNBT(nbt);
    }
}

