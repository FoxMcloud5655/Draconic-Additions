package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.OPStorage;
import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.inventory.TileItemStackHandler;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ShieldData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.SimpleModuleHost;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileChaosHolderBase extends TileBCore {
	public OPStorage opStorage;
	public TileItemStackHandler itemHandler;

	public final ManagedInt chaos = register(new ManagedInt("chaos", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public SimpleModuleHost moduleHost = new SimpleModuleHost(TechLevel.CHAOTIC, 6, 6, ModuleCfg.removeInvalidModules, ModuleCategory.ENERGY).addAdditionalType(ModuleTypes.SHIELD_BOOST);

	public int getMaxChaos() {
		return moduleHost.getModuleData(ModuleTypes.SHIELD_BOOST, new ShieldData(0, 0)).shieldCapacity() * 10;
	}

	public boolean canRemoveModule(ModuleEntity entity) {
		return !(entity.getModule().getData() instanceof ShieldData data && getMaxChaos() - data.shieldCapacity() * 10 < chaos.get());
	}

	public TileChaosHolderBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
		capManager.setManaged("module_host", DECapabilities.MODULE_HOST_CAPABILITY, moduleHost).saveBoth().syncContainer();
	}
}
