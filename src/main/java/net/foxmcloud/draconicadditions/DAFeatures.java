package net.foxmcloud.draconicadditions;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IModFeatures;
import com.brandon3055.brandonscore.registry.ModFeature;
import com.brandon3055.brandonscore.registry.ModFeatures;

import net.foxmcloud.draconicadditions.blocks.chaosritual.ChaosStabilizerCore;
import net.foxmcloud.draconicadditions.blocks.chaosritual.tileentity.TileChaosStabilizerCore;
import net.foxmcloud.draconicadditions.blocks.machines.*;
import net.foxmcloud.draconicadditions.blocks.tileentity.*;
import net.foxmcloud.draconicadditions.client.creativetab.DATab;
import net.foxmcloud.draconicadditions.items.*;
import net.foxmcloud.draconicadditions.items.armor.*;
import net.foxmcloud.draconicadditions.items.baubles.*;
import net.foxmcloud.draconicadditions.items.tools.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(DraconicAdditions.MODID)
@ModFeatures(modid = DraconicAdditions.MODID)
public class DAFeatures implements IModFeatures {

	private static CreativeTabs tabDA = new DATab(CreativeTabs.getNextID(), DraconicAdditions.MODID, "draconicadditions", 0);
	private CreativeTabs[] tabs = new CreativeTabs[] {tabDA};

	@Nullable
	@Override
	public CreativeTabs getCreativeTab(Feature feature) {
		return feature.creativeTab() == -1 ? null : tabs[feature.creativeTab()];
	}

	// Crafting Components

	@ModFeature(name = "inert_potato_helm", stateOverride = "crafting#type=inertPotatoHelm")
	public static Item inertPotatoHelm = new Item();

	@ModFeature(name = "inert_potato_chest", stateOverride = "crafting#type=inertPotatoChest")
	public static Item inertPotatoChest = new Item();

	@ModFeature(name = "inert_potato_legs", stateOverride = "crafting#type=inertPotatoLegs")
	public static Item inertPotatoLegs = new Item();

	@ModFeature(name = "inert_potato_boots", stateOverride = "crafting#type=inertPotatoBoots")
	public static Item inertPotatoBoots = new Item();

	@ModFeature(name = "chaotic_energy_core", stateOverride = "crafting#type=chaoticECore")
	public static ChaoticEnergyCore chaoticEnergyCore = new ChaoticEnergyCore();

	// Blocks

	@ModFeature(name = "chaos_stabilizer_core", tileEntity = TileChaosStabilizerCore.class, itemBlock = ItemBlockBCore.class)
	public static ChaosStabilizerCore chaosStabilizerCore = new ChaosStabilizerCore();

	@ModFeature(name = "armor_generator", tileEntity = TileArmorGenerator.class, itemBlock = ItemBlockBCore.class)
	public static ArmorGenerator armorGenerator = new ArmorGenerator();

	@ModFeature(name = "chaotic_armor_generator", tileEntity = TileChaoticArmorGenerator.class, itemBlock = ItemBlockBCore.class)
	public static ChaoticArmorGenerator chaoticArmorGenerator = new ChaoticArmorGenerator();

	@ModFeature(name = "item_drainer", tileEntity = TileItemDrainer.class, itemBlock = ItemBlockBCore.class)
	public static ItemDrainer itemDrainer = new ItemDrainer();

	@ModFeature(name = "chaos_liquefier", tileEntity = TileChaosLiquefier.class, itemBlock = ItemBlockBCore.class)
	public static ChaosLiquefier chaosLiquefier = new ChaosLiquefier();

	// Tools

	@ModFeature(name = "chaotic_staff_of_power")
	public static ChaoticStaffOfPower chaoticStaffOfPower = new ChaoticStaffOfPower();

	@ModFeature(name = "chaos_container", stateOverride = "misc#type=normal")
	public static ChaosContainer chaosContainer = new ChaosContainer();

	// Potato Armor

	@ModFeature(name = "infused_potato_helm", stateOverride = "armor#type=infusedPotatoHelm")
	public static InfusedPotatoArmor infusedPotatoHelm = new InfusedPotatoArmor(0, EntityEquipmentSlot.HEAD);

	@ModFeature(name = "infused_potato_chest", stateOverride = "armor#type=infusedPotatoChest")
	public static InfusedPotatoArmor infusedPotatoChest = new InfusedPotatoArmor(1, EntityEquipmentSlot.CHEST);

	@ModFeature(name = "infused_potato_legs", stateOverride = "armor#type=infusedPotatoLegs")
	public static InfusedPotatoArmor infusedPotatoLegs = new InfusedPotatoArmor(2, EntityEquipmentSlot.LEGS);

	@ModFeature(name = "infused_potato_boots", stateOverride = "armor#type=infusedPotatoBoots")
	public static InfusedPotatoArmor infusedPotatoBoots = new InfusedPotatoArmor(3, EntityEquipmentSlot.FEET);

	@ModFeature(name = "potato_helm", stateOverride = "armor#type=potatoHelm")
	public static PotatoArmor potatoHelm = new PotatoArmor(0, EntityEquipmentSlot.HEAD);

	@ModFeature(name = "potato_chest", stateOverride = "armor#type=potatoChest")
	public static PotatoArmor potatoChest = new PotatoArmor(1, EntityEquipmentSlot.CHEST);

	@ModFeature(name = "potato_legs", stateOverride = "armor#type=potatoLegs")
	public static PotatoArmor potatoLegs = new PotatoArmor(2, EntityEquipmentSlot.LEGS);

	@ModFeature(name = "potato_boots", stateOverride = "armor#type=potatoBoots")
	public static PotatoArmor potatoBoots = new PotatoArmor(3, EntityEquipmentSlot.FEET);

	// Chaotic Armor

	@ModFeature(name = "chaotic_helm", stateOverride = "armor#type=chaoticHelm")
	public static ChaoticArmor chaoticHelm = new ChaoticArmor(0, EntityEquipmentSlot.HEAD);

	@ModFeature(name = "chaotic_chest", stateOverride = "armor#type=chaoticChest")
	public static ChaoticArmor chaoticChest = new ChaoticArmor(1, EntityEquipmentSlot.CHEST);

	@ModFeature(name = "chaotic_legs", stateOverride = "armor#type=chaoticLegs")
	public static ChaoticArmor chaoticLegs = new ChaoticArmor(2, EntityEquipmentSlot.LEGS);

	@ModFeature(name = "chaotic_boots", stateOverride = "armor#type=chaoticBoots")
	public static ChaoticArmor chaoticBoots = new ChaoticArmor(3, EntityEquipmentSlot.FEET);

	// Shield Baubles

	@ModFeature(name = "basic_shield_necklace", stateOverride = "baubles#type=basicShieldNecklace")
	public static ShieldNecklace basicShieldNecklace = new ShieldNecklace(0);

	@ModFeature(name = "wyvern_shield_necklace", stateOverride = "baubles#type=wyvernShieldNecklace")
	public static ShieldNecklace wyvernShieldNecklace = new ShieldNecklace(1);

	@ModFeature(name = "draconic_shield_necklace", stateOverride = "baubles#type=draconicShieldNecklace")
	public static ShieldNecklace draconicShieldNecklace = new ShieldNecklace(2);

	// Other Baubles

	@ModFeature(name = "overload_belt", stateOverride = "baubles#type=overloadBelt")
	public static OverloadBelt overloadBelt = new OverloadBelt();

	@ModFeature(name = "vampiric_shirt", stateOverride = "baubles#type=vampiricShirt")
	public static VampiricShirt vampiricShirt = new VampiricShirt();

	@ModFeature(name = "inertia_cancel_ring", stateOverride = "baubles#type=inertiacancelring")
	public static InertiaCancelRing inertiaCancelRing = new InertiaCancelRing();
}
