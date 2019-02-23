package net.foxmcloud.draconicadditions;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IModFeatures;
import com.brandon3055.brandonscore.registry.ModFeature;
import com.brandon3055.brandonscore.registry.ModFeatures;
import com.brandon3055.draconicevolution.client.creativetab.DETab;

import net.foxmcloud.draconicadditions.items.ChaoticEnergyCore;
import net.foxmcloud.draconicadditions.items.armor.ChaoticArmor;
import net.foxmcloud.draconicadditions.items.armor.InfusedPotatoArmor;
import net.foxmcloud.draconicadditions.items.armor.PotatoArmor;
import net.foxmcloud.draconicadditions.items.baubles.OverloadBelt;
import net.foxmcloud.draconicadditions.items.baubles.ShieldNecklace;
import net.foxmcloud.draconicadditions.items.baubles.VampiricShirt;
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
    
    @ModFeature(name = "inert_potato_helm", cTab = 0, stateOverride = "crafting#type=inertPotatoHelm")
    public static Item inertPotatoHelm = new Item();
    
    @ModFeature(name = "inert_potato_chest", cTab = 0, stateOverride = "crafting#type=inertPotatoChest")
    public static Item inertPotatoChest = new Item();
    
    @ModFeature(name = "inert_potato_legs", cTab = 0, stateOverride = "crafting#type=inertPotatoLegs")
    public static Item inertPotatoLegs = new Item();
    
    @ModFeature(name = "inert_potato_boots", cTab = 0, stateOverride = "crafting#type=inertPotatoBoots")
    public static Item inertPotatoBoots = new Item();
    
    @ModFeature(name = "chaotic_energy_core", cTab = 0, stateOverride = "crafting#type=chaoticECore")
    public static ChaoticEnergyCore chaoticEnergyCore = new ChaoticEnergyCore();
    
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
    
    //Chaotic Armor
    
    @ModFeature(name = "chaotic_helm", cTab = 0, stateOverride = "armor#type=chaoticHelm")
    public static ChaoticArmor chaoticHelm = new ChaoticArmor(0, EntityEquipmentSlot.HEAD);

    @ModFeature(name = "chaotic_chest", cTab = 0, stateOverride = "armor#type=chaoticChest")
    public static ChaoticArmor chaoticChest = new ChaoticArmor(1, EntityEquipmentSlot.CHEST);

    @ModFeature(name = "chaotic_legs", cTab = 0, stateOverride = "armor#type=chaoticLegs")
    public static ChaoticArmor chaoticLegs = new ChaoticArmor(2, EntityEquipmentSlot.LEGS);

    @ModFeature(name = "chaotic_boots", cTab = 0, stateOverride = "armor#type=chaoticBoots")
    public static ChaoticArmor chaoticBoots = new ChaoticArmor(3, EntityEquipmentSlot.FEET);
    
    //Shield Baubles //TODO: Remove comments once updating DE.
    
    //@ModFeature(name = "basic_shield_necklace", cTab = 0, stateOverride = "baubles#type=basicShieldNecklace")
    //public static ShieldNecklace basicShieldNecklace = new ShieldNecklace(0);
    
    //@ModFeature(name = "wyvern_shield_necklace", cTab = 0, stateOverride = "baubles#type=wyvernShieldNecklace")
    //public static ShieldNecklace wyvernShieldNecklace = new ShieldNecklace(1);
    
    //@ModFeature(name = "draconic_shield_necklace", cTab = 0, stateOverride = "baubles#type=draconicShieldNecklace")
    //public static ShieldNecklace draconicShieldNecklace = new ShieldNecklace(2);
    
    //Other Baubles
    
    @ModFeature(name = "overload_belt", cTab = 0, stateOverride = "baubles#type=overloadBelt")
    public static OverloadBelt overloadBelt = new OverloadBelt();
    
    @ModFeature(name = "vampiric_shirt", cTab = 0, stateOverride = "baubles#type=vampiricShirt")
    public static VampiricShirt vampiricShirt = new VampiricShirt();
}
