package net.foxmcloud.draconicadditions.items.curios;

import static com.brandon3055.draconicevolution.init.ModuleCfg.removeInvalidModules;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.ElytraEnabledItem;
import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.entities.FlightEntity;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.TechProperties;

import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.items.IModularEnergyItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class ModularNecklace extends Item implements IModularEnergyItem, ElytraEnabledItem {

	private TechLevel techLevel;
	
	public ModularNecklace(TechProperties props) {
		super(props);
		techLevel = props.getTechLevel();
	}
	
	@Override
	public TechLevel getTechLevel() {
		return techLevel;
	}

	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, 1 + techLevel.index, 1 + techLevel.index, "necklace", removeInvalidModules);
		host.addCategories(ModuleCategory.ALL, ModuleCategory.ARMOR_HEAD, ModuleCategory.ARMOR_CHEST);
		return host;
	}

	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		long capacity = (long)(EquipCfg.getBaseEnergy(techLevel) * DAConfig.necklaceCapacityMultiplier);
		return new ModularOPStorage(host, capacity, capacity / 64);
	}

    @Override
	public boolean canElytraFlyBC(ItemStack stack, LivingEntity entity) {
        ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElseThrow(IllegalStateException::new);
        FlightEntity flight = host.getEntitiesByType(ModuleTypes.FLIGHT).map(e -> (FlightEntity) e).findAny().orElse(null);
        return flight != null && flight.getElytraEnabled();
    }

    @Override
    public boolean elytraFlightTickBC(ItemStack stack, LivingEntity entity, int flightTicks) {
        LazyOptional<IOPStorage> power = stack.getCapability(DECapabilities.OP_STORAGE);
        boolean creative = entity instanceof Player player && player.getAbilities().instabuild;
        power.ifPresent(storage -> {
            int energy = EquipCfg.elytraFlightEnergy;
            if (storage.getOPStored() < energy && !creative) {
                storage.modifyEnergyStored(-10);
                Vec3 motion = entity.getDeltaMovement();
                entity.setDeltaMovement(motion.x * 0.95, motion.y > 0 ? motion.y * 0.95 : motion.y, motion.z * 0.95);

            } else{
                if (entity.isSprinting()) {
                    ModuleHost host = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY).orElseThrow(IllegalStateException::new);
                    FlightEntity module = (FlightEntity)host.getEntitiesByType(ModuleTypes.FLIGHT).findAny().orElse(null);
                    double flightSpeed = module == null ? 0 : module.getElytraBoost();
                    if (flightSpeed > 0) {
                        double speed = 1.5D * flightSpeed;
                        double accel = 0.01 * flightSpeed;
                        Vec3 look = entity.getLookAngle();
                        Vec3 motion = entity.getDeltaMovement();
                        entity.setDeltaMovement(motion.add(
                                look.x * accel + (look.x * speed - motion.x) * accel,
                                look.y * accel + (look.y * speed - motion.y) * accel,
                                look.z * accel + (look.z * speed - motion.z) * accel
                        ));
                        energy += EquipCfg.getElytraEnergy(module.getModule().getModuleTechLevel()) * flightSpeed;
                    }
                }
    public boolean isBarVisible(ItemStack stack) {
        return damageBarVisible(stack);
    }

                if (!entity.level.isClientSide && !creative) {
                    storage.modifyEnergyStored(-energy);
                }
            }
        });
    @Override
    public int getBarWidth(ItemStack stack) {
        return damageBarWidth(stack);
    }

        return true;
    @Override
    public int getBarColor(ItemStack stack) {
        return damageBarColour(stack);
    }
}
