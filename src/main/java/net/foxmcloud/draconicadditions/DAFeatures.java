package net.foxmcloud.draconicadditions;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IModFeatures;
import com.brandon3055.brandonscore.registry.ModFeature;
import com.brandon3055.brandonscore.registry.ModFeatures;
import com.brandon3055.draconicevolution.client.creativetab.DETab;

import net.foxmcloud.draconicadditions.items.armor.InfusedPotatoArmor;
import net.foxmcloud.draconicadditions.items.armor.PotatoArmor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(DraconicAdditions.MODID)
@ModFeatures(modid = DraconicAdditions.MODID)
public class DAFeatures implements IModFeatures {
	
    private static CreativeTabs tabDA = new DETab(CreativeTabs.getNextID(), DraconicAdditions.MODID, "draconicadditions", 0);
    private CreativeTabs[] tabs = new CreativeTabs[]{tabDA};

    @Nullable
    @Override
    public CreativeTabs getCreativeTab(Feature feature) {
        return feature.creativeTab() == -1 ? null : tabs[feature.creativeTab()];
    }

    //Crafting Components
    
    @ModFeature(name = "inert_potato_helm", cTab = 0, stateOverride = "armor#type=inertPotatoHelm")
    public static Item inertPotatoHelm = new Item();
    
    @ModFeature(name = "inert_potato_chest", cTab = 0, stateOverride = "armor#type=inertPotatoChest")
    public static Item inertPotatoChest = new Item();
    
    @ModFeature(name = "inert_potato_legs", cTab = 0, stateOverride = "armor#type=inertPotatoLegs")
    public static Item inertPotatoLegs = new Item();
    
    @ModFeature(name = "inert_potato_boots", cTab = 0, stateOverride = "armor#type=inertPotatoBoots")
    public static Item inertPotatoBoots = new Item();
    
    //Potato Armor
    
    @ModFeature(name = "infused_potato_helm", cTab = 0, stateOverride = "armor#type=infusedPotatoHelm")
    public static InfusedPotatoArmor infusedPotatoHelm = new InfusedPotatoArmor(0, EntityEquipmentSlot.HEAD);
    
    @ModFeature(name = "infused_potato_chest", cTab = 0, stateOverride = "armor#type=infusedPotatoChest")
    public static InfusedPotatoArmor infusedPotatoChest = new InfusedPotatoArmor(1, EntityEquipmentSlot.CHEST);
    
    @ModFeature(name = "infused_potato_legs", cTab = 0, stateOverride = "armor#type=infusedPotatoLegs")
    public static InfusedPotatoArmor infusedPotatoLegs = new InfusedPotatoArmor(2, EntityEquipmentSlot.LEGS);
    
    @ModFeature(name = "infused_potato_boots", cTab = 0, stateOverride = "armor#type=infusedPotatoBoots")
    public static InfusedPotatoArmor infusedPotatoBoots = new InfusedPotatoArmor(3, EntityEquipmentSlot.FEET);
    
    @ModFeature(name = "potato_helm", cTab = 0, stateOverride = "armor#type=potatoHelm")
    public static PotatoArmor potatoHelm = new PotatoArmor(0, EntityEquipmentSlot.HEAD);

    @ModFeature(name = "potato_chest", cTab = 0, stateOverride = "armor#type=potatoChest")
    public static PotatoArmor potatoChest = new PotatoArmor(1, EntityEquipmentSlot.CHEST);

    @ModFeature(name = "potato_legs", cTab = 0, stateOverride = "armor#type=potatoLegs")
    public static PotatoArmor potatoLegs = new PotatoArmor(2, EntityEquipmentSlot.LEGS);

    @ModFeature(name = "potato_boots", cTab = 0, stateOverride = "armor#type=potatoBoots")
    public static PotatoArmor potatoBoots = new PotatoArmor(3, EntityEquipmentSlot.FEET);
    

    

}
