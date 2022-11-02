package net.foxmcloud.draconicadditions.lib;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.client.utils.CyclingItemGroup;
import com.brandon3055.brandonscore.inventory.ContainerBCTile;
import com.brandon3055.draconicevolution.init.TechProperties;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.blocks.machines.ChaosLiquefier;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquefier;
import net.foxmcloud.draconicadditions.inventory.GUILayoutFactories;
import net.foxmcloud.draconicadditions.items.Hermal;
import net.foxmcloud.draconicadditions.items.armor.InfusedPotatoArmor;
import net.foxmcloud.draconicadditions.items.curios.ModularHarness;
import net.foxmcloud.draconicadditions.items.curios.ModularNecklace;
import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = DraconicAdditions.MODID, bus = MOD)
@ObjectHolder(DraconicAdditions.MODID)
public class DAContent {
	public static transient ArrayList<ResourceLocation> ITEM_REGISTRY_ORDER = new ArrayList<>();

	// Tile Entities
	@ObjectHolder("chaos_liquefier")
	public static BlockEntityType<TileChaosLiquefier> tileChaosLiquefier;

	@SubscribeEvent
	public static void registerBlockEntity(RegistryEvent.Register<BlockEntityType<?>> event) {
		event.getRegistry().register(BlockEntityType.Builder.of(TileChaosLiquefier::new, chaosLiquefier).build(null).setRegistryName("chaos_liquefier"));
	}

	@ObjectHolder("chaos_liquefier")
	public static MenuType<ContainerBCTile<TileChaosLiquefier>> containerChaosLiquefier;

	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
		event.getRegistry().register(IForgeMenuType.create((id, inv, data) -> new ContainerBCTile<>(containerChaosLiquefier, id, inv, data, GUILayoutFactories.CHAOS_LIQUEFIER_LAYOUT)).setRegistryName("chaos_liquefier"));
	}

	@ObjectHolder("chaos_liquefier") public static ChaosLiquefier chaosLiquefier;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Properties machine = Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).strength(3.0F, 8F).noOcclusion().requiresCorrectToolForDrops();
		event.getRegistry().register(new ChaosLiquefier(machine).setRegistryName("chaos_liquefier"));
	}

	// Crafting Components

	@ObjectHolder("inert_potato_helm")  public static Item inertPotatoHelm;
	@ObjectHolder("inert_potato_chest") public static Item inertPotatoChest;
	@ObjectHolder("inert_potato_legs")  public static Item inertPotatoLegs;
	@ObjectHolder("inert_potato_boots") public static Item inertPotatoBoots;
	@ObjectHolder("chaos_heart")        public static Item chaosHeart;
	@ObjectHolder("hermal")             public static Hermal hermal;

	// Armor

	@ObjectHolder("infused_potato_helm")  public static InfusedPotatoArmor infusedPotatoHelm;
	@ObjectHolder("infused_potato_chest") public static InfusedPotatoArmor infusedPotatoChest;
	@ObjectHolder("infused_potato_legs")  public static InfusedPotatoArmor infusedPotatoLegs;
	@ObjectHolder("infused_potato_boots") public static InfusedPotatoArmor infusedPotatoBoots;

	// Tools

	@ObjectHolder("chaos_container") public static ChaosContainer chaosContainer;

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

        TechProperties wyvernTier = (TechProperties) new TechProperties(TechLevel.WYVERN).tab(DAGroup).rarity(Rarity.UNCOMMON).durability(-1).fireResistant();
        TechProperties draconicTier = (TechProperties) new TechProperties(TechLevel.DRACONIC).tab(DAGroup).rarity(Rarity.RARE).durability(-1).fireResistant();
        TechProperties chaoticTier = (TechProperties) new TechProperties(TechLevel.CHAOTIC).tab(DAGroup).rarity(Rarity.EPIC).durability(-1).fireResistant();
        TechProperties hermalTier = (TechProperties) new TechProperties(TechLevel.CHAOTIC).tab(DAGroup).rarity(Rarity.UNCOMMON).durability(-1).fireResistant()
        	.food(new FoodProperties.Builder().alwaysEat().nutrition(0).saturationMod(0).build());

		// Blocks

		registerItem(event, new ItemBlockBCore(chaosLiquefier, new Item.Properties().tab(DAGroup)).setRegistryName(Objects.requireNonNull(chaosLiquefier.getRegistryName())));

		// Items

		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_helm"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_chest"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_legs"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("inert_potato_boots"));
		registerItem(event, new Item(new Item.Properties().tab(DAGroup)).setRegistryName("chaos_heart"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlot.HEAD).setRegistryName("infused_potato_helm"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlot.CHEST).setRegistryName("infused_potato_chest"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlot.LEGS).setRegistryName("infused_potato_legs"));
		registerItem(event, new InfusedPotatoArmor(new Item.Properties().tab(DAGroup), EquipmentSlot.FEET).setRegistryName("infused_potato_boots"));
		registerItem(event, new ChaosContainer(chaoticTier).setRegistryName("chaos_container"));
		registerItem(event, new ModularNecklace(wyvernTier).setRegistryName("wyvern_necklace"));
		registerItem(event, new ModularNecklace(draconicTier).setRegistryName("draconic_necklace"));
		registerItem(event, new ModularNecklace(chaoticTier).setRegistryName("chaotic_necklace"));
		registerItem(event, new ModularHarness(wyvernTier).setRegistryName("wyvern_harness"));
		registerItem(event, new ModularHarness(draconicTier).setRegistryName("draconic_harness"));
		registerItem(event, new ModularHarness(chaoticTier).setRegistryName("chaotic_harness"));
		registerItem(event, new Hermal(hermalTier).setRegistryName("hermal"));
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
