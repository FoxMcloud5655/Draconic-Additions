package net.foxmcloud.draconicadditions.modules;

import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.lib.*;
import com.google.common.collect.ImmutableList;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class ModuleGrid extends com.brandon3055.draconicevolution.api.modules.lib.ModuleGrid {

    private final TileChaosHolderBase tile;

    public ModuleGrid(ModuleHostContainer container, Inventory player, TileChaosHolderBase tile) {
        super(container, player);
        this.tile = tile;
    }

    @Override
    public InstallResult cellClicked(GridPos pos, double x, double y, int button, ClickType clickType) {
        ItemStack stack = player.player.containerMenu.getCarried();
        Module<?> module = ModuleItem.getModule(stack);
        boolean holdingStack = !stack.isEmpty();
        ModuleContext context = container.getModuleContext();

        //Sanity Checks
        if ((holdingStack && module == null) || !pos.isValidCell()) {
            return null; //Player tried to insert an item that is not a valid module
        }

        //Really this could be pick up or drop off
        if (clickType == ClickType.PICKUP) {
            if (holdingStack) { //Try to insert module
                ModuleEntity entity = module.createEntity();
                entity.setPos(pos.getGridX(), pos.getGridY());
                InstallResult result = checkInstall(entity);
                if (result.resultType == InstallResult.InstallResultType.YES) {
                    entity.readFromItemStack(stack, context);
                    getModuleHost().addModule(entity, context);
                    stack.shrink(1);
                    onGridChange();
                    return null;
                }
                return result;
            }
            else if (pos.hasEntity()) { //Try to extract module
                ModuleEntity entity = pos.getEntity();
                if (!tile.canRemoveModule(entity)) {
                    return null;
                }
                ItemStack extracted = new ItemStack(entity.getModule().getItem());
                entity.writeToItemStack(extracted, context);
                getModuleHost().removeModule(entity, context);
                player.player.containerMenu.setCarried(extracted);
                onGridChange();
            }
        }
        else if (clickType == ClickType.QUICK_MOVE) {
            if (pos.hasEntity()) { //Try to transfer module
                ModuleEntity entity = pos.getEntity();
                if (!tile.canRemoveModule(entity)) {
                    return null;
                }
                ItemStack extracted = new ItemStack(entity.getModule().getItem());
                entity.writeToItemStack(extracted, context);
                if (player.add(extracted)) {
                    getModuleHost().removeModule(entity, context);
                    onGridChange();
                }
            }
        }
        else if (clickType == ClickType.PICKUP_ALL && module != null) {
            for (ModuleEntity entity : ImmutableList.copyOf(getModuleHost().getModuleEntities())) {
                if (entity.getModule() == module && tile.canRemoveModule(entity)) {
                    ItemStack modStack = new ItemStack(module.getItem());
                    entity.writeToItemStack(modStack, context);
                    if (ItemStack.isSameItemSameTags(stack, modStack) && stack.getCount() < stack.getMaxStackSize()) {
                        stack.grow(1);
                        getModuleHost().removeModule(entity, context);
                    }
                }
                onGridChange();
            }
        }
        else if (clickType == ClickType.CLONE) {
            if (player.player.getAbilities().instabuild && player.player.inventoryMenu.getCarried().isEmpty() && pos.hasEntity()) {
                ModuleEntity entity = pos.getEntity();
                ItemStack modStack = new ItemStack(entity.getModule().getItem());
                entity.writeToItemStack(modStack, context);
                player.player.containerMenu.setCarried(modStack);
            }
        }
        return null;
    }
}
