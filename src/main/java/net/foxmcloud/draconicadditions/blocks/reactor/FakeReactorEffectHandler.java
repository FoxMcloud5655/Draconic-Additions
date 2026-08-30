package net.foxmcloud.draconicadditions.blocks.reactor;

import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.draconicevolution.client.DEParticles;

import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorComponent;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorCore;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorInjector;
import net.foxmcloud.draconicadditions.client.render.effect.FakeReactorBeamFX;
import net.foxmcloud.draconicadditions.client.sound.FakeReactorSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Created by brandon3055 on 11/02/2017.
 */
@Deprecated
public class FakeReactorEffectHandler {

    private TileFakeReactorCore reactor;
    @OnlyIn (Dist.CLIENT)
    private FakeReactorBeamFX[] effects;

    @OnlyIn (Dist.CLIENT)
    private FakeReactorSound reactorSound;

    public FakeReactorEffectHandler(TileFakeReactorCore reactor) {
        this.reactor = reactor;
    }

    @OnlyIn (Dist.CLIENT)
    public void updateEffects() {
        if (effects == null) {
            effects = new FakeReactorBeamFX[6];
        }

        if ((reactorSound == null || reactorSound.isStopped() || !Minecraft.getInstance().getSoundManager().isActive(reactorSound)) && reactor.reactorState.get().isShieldActive() && reactor.shieldCharge.get() > 0) {
            reactorSound = new FakeReactorSound(reactor);
            Minecraft.getInstance().getSoundManager().play(reactorSound);
        } else if (reactorSound != null && (!reactor.reactorState.get().isShieldActive() || reactor.shieldCharge.get() <= 0)) {
            reactorSound.donePlaying = true;
        }

        if (reactor.reactorState.get() == TileFakeReactorCore.ReactorState.INVALID || reactor.shieldAnimationState <= 0) {
            return;
        }

        for (Direction facing : Direction.values()) {
            int index = facing.get3DDataValue();
            TileFakeReactorComponent component = reactor.getComponent(facing);

            if (component == null) {
                if (effects[index] != null) {
                    effects[index].remove();
                    effects[index] = null;
                }
                continue;
            }

            if (effects[index] != null && effects[index].isAlive()) {
                effects[index].updateFX((float) reactor.shieldAnimationState, 1);//todo Power Stat
                continue;
            }

            FakeReactorBeamFX beamFX = new FakeReactorBeamFX((ClientLevel) reactor.getLevel(), Vec3D.getCenter(component.getBlockPos()), component.facing.get(), reactor, component instanceof TileFakeReactorInjector);
            effects[index] = beamFX;
            DEParticles.addParticleDirect(reactor.getLevel(), beamFX);
        }
    }

}
