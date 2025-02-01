package net.foxmcloud.draconicadditions.mixins;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.entities.UndyingEntity;
import com.brandon3055.draconicevolution.client.render.hud.ShieldHudElement;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import net.foxmcloud.draconicadditions.items.curios.ModularNecklace;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Comparator;
import java.util.List;

@Mixin(ShieldHudElement.class)
public class HUDTotemDisplay {
	@ModifyVariable(method = "tick", at = @At("STORE"), name = "totems")
	private List<UndyingEntity> fixTotemCount(List<UndyingEntity> totems) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return totems;
		ItemStack necklaceStack = EquipmentManager.findItem((e) -> e.getItem() instanceof ModularNecklace, mc.player);
		LazyOptional<ModuleHost> optionalNecklaceModuleHost = necklaceStack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
		if (!necklaceStack.isEmpty() && optionalNecklaceModuleHost.isPresent()) {
			ModuleHost necklaceHost = optionalNecklaceModuleHost.orElseThrow(IllegalStateException::new);
			List<UndyingEntity> entities = necklaceHost.getEntitiesByType(ModuleTypes.UNDYING)
				.map((e) -> (UndyingEntity) e)
				.sorted(Comparator.comparing((e) -> e.getModule().getModuleTechLevel().index))
				.toList();
			if (!totems.containsAll(entities)) {
				totems.addAll(entities);
			}
		}
		return totems;
	}
}
