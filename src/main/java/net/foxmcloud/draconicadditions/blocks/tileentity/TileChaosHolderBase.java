package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.OPStorage;
import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.inventory.TileItemStackHandler;
import com.brandon3055.brandonscore.lib.IRSSwitchable;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.ShieldData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleEntity;
import com.brandon3055.draconicevolution.api.modules.lib.SimpleModuleHost;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.ModuleCfg;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileChaosHolderBase extends TileBCore implements IRSSwitchable {
	public OPStorage opStorage;
	public TileItemStackHandler itemHandler;
	
	protected static final int maxCharge = 200;

	public final ManagedInt chaos = register(new ManagedInt("chaos", 0, DataFlags.SAVE_BOTH_SYNC_TILE, DataFlags.TRIGGER_UPDATE));

	public SimpleModuleHost moduleHost = new SimpleModuleHost(TechLevel.CHAOTIC, 6, 6, ModuleCfg.removeInvalidModules, ModuleCategory.ENERGY);

	public int getMaxChaos() {
		return moduleHost.getModuleData(ModuleTypes.SHIELD_BOOST, new ShieldData(0, 0)).shieldCapacity() * 10;
	}

	public boolean canRemoveModule(ModuleEntity<?> entity) {
		return !(entity.getModule().getData() instanceof ShieldData data && getMaxChaos() - data.shieldCapacity() * 10 < chaos.get());
	}

	public TileChaosHolderBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
		moduleHost.addAdditionalType(ModuleTypes.SHIELD_BOOST);
		capManager.set(DECapabilities.Host.BLOCK, moduleHost); // NOT CORRECT, since it doesn't save the modules when saved or loaded.
		//capManager.setManaged("module_host", DECapabilities.Host.BLOCK, moduleHost).saveBoth().syncContainer(); // This is correct, but can't compile.
	}
	
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}
	
	public static int calcChaos(ItemStack stack) {
		switch (chaosID(stack.getItem())) {
		case 1:
			return 11664;
		case 2:
			return 1296;
		case 3:
			return 144;
		case 4:
			return 16;
		case 5:
			return 20000;
		default:
			return 0;
		}
	}

	public int calcCharge(ItemStack stack) {
		switch (chaosID(stack.getItem())) {
		case 1:
			return (int)(maxCharge / 1.5);
		case 2:
			return maxCharge / 4;
		case 3:
			return maxCharge / 8;
		case 4:
			return maxCharge / 16;
		case 5:
			return maxCharge;
		default:
			return 0;
		}
	}

	public static int chaosID(Item item) {
		if (item == DEContent.CHAOS_SHARD.get())
			return 1;
		else if (item == DEContent.CHAOS_FRAG_LARGE.get())
			return 2;
		else if (item == DEContent.CHAOS_FRAG_MEDIUM.get())
			return 3;
		else if (item == DEContent.CHAOS_FRAG_SMALL.get())
			return 4;
		else if (item == DAContent.chaosHeart.get())
			return 5;
		else return 0;
	}
}
