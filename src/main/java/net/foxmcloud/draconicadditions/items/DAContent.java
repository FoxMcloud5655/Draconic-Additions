package net.foxmcloud.draconicadditions.items;

import static com.brandon3055.brandonscore.api.TechLevel.*;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import com.brandon3055.brandonscore.lib.TechPropBuilder;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.items.armor.InfusedPotatoArmor;
import net.foxmcloud.draconicadditions.items.curios.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DraconicAdditions.MODID, bus = MOD)
@ObjectHolder(DraconicAdditions.MODID)
public class DAContent {
	public static transient ArrayList<ResourceLocation> ITEM_REGISTRY_ORDER = new ArrayList<>();

	// Crafting Components

	@ObjectHolder("inert_potato_helm")  public static Item inertPotatoHelm;
	@ObjectHolder("inert_potato_chest") public static Item inertPotatoChest;
	@ObjectHolder("inert_potato_legs")  public static Item inertPotatoLegs;
	@ObjectHolder("inert_potato_boots") public static Item inertPotatoBoots;
	@ObjectHolder("chaos_heart")        public static Item chaosHeart;
	//@ObjectHolder("hermal")             public static Hermal hermal;
	
	// Armor
	
	@ObjectHolder("infused_potato_helm")  public static InfusedPotatoArmor infusedPotatoHelm;
	@ObjectHolder("infused_potato_chest") public static InfusedPotatoArmor infusedPotatoChest;
	@ObjectHolder("infused_potato_legs")  public static InfusedPotatoArmor infusedPotatoLegs;
	@ObjectHolder("infused_potato_boots") public static InfusedPotatoArmor infusedPotatoBoots;

	// Curios

	@ObjectHolder("wyvern_necklace")   public static ModularNecklace necklaceWyvern;
	@ObjectHolder("draconic_necklace") public static ModularNecklace necklaceDraconic;
	@ObjectHolder("chaotic_necklace")  public static ModularNecklace necklaceChaotic;
	@ObjectHolder("wyvern_harness")    public static ModularHarness  harnessWyvern;
	@ObjectHolder("draconic_harness")  public static ModularHarness  harnessDraconic;
	@ObjectHolder("chaotic_harness")   public static ModularHarness  harnessChaotic;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		Supplier<Object[]> tabDA = () -> new Object[]{necklaceWyvern, chaosHeart, inertPotatoHelm};
		CyclingItemGroup DAGroup = new CyclingItemGroup(DraconicAdditions.MODID + ".items", 40, tabDA, ITEM_REGISTRY_ORDER);

		TechPropBuilder wyvernTier = new TechPropBuilder(WYVERN).maxStackSize(1).group(DAGroup).rarity(Rarity.UNCOMMON).maxDamage(-1);
		TechPropBuilder draconicTier = new TechPropBuilder(DRACONIC).maxStackSize(1).group(DAGroup).rarity(Rarity.RARE).maxDamage(-1);
		TechPropBuilder chaoticTier = new TechPropBuilder(CHAOTIC).maxStackSize(1).group(DAGroup).rarity(Rarity.EPIC).maxDamage(-1);
		TechPropBuilder hermalTier = new TechPropBuilder(CHAOTIC).maxStackSize(1).group(DAGroup).rarity(Rarity.EPIC).maxDamage(-1);

		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_helm"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_chest"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_legs"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_boots"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("chaos_heart"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlotType.HEAD).setRegistryName("infused_potato_helm"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlotType.CHEST).setRegistryName("infused_potato_chest"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlotType.LEGS).setRegistryName("infused_potato_legs"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlotType.FEET).setRegistryName("infused_potato_boots"));
		registerItem(event, new ModularNecklace(wyvernTier).setRegistryName("wyvern_necklace"));
		registerItem(event, new ModularNecklace(draconicTier).setRegistryName("draconic_necklace"));
		registerItem(event, new ModularNecklace(chaoticTier).setRegistryName("chaotic_necklace"));
		registerItem(event, new ModularHarness(wyvernTier).setRegistryName("wyvern_harness"));
		registerItem(event, new ModularHarness(draconicTier).setRegistryName("draconic_harness"));
		registerItem(event, new ModularHarness(chaoticTier).setRegistryName("chaotic_harness"));

		//registerItem(event, new Hermal(new Item.Properties().stacksTo(1).tab(DAGroup)).setRegistryName("hermal"));
	}

	// Blocks

	/*
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
	 */

	private static void registerItem(RegistryEvent.Register<Item> event, Item item) {
		event.getRegistry().register(item);
		ITEM_REGISTRY_ORDER.add(item.getRegistryName());
	}
}
