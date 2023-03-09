package net.foxmcloud.draconicadditions.modules.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.config.IntegerProperty;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.entities.ShieldControlEntity;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.StackModuleContext;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;
import com.brandon3055.draconicevolution.network.DraconicNetwork;

import net.foxmcloud.draconicadditions.lib.DAModules;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.foxmcloud.draconicadditions.modules.data.ChaosInjectorData;
import net.foxmcloud.draconicadditions.modules.data.StableChaosData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class ChaosInjectorEntity extends ModuleEntity<ChaosInjectorData> implements Comparable {
	public static final DamageSource injectionDeath = new DamageSource("chaosInjection").bypassInvul().bypassArmor().bypassMagic();
	private static final int maxChaos = 40;
	private static final int hpDrainAmount = 1;
	private static final double shieldDrainDivider = 2;
	private static final boolean shieldCooldownWhenInjecting = true;

	private int chaos = 0;
	private boolean isChaotic = false;
	private float storedHP = 0;
	private int prevRate = 0;
	private float prevHP = 1;
	private int warningCountdown = 0;
	private int prevWarningCountdown = 0;

	private IntegerProperty rate;

	public ChaosInjectorEntity(Module<ChaosInjectorData> module) {
		super(module);
		addProperty(rate = new IntegerProperty("chaos_injector.rate", 0).setFormatter(ConfigProperty.IntegerFormatter.RAW)
				.range(-module.getData().getInjectRate(), module.getData().getInjectRate()));
		this.savePropertiesToItem = true;
	}

	public int getRate() {
		return rate.getValue();
	}

	@Override
	public void tick(ModuleContext context) {
		if (context instanceof StackModuleContext stackContext) {
			if (!stackContext.isEquipped() && !isChaotic) {
				return;
			}
			if (warningCountdown > 0) {
				warningCountdown--;
			}
			LivingEntity entity = stackContext.getEntity();
			if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
				return;
			}
			ShieldControlEntity shield = host.getEntitiesByType(com.brandon3055.draconicevolution.api.modules.ModuleTypes.SHIELD_CONTROLLER).map(e -> (ShieldControlEntity) e).findAny().orElse(null);
			if (getRate() == 0 && isChaotic) {
				rate.setValue(1);
			}
			boolean shouldTick = !entity.level.isClientSide && getRate() != 0 && (entity.tickCount % Math.max(20 / Math.abs(getRate()), 1) == 0);
			if (getRate() > 0 && shouldTick) {
				if (!isChaotic) {
					if (entity.getHealth() < hpDrainAmount) {
						if (!entity.level.isClientSide) {
							if (modifyChaosInStorage(-1) == -1) {
								modifyChaos(1);
							}
							else if (entity instanceof ServerPlayer player){
								player.displayClientMessage(new TranslatableComponent("info.da.chaos_injector.storageEmpty").withStyle(ChatFormatting.YELLOW), true);
							}
						}
					}
					else {
						drainBlood(entity, hpDrainAmount);
					}
				}
				else if (isChaotic && !entity.level.isClientSide) {
					modifyChaos(1);
				}
			}
			else if (getRate() < 0 && shouldTick) {
				if (!isChaotic && storedHP > 0) {
					drainBlood(entity, -hpDrainAmount);
				}
				else if (isChaotic && !entity.level.isClientSide) {
					if (modifyChaosInStorage(1) == 1) {
						modifyChaos(-1);
					}
					else if (entity instanceof ServerPlayer player) {
						StableChaosData allData = host.getModuleData(ModuleTypes.STABLE_CHAOS);
						player.displayClientMessage(allData != null && allData.getMaxChaos() > 0 ? new TranslatableComponent("info.da.chaos_injector.storageFull").withStyle(ChatFormatting.RED) : new TranslatableComponent("info.da.chaos_injector.noStorage").withStyle(ChatFormatting.RED), true);
					}
					else if (entity instanceof Player player) {
						player.level.playSound(player, new BlockPos(player.getX(), player.getY(), player.getZ()), DESounds.beam, SoundSource.MASTER, 1.0F, 2.0F);
					}
					if (chaos == 0) {
						entity.setHealth(1);
						if (storedHP >= 1) {
							storedHP -= 1;
						}
					}
				}
			}
			if (entity instanceof Player && (isChaotic || getRate() > 0)) {
				((Player)entity).getFoodData().tickTimer = 0;
			}
			if (isChaotic) {
				double pointsToDrain = shieldCooldownWhenInjecting ? maxChaos / 8 : chaos / shieldDrainDivider;
				if (shield != null && entity instanceof Player player && warningCountdown <= 0) {
					double factor = (shield.getShieldPoints() / (shield.getShieldCapacity() + (maxChaos / (shieldDrainDivider * 2) * 100))) * 2;
					if (factor <= 1) {
						int cooldownAmount = Math.max((int)Math.round(factor * 40), 2);
						float pitch = 1.5F + ((float)(1 - factor) * 0.5F);
						prevWarningCountdown = cooldownAmount;
						if (player.level.isClientSide) {
							player.level.playSound(player, new BlockPos(player.getX(), player.getY(), player.getZ()), DESounds.beam, SoundSource.MASTER, 1.0F, pitch);
						}
						else {
							((ServerPlayer)player).displayClientMessage(new TranslatableComponent("info.da.chaos_injector.shieldLow").withStyle(ChatFormatting.RED), true);
						}
						warningCountdown = cooldownAmount;
					}
				}
				if (entity.level.isClientSide) {
					entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 2, 1, false, false, false));
					entity.setHealth(chaos / (maxChaos / 20F));
				}
				else {
					entity.removeAllEffects();
					entity.setHealth(0.001F);
					boolean shouldDie = !stackContext.isEquipped();
					if (shield != null) {
						if (shield.isShieldEnabled()) {
							if (shield.getShieldPoints() <= pointsToDrain) {
								shouldDie = true;
							}
							shield.subtractShieldPoints(pointsToDrain);
							shield.setShieldCoolDown(shieldCooldownWhenInjecting ? 1 : 0);
						}
						else shouldDie = true;
					}
					else shouldDie = true;
					if (shouldDie) {
						entity.getCombatTracker().recordDamage(injectionDeath, prevHP, Float.MAX_VALUE / 5F);
						DraconicNetwork.sendExplosionEffect(((ServerLevel)entity.level).dimension(), entity.blockPosition(), Math.min(10, chaos * 4), false);
						chaos = 0;
						isChaotic = false;
						rate.setValue(-1);
						storedHP = 0;
						prevHP = 1;
						boolean hasActuallyDied = false;
						int timesToKillEntity = 100;
						while (!hasActuallyDied && timesToKillEntity-- >= 0) {
							entity.setHealth(0);
							entity.die(injectionDeath);
							hasActuallyDied = entity.isDeadOrDying();
						}
					}
				}
			}
			if (!isChaotic) {
				if (getRate() <= 0) {
					prevHP = entity.getHealth();
				}
				if (getRate() < 0 && entity.getHealth() <= 1 && entity.level.isClientSide) {
					entity.removeEffectNoUpdate(MobEffects.WITHER);
				}
				if (getRate() > 0) {
					entity.setHealth(prevHP);
					if (entity instanceof Player player && warningCountdown <= 0) {
						int cooldownAmount = Math.max(Math.round(prevHP + 2), 4);
						float pitch = 1.5F + ((float)(1 - prevHP / entity.getMaxHealth()) * 0.5F);
						prevWarningCountdown = cooldownAmount;
						if (shield == null) {
							if (player.level.isClientSide) {
								player.level.playSound(player, new BlockPos(player.getX(), player.getY(), player.getZ()), DESounds.beam, SoundSource.MASTER, 1.0F, pitch);
							}
							else {
								((ServerPlayer)player).displayClientMessage(new TranslatableComponent("info.da.chaos_injector.noShield").withStyle(ChatFormatting.RED), true);
							}
							warningCountdown = cooldownAmount;
						}
						else if (!shield.isShieldEnabled()) {
							if (player.level.isClientSide) {
								player.level.playSound(player, new BlockPos(player.getX(), player.getY(), player.getZ()), DESounds.beam, SoundSource.MASTER, 1.0F, pitch);
							}
							else {
								((ServerPlayer)player).displayClientMessage(new TranslatableComponent("info.da.chaos_injector.shieldDisabled").withStyle(ChatFormatting.RED), true);
							}
							warningCountdown = cooldownAmount;
						}
						else if (shieldCooldownWhenInjecting && shield.getShieldPoints() < 1500 / shieldDrainDivider) {
							if (!player.level.isClientSide) {
								((ServerPlayer)player).displayClientMessage(new TranslatableComponent("info.da.chaos_injector.shieldCapacityLow").withStyle(ChatFormatting.YELLOW), true);
							}
							warningCountdown = 20;
							prevWarningCountdown = warningCountdown; 
						}
					}
				}
			}
			if (entity instanceof ServerPlayer player && warningCountdown == prevWarningCountdown / 2) {
				player.displayClientMessage(new TranslatableComponent(""), true);
			}
			prevRate = rate.getValue();
		}
	}

	// Amount is the float to drain from HP, returns amount drained.
	private float drainBlood(LivingEntity entity, float amount) {
		float prevHP = entity.getHealth();
		if (prevHP <= 1 && amount > 0) {
			amount = entity.getHealth() - 0.001F;
		}
		entity.setHealth(entity.getHealth() - Math.max(amount, -storedHP));
		float removedHP = prevHP - entity.getHealth();
		storedHP += removedHP;
		this.prevHP = entity.getHealth();
		return removedHP;
	}

	// Returns the amount successfully pushed/pulled to/from chaos storage modules.  Positive values push, negative values pull.
	private int modifyChaosInStorage(int amount) {
		int chaosToModify = amount;
		StableChaosData allData = host.getModuleData(ModuleTypes.STABLE_CHAOS);
		if (allData != null && allData.getMaxChaos() > 0) {
			Stream<ModuleEntity<?>> chaosEntities = host.getEntitiesByType(ModuleTypes.STABLE_CHAOS);
			ArrayList<StableChaosEntity> sortedChaosEntities = StableChaosEntity.getSortedListFromStream(chaosEntities);
			for (StableChaosEntity ce : sortedChaosEntities) {
				StableChaosData data = (StableChaosData)ce.getModule().getData();
				if ((chaosToModify > 0 && ce.getChaos() < data.getMaxChaos()) || (chaosToModify < 0 && ce.getChaos() > 0)) {
					chaosToModify -= ce.modifyChaos(chaosToModify);
				}
				if (chaosToModify == 0) {
					break;
				}
			}
		}
		return amount - chaosToModify;
	}

	public int getChaos() {
		return chaos;
	}

	public float getStoredHP() {
		return storedHP;
	}

	public boolean isChaosInBlood() {
		return isChaotic;
	}

	// Returns how much chaos was successfully pulled/pushed to/from this storage.
	public int modifyChaos(int amount) {
		int chaosToMod;
		if (amount >= 0) {
			isChaotic = true;
			chaosToMod = Math.min(maxChaos - chaos, amount);
		}
		else {
			chaosToMod = Math.max(-chaos, amount);
		}
		chaos += chaosToMod;
		if (chaos <= 0) {
			isChaotic = false;
		}
		return chaosToMod;
	}

	@Override
	public void writeToItemStack(ItemStack stack, ModuleContext context) {
		super.writeToItemStack(stack, context);
		stack.getOrCreateTag().putInt("chaos", chaos);
		stack.getOrCreateTag().putBoolean("injecting", isChaotic);
		stack.getOrCreateTag().putFloat("storedHP", storedHP);
		stack.getOrCreateTag().putFloat("prevRate", prevRate);
		stack.getOrCreateTag().putFloat("prevHP", prevHP);
		stack.getOrCreateTag().putInt("warningCountdown", warningCountdown);
		stack.getOrCreateTag().putInt("prevWarningCountdown", prevWarningCountdown);
	}

	@Override
	public void readFromItemStack(ItemStack stack, ModuleContext context) {
		super.readFromItemStack(stack, context);
		if (stack.hasTag()) {
			chaos = stack.getOrCreateTag().getInt("chaos");
			isChaotic = stack.getOrCreateTag().getBoolean("injecting");
			storedHP = stack.getOrCreateTag().getFloat("storedHP");
			prevRate = stack.getOrCreateTag().getInt("prevRate");
			prevHP = stack.getOrCreateTag().getFloat("prevHP");
			warningCountdown = stack.getOrCreateTag().getInt("warningCountdown");
			prevWarningCountdown = stack.getOrCreateTag().getInt("prevWarningCountdown");
		}
	}

	@Override
	public void writeToNBT(CompoundTag compound) {
		super.writeToNBT(compound);
		compound.putInt("chaos", chaos);
		compound.putBoolean("injecting", isChaotic);
		compound.putFloat("storedHP", storedHP);
		compound.putFloat("prevRate", prevRate);
		compound.putFloat("prevHP", prevHP);
		compound.putInt("warningCountdown", warningCountdown);
		compound.putInt("prevWarningCountdown", prevWarningCountdown);
	}

	@Override
	public void readFromNBT(CompoundTag compound) {
		super.readFromNBT(compound);
		chaos = compound.getInt("chaos");
		isChaotic = compound.getBoolean("injecting");
		storedHP = compound.getFloat("storedHP");
		prevRate = compound.getInt("prevRate");
		prevHP = compound.getFloat("prevHP");
		warningCountdown = compound.getInt("warningCountdown");
		prevWarningCountdown = compound.getInt("prevWarningCountdown");
	}

	@Override
	public int compareTo(@NotNull Object o) {
		ChaosInjectorData data = (ChaosInjectorData)module.getData();
		ChaosInjectorData otherData = (ChaosInjectorData)((ChaosInjectorEntity)o).getModule().getData();
		return data.getInjectRate() - otherData.getInjectRate();
	}
	
	@Override
    public boolean moduleClicked(Player player, double x, double y, int button, ClickType clickType) {
		boolean cantInteract = isChaotic || getRate() > 0;
		if (cantInteract && player instanceof ServerPlayer sPlayer) {
			sPlayer.displayClientMessage(new TranslatableComponent("info.da.chaos.cantmove", new TranslatableComponent("item.draconicadditions.chaos_injector_module").getString()).withStyle(ChatFormatting.RED), true);
		}
		return cantInteract;
    }

	public static ChaosInjectorEntity getInjectorEntity(LivingEntity entity) {
		List<ItemStack> items = EquipmentManager.findItems(e -> e.getItem() instanceof IModularItem, entity);
		for (ItemStack stack : items) {
			IModularItem item = ((IModularItem)stack.getItem());
			LazyOptional<ModuleHost> cap = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
			if (!cap.isPresent()) {
				continue;
			}
			ModuleHost host = cap.orElseThrow(IllegalStateException::new);
			ArrayList<ChaosInjectorEntity> entities = getSortedListFromStream(host.getEntitiesByType(ModuleTypes.CHAOS_INJECTOR));
			if (entities.isEmpty()) {
				continue;
			}
			return entities.get(0);
		}
		return null;
	}

	public static ArrayList<ChaosInjectorEntity> getSortedListFromStream(Stream<ModuleEntity<?>> chaosEntities) {
		ArrayList<ChaosInjectorEntity> orderedInjectorEntities = new ArrayList<>();
		chaosEntities.forEach(entity -> orderedInjectorEntities.add((ChaosInjectorEntity)entity));
		Collections.sort(orderedInjectorEntities);
		return orderedInjectorEntities;
	}
}
