package net.foxmcloud.draconicadditions;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.brandonscore.registry.IModFeatures;
import com.brandon3055.brandonscore.registry.ModFeature;
import com.brandon3055.brandonscore.registry.ModFeatures;
import com.brandon3055.draconicevolution.blocks.tileentity.TileChaosCrystal;
import com.brandon3055.draconicevolution.items.ItemPersistent;

import net.foxmcloud.draconicadditions.blocks.ChaosCrystalStable;
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
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(DraconicAdditions.MODID)
@ModFeatures(modid = DraconicAdditions.MODID)
public class DAFeatures implements IModFeatures {

	private static CreativeTabs tabDA = new DATab(DraconicAdditions.MODID, "draconicadditions", 0);
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
	
    @ModFeature(name = "chaos_heart", stateOverride = "crafting#type=chaosHeart")
    public static ItemPersistent chaosHeart = new ItemPersistent();

	@ModFeature(name = "chaotic_energy_core", stateOverride = "crafting#type=chaoticECore")
	public static ChaoticEnergyCore chaoticEnergyCore = new ChaoticEnergyCore();
	
    @ModFeature(name = "hermal", stateOverride = "crafting#type=hermal", isActive = false)
    public static Hermal hermal = new Hermal();

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
	
	@ModFeature(name = "capacitor_supplier", tileEntity = TileCapacitorSupplier.class, itemBlock = ItemBlockBCore.class)
	public static CapacitorSupplier capacitorSupplier = new CapacitorSupplier();
	
	//@ModFeature(name = "chaos_infuser", tileEntity = TileChaosInfuser.class, itemBlock = ItemBlockBCore.class)
	//public static ChaosInfuser chaosInfuser = new ChaosInfuser();

	// Tools

	@ModFeature(name = "chaotic_staff_of_power")
	public static ChaoticStaffOfPower chaoticStaffOfPower = new ChaoticStaffOfPower();
	
	@ModFeature(name = "chaotic_bow")
	public static ChaoticBow chaoticBow = new ChaoticBow();

	@ModFeature(name = "chaos_container", stateOverride = "tools#type=chaoscontainer")
	public static ChaosContainer chaosContainer = new ChaosContainer();

	@ModFeature(name = "portable_wired_charger", variantMap = {
			"0:type=basic", "1:type=wyvern", "2:type=draconic", "3:type=chaotic",
			"4:type=basicactive", "5:type=wyvernactive", "6:type=draconicactive", "7:type=chaoticactive"})
	public static PortableWiredCharger pwc = new PortableWiredCharger();
	public static ItemStack pwcBasic = new ItemStack(pwc, 1, 0);
	public static ItemStack pwcWyvern = new ItemStack(pwc, 1, 1);
	public static ItemStack pwcDraconic = new ItemStack(pwc, 1, 2);
	public static ItemStack pwcChaotic = new ItemStack(pwc, 1, 3);

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
	
	@ModFeature(name = "hermal_helm", stateOverride = "armor#type=potatoHelm", isActive = false)
	public static HermalArmor hermalHelm = new HermalArmor(0, EntityEquipmentSlot.HEAD);

	@ModFeature(name = "hermal_chest", stateOverride = "armor#type=potatoChest", isActive = false)
	public static HermalArmor hermalChest = new HermalArmor(1, EntityEquipmentSlot.CHEST);

	@ModFeature(name = "hermal_legs", stateOverride = "armor#type=potatoLegs", isActive = false)
	public static HermalArmor hermalLegs = new HermalArmor(2, EntityEquipmentSlot.LEGS);

	@ModFeature(name = "hermal_boots", stateOverride = "armor#type=potatoBoots", isActive = false)
	public static HermalArmor hermalBoots = new HermalArmor(3, EntityEquipmentSlot.FEET);

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
	
	// Misc / Decor
	
	@ModFeature(name = "chaos_crystal_stable")
	public static ChaosCrystalStable chaosCrystalStable = new ChaosCrystalStable();
}
