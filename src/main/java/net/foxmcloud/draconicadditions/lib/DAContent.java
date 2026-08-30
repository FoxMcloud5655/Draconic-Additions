package net.foxmcloud.draconicadditions.lib;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.draconicevolution.api.DataComponentAccessor;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleProvider;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.blocks.reactor.ReactorComponent;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorInjector;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.TechProperties;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularEnergyItem;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.blocks.machines.*;
import net.foxmcloud.draconicadditions.blocks.reactor.FakeReactorComponent;
import net.foxmcloud.draconicadditions.blocks.reactor.FakeReactorCore;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorCore;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorInjector;
import net.foxmcloud.draconicadditions.blocks.reactor.tileentity.TileFakeReactorStabilizer;
import net.foxmcloud.draconicadditions.blocks.tileentity.*;
import net.foxmcloud.draconicadditions.inventory.*;
import net.foxmcloud.draconicadditions.items.*;
import net.foxmcloud.draconicadditions.items.armor.*;
import net.foxmcloud.draconicadditions.items.curios.*;
import net.foxmcloud.draconicadditions.items.tools.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DAContent {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, DraconicAdditions.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, DraconicAdditions.MODID);
	public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DraconicAdditions.MODID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, DraconicAdditions.MODID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, DraconicAdditions.MODID);

	public static final TechProperties hermalTier = (TechProperties) new TechProperties(TechLevel.CHAOTIC).rarity(Rarity.UNCOMMON).durability(-1).fireResistant()
			.food(new FoodProperties.Builder().alwaysEdible().nutrition(0).saturationModifier(0).build());

	public static void init(IEventBus modBus) {
		BLOCKS.register(modBus);
		ITEMS.register(modBus);
		TILES_ENTITIES.register(modBus);
		MENU_TYPES.register(modBus);
		ENTITY_TYPES.register(modBus);
		modBus.addListener(DAContent::registerCapabilities);
	}

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		DAContent.ITEMS.getEntries().forEach(holder -> {
			Item item = holder.get();
			if (item instanceof IModularItem) {
				event.registerItem(DECapabilities.Host.ITEM, (stack, v) -> getItemHostCap(stack), item);
				if (item instanceof IModularEnergyItem) {
					event.registerItem(CapabilityOP.ITEM, (stack, v) -> getEnergyCap(stack), item);
					event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, v) -> getEnergyCap(stack), item);
				}
			}
			if (item instanceof IDEEquipment) {
				EquipmentManager.registerCapability(event, item);
			}
			if (item instanceof ModuleProvider<?> provider) {
				event.registerItem(DECapabilities.Module.ITEM, (stack, context) -> provider, item);
			}
		});

		DAModules.ITEMS.getEntries().forEach(holder -> {
			Item item = holder.get();
			if (item instanceof ModuleProvider<?> provider) {
				event.registerItem(DECapabilities.Module.ITEM, (stack, context) -> provider, item);
			}
		});

		TileChaosCrystalizer.register(event);
		TileChaosExtractor.register(event);
		TileChaosInfuser.register(event);
		TileChaosLiquifier.register(event);
	}

	// Stolen from DE's CapabilityData until it's made public rather than private!

	private static ModuleHostImpl getItemHostCap(ItemStack stack) {
		if (!(stack.getItem() instanceof IModularItem item)) {
			throw new IllegalStateException("ITEM_HOST_DATA can only be used on an ItemStack who's item implements IModularItem!");
		}
		ModuleHostImpl host = item.createHostCapForRegistration(stack);
		assert host != null;
		host.updateDataAccess(DataComponentAccessor.itemStack(stack));
		return host;
	}

	private static ModularOPStorage getEnergyCap(ItemStack stack) {
		if (!(stack.getItem() instanceof IModularEnergyItem item)) {
			throw new IllegalStateException("ITEM_HOST_DATA can only be used on an ItemStack who's item implements IModularEnergyItem!");
		}
		ModularOPStorage storage = item.createOPCapForRegistration(stack);
		assert storage != null;
		storage.updateDataAccess(DataComponentAccessor.itemStack(stack));
		return storage;
	}

	// Tile Entities

	public static final DeferredHolder<Block, ChaosLiquifier> chaosLiquifier = BLOCKS.register("chaos_liquifier", () -> new ChaosLiquifier(DEContent.HARDENED_MACHINE));
	public static final DeferredHolder<Item, ItemBlockBCore> itemChaosLiquifier = ITEMS.register("chaos_liquifier", () -> new ItemBlockBCore(chaosLiquifier.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChaosLiquifier>> tileChaosLiquifier = TILES_ENTITIES.register("chaos_liquifier", () -> BlockEntityType.Builder.of(TileChaosLiquifier::new, chaosLiquifier.get()).build(null));
	public static final DeferredHolder<MenuType<?>, MenuType<ChaosBaseMenu>> menuChaosLiquifier = MENU_TYPES.register("chaos_liquifier", () -> IMenuTypeExtension.create(ChaosLiquifierMenu::new));

	public static final DeferredHolder<Block, ChaosInfuser> chaosInfuser     = BLOCKS.register("chaos_infuser",   () -> new ChaosInfuser(DEContent.HARDENED_MACHINE));
	public static final DeferredHolder<Item, ItemBlockBCore> itemChaosInfuser = ITEMS.register("chaos_infuser", () -> new ItemBlockBCore(chaosInfuser.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChaosInfuser>> tileChaosInfuser = TILES_ENTITIES.register("chaos_infuser", () -> BlockEntityType.Builder.of(TileChaosInfuser::new, chaosInfuser.get()).build(null));
	public static final DeferredHolder<MenuType<?>, MenuType<ChaosBaseMenu>> menuChaosInfuser = MENU_TYPES.register("chaos_infuser", () -> IMenuTypeExtension.create(ChaosInfuserMenu::new));

	public static final DeferredHolder<Block, ChaosExtractor> chaosExtractor = BLOCKS.register("chaos_extractor", () -> new ChaosExtractor(DEContent.HARDENED_MACHINE));
	public static final DeferredHolder<Item, ItemBlockBCore> itemChaosExtractor = ITEMS.register("chaos_extractor", () -> new ItemBlockBCore(chaosExtractor.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChaosExtractor>> tileChaosExtractor = TILES_ENTITIES.register("chaos_extractor", () -> BlockEntityType.Builder.of(TileChaosExtractor::new, chaosExtractor.get()).build(null));
	public static final DeferredHolder<MenuType<?>, MenuType<ChaosBaseMenu>> menuChaosExtractor = MENU_TYPES.register("chaos_extractor", () -> IMenuTypeExtension.create(ChaosExtractorMenu::new));

	public static final DeferredHolder<Block, ChaosCrystalizer> chaosCrystalizer = BLOCKS.register("chaos_crystalizer", () -> new ChaosCrystalizer(DEContent.HARDENED_MACHINE));
	public static final DeferredHolder<Item, ItemBlockBCore> itemChaosCrystalizer = ITEMS.register("chaos_crystalizer", () -> new ItemBlockBCore(chaosCrystalizer.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileChaosCrystalizer>> tileChaosCrystalizer = TILES_ENTITIES.register("chaos_crystalizer", () -> BlockEntityType.Builder.of(TileChaosCrystalizer::new, chaosCrystalizer.get()).build(null));
	public static final DeferredHolder<MenuType<?>, MenuType<ChaosBaseMenu>> menuChaosCrystalizer = MENU_TYPES.register("chaos_crystalizer", () -> IMenuTypeExtension.create(ChaosCrystalizerMenu::new));

	// Fake Reactor

	public static final DeferredHolder<Block, FakeReactorCore> fakeReactorCore = BLOCKS.register("fake_reactor_core", () -> new FakeReactorCore(DEContent.HARDENED_MACHINE));
	public static final DeferredHolder<Item, ItemBlockBCore> itemFakeReactorCore = ITEMS.register("fake_reactor_core", () -> new ItemBlockBCore(fakeReactorCore.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFakeReactorCore>> tileFakeReactorCore = TILES_ENTITIES.register("fake_reactor_core", () -> BlockEntityType.Builder.of(TileFakeReactorCore::new, fakeReactorCore.get()).build(null));
	public static final DeferredHolder<MenuType<?>, MenuType<FakeReactorMenu>> menuFakeReactorCore = MENU_TYPES.register("fake_reactor_core", () -> IMenuTypeExtension.create(FakeReactorMenu::new));

	public static final DeferredHolder<Block, FakeReactorComponent> fakeReactorStabilizer = BLOCKS.register("fake_reactor_stabilizer", () -> new FakeReactorComponent(Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5.0F, 6000F).noOcclusion(), false));
	public static final DeferredHolder<Item, ItemBlockBCore> itemFakeReactorStabilizer = ITEMS.register("fake_reactor_stabilizer", () ->  new ItemBlockBCore(fakeReactorStabilizer.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFakeReactorStabilizer>> tileFakeReactorStabilizer = TILES_ENTITIES.register("fake_reactor_stabilizer", () -> BlockEntityType.Builder.of(TileFakeReactorStabilizer::new, fakeReactorStabilizer.get()).build(null));

	public static final DeferredHolder<Block, FakeReactorComponent> fakeReactorInjector = BLOCKS.register("fake_reactor_injector", () -> new FakeReactorComponent(Properties.of().mapColor(MapColor.COLOR_GRAY).strength(5.0F, 6000F).noOcclusion(), true));
	public static final DeferredHolder<Item, ItemBlockBCore> itemFakeReactorInjector = ITEMS.register("fake_reactor_injector", () ->  new ItemBlockBCore(fakeReactorInjector.get(), new Item.Properties()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFakeReactorInjector>> tileFakeReactorInjector = TILES_ENTITIES.register("fake_reactor_injector", () -> BlockEntityType.Builder.of(TileFakeReactorInjector::new, fakeReactorInjector.get()).build(null));

	// Music Discs - Format stolen from Alex's Caves; thanks for showing me how to do this.

	public static final ResourceKey<JukeboxSong> JUKEBOX_SONG_HERMAL = ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(DraconicAdditions.MODID, "hermal"));

	// Crafting Components

	public static final DeferredHolder<Item, Item> inertPotatoHelm  = ITEMS.register("inert_potato_helm",  () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> inertPotatoChest = ITEMS.register("inert_potato_chest", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> inertPotatoLegs  = ITEMS.register("inert_potato_legs",  () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> inertPotatoBoots = ITEMS.register("inert_potato_boots", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> chaosHeart       = ITEMS.register("chaos_heart",        () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Hermal> hermal         = ITEMS.register("hermal",             () -> new Hermal(hermalTier));

	// Armor

	public static final DeferredHolder<Item, InfusedPotatoArmor> infusedPotatoHelm  = ITEMS.register("infused_potato_helm",  () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.HELMET));
	public static final DeferredHolder<Item, InfusedPotatoArmorChest> infusedPotatoChest = ITEMS.register("infused_potato_chest", () -> new InfusedPotatoArmorChest(new Item.Properties()));
	public static final DeferredHolder<Item, InfusedPotatoArmor> infusedPotatoLegs  = ITEMS.register("infused_potato_legs",  () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.LEGGINGS));
	public static final DeferredHolder<Item, InfusedPotatoArmor> infusedPotatoBoots = ITEMS.register("infused_potato_boots", () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.BOOTS));

	// Tools

	public static final DeferredHolder<Item, ChaosContainer> chaosContainer = ITEMS.register("chaos_container", () -> new ChaosContainer(DEContent.CHAOTIC_TOOLS));

	// Curios

	public static final DeferredHolder<Item, ModularNecklace> necklaceWyvern   = ITEMS.register("wyvern_necklace",   () -> new ModularNecklace(DEContent.WYVERN_TOOLS));
	public static final DeferredHolder<Item, ModularNecklace> necklaceDraconic = ITEMS.register("draconic_necklace", () -> new ModularNecklace(DEContent.DRACONIC_TOOLS));
	public static final DeferredHolder<Item, ModularNecklace> necklaceChaotic  = ITEMS.register("chaotic_necklace",  () -> new ModularNecklace(DEContent.CHAOTIC_TOOLS));
	//public static final DeferredHolder<Item, ModularHarness>  harnessWyvern    = ITEMS.register("wyvern_harness",    () -> new ModularHarness(DEContent.WYVERN_TOOLS));
	//public static final DeferredHolder<Item, ModularHarness>  harnessDraconic  = ITEMS.register("draconic_harness",  () -> new ModularHarness(DEContent.DRACONIC_TOOLS));
	//public static final DeferredHolder<Item, ModularHarness>  harnessChaotic   = ITEMS.register("chaotic_harness",   () -> new ModularHarness(DEContent.CHAOTIC_TOOLS));

	// Blocks

	/*

	@ModFeature(name = "armor_generator", tileEntity = TileArmorGenerator.class, itemBlock = ItemBlockBCore.class)
	public static ArmorGenerator armorGenerator = new ArmorGenerator();

	@ModFeature(name = "chaotic_armor_generator", tileEntity = TileChaoticArmorGenerator.class, itemBlock = ItemBlockBCore.class)
	public static ChaoticArmorGenerator chaoticArmorGenerator = new ChaoticArmorGenerator();

	// Tools

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
}
