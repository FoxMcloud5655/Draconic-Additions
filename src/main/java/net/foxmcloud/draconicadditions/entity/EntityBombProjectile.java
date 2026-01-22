/*
package net.foxmcloud.draconicadditions.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityBombProjectile extends AbstractHurtingProjectile {

	private int explosionPower = 1;

	public EntityBombProjectile(EntityType<? extends AbstractHurtingProjectile> entity, LivingEntity shooter, double xDir, double yDir, double zDir, Level world) {
		super(entity, shooter, xDir, yDir, zDir, world);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level().isClientSide) {
			Entity target = result.getEntity();
			boolean flag = target.hurt(damageSources().fellOutOfWorld(), 4.0F);
			if (this.getOwner() instanceof LivingEntity owner) {
				this.doEnchantDamageEffects(owner, target);
			}
		}
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (!this.level().isClientSide) {
			Entity entity = this.getOwner();
			boolean flag = net.neoforged.neoforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
			this.level().explode((Entity)null, this.getX(), this.getY(), this.getZ(), (float)this.explosionPower, flag, flag ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
			this.discard();
		}
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.PORTAL;
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource p_37381_, float p_37382_) {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("ExplosionPower", (byte)this.explosionPower);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.explosionPower = tag.getByte("ExplosionPower");
	}
}
*/
