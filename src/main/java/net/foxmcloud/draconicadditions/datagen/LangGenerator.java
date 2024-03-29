package net.foxmcloud.draconicadditions.datagen;

import javax.annotation.Nonnull;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleType;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.lib.DAModules;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGenerator extends LanguageProvider {
	public LangGenerator(DataGenerator gen) {
		super(gen, DraconicAdditions.MODID, "en_us");
	}

	private void blocks(PrefixHelper helper) {
		helper.add(DAContent.chaosLiquefier, "Chaos Liquefier");
		helper.add(DAContent.chaosInfuser, "Chaos Infuser");
	}

	private void items(PrefixHelper helper) {
		helper.setPrefix("item." + DraconicAdditions.MODID);
		helper.add(DAContent.chaosHeart,         "Chaos Heart");
		helper.add(DAContent.inertPotatoHelm,    "Inert Potato Helmet");
		helper.add(DAContent.inertPotatoChest,   "Inert Potato Chestplate");
		helper.add(DAContent.inertPotatoLegs,    "Inert Potato Leggings");
		helper.add(DAContent.inertPotatoBoots,   "Inert Potato Boots");
		helper.add(DAContent.infusedPotatoHelm,  "Infused Potato Helmet");
		helper.add(DAContent.infusedPotatoChest, "Infused Potato Chestplate");
		helper.add(DAContent.infusedPotatoLegs,  "Infused Potato Leggings");
		helper.add(DAContent.infusedPotatoBoots, "Infused Potato Boots");
		helper.add(DAContent.chaosContainer,     "Chaos Container");
		helper.add(DAContent.necklaceWyvern,     "Wyvern Necklace");
		helper.add(DAContent.necklaceDraconic,   "Draconic Necklace");
		helper.add(DAContent.necklaceChaotic,    "Chaotic Necklace");
		helper.add(DAContent.harnessWyvern,      "Wyvern Harness");
		helper.add(DAContent.harnessDraconic,    "Draconic Harness");
		helper.add(DAContent.harnessChaotic,     "Chaotic Harness");
		helper.add(DAContent.hermal,             "hermal");
		helper.add("hermal.desc",                "hermal?");
	}

	private void itemGroups(PrefixHelper helper) {
		helper.setPrefix("itemGroup." + DraconicAdditions.MODID);
		helper.add("items",   "Draconic Additions");
		helper.add("modules", "Draconic Additions Modules");
	}

	private void modules(PrefixHelper helper) {
		helper.setPrefix("module." + DraconicAdditions.MODID);
		helper.add(DAModules.chaoticAutoFeed,   "Chaotic Auto Feed Module");
		helper.add(ModuleTypes.TICK_ACCEL,      "Tick Accelerator");
		helper.add(DAModules.draconicTickAccel, "Draconic Tick Accelerator");
		helper.add(DAModules.chaoticTickAccel,  "Chaotic Tick Accelerator");
		helper.add("tick_accel.name",           "Additional Ticks");
		helper.add("tick_accel.value",          "%s Ticks");
		helper.add(ModuleTypes.STABLE_CHAOS,    "Chaos Stabilizer");
		helper.add(DAModules.semiStableChaos,   "Semi-Stable Chaos Holder");
		helper.add(DAModules.stableChaos,       "Stable Chaos Holder");
		helper.add(DAModules.unstableChaos,     "Unstable Chaos Holder");
		helper.add("maxChaos.name",             "Max Chaos Stored");
		helper.add("maxChaos.value",            "%s mB");
		helper.add("maxInstability.name",       "Instability");
		helper.add("maxInstability.value",      "%s%%");
		helper.add(ModuleTypes.CHAOS_INJECTOR,  "Chaos Injection System");
		helper.add(DAModules.chaosInjector,     "Chaos Injector");
		helper.add("chaos_injector.name",       "Rate of Injection");
		helper.add("chaos_injector.value",      "%s Hearts");
	}

	private void gui(PrefixHelper helper) {
		helper.setPrefix("gui." + DraconicAdditions.MODID);
		helper.add("chaos_liquefier", "Chaos Liquefier");
		helper.add("chaos_liquefier.chaosSlot.hover", "Accepts Chaos Shards and Chaos Hearts.");
		helper.add("chaos_infuser", "Chaos Infuser");
		helper.add("chaos_infuser.chaosSlot.hover", "Accepts any modular item with a Chaos Stabilizer Module.");
	}

	private void info(PrefixHelper helper) {
		helper.setPrefix("info.da");
		helper.add("storedChaos", "Stored Chaos: %s / %s mB");
		helper.add("maxChaos", "Max Chaos: %s B");
		helper.add("chaos.noShield", "The Chaos Container requires shield boosting modules to operate.");
		helper.add("chaos.xfer.to", "Transferred %s mB to %s");
		helper.add("chaos.xfer.from", "Transferred %s mB from %s");
		helper.add("chaos.warning", "WARNING: Your %s is on the verge of exploding!");
		helper.add("chaos.explode", "The %s explodes in your hand!");
		helper.add("chaos.cantdrop", "Dropping this %s seems like a very bad idea right now.");
		helper.add("chaos.cantmove", "Moving this %s seems like a very bad idea right now.");
		helper.add("chaos_injector.noShield", "WARNING: No shield detected!");
		helper.add("chaos_injector.noStorage", "WARNING: You cannot inject or vent chaos without Chaos Holder Modules!");
		helper.add("chaos_injector.storageEmpty", "WARNING: Injection failed! Cause: No chaos found in Chaos Holder Modules.");
		helper.add("chaos_injector.storageFull", "WARNING: Unable to vent chaos; your Chaos Holder Modules are full!");
		helper.add("chaos_injector.shieldDisabled", "WARNING: Your shield is disabled!");
		helper.add("chaos_injector.shieldCapacityLow", "WARNING: Your shield might not be high enough to survive injection!");
		helper.add("chaos_injector.shieldLow", "WARNING: Your shielding is getting low!");
		helper.add("hermal.lore", "pls no eat");
		helper.add("hermal.craft", "Ultimate power in something so seemingly mundane.");
		helper.add("hermal.eat.attempt", "This seems like a bad idea...");
		helper.add("hermal.eat.success", "No one contests the power of hermal.");
		helper.add("instability", "Current Instability: %s%%");
		helper.add("modular_harness.cantmove", "This block doesn't seem to budge...");
		helper.add("modular_harness.storeSuccess", "You place the machine into your harness.");
		helper.add("modular_harness.placeSuccess", "You take the machine off of your harness and set it back down.");
		helper.add("modular_harness.storedBlock", "Currently Stored: ");
		helper.add("opCost", "OP Cost: %s OP/t");
	}
	
	private void deaths(PrefixHelper helper) {
		helper.setPrefix("death.attack");
		helper.add("chaosInjection", "%s exploded in a gory mess when trying to play with powers beyond their control");
	}

	private void itemProps(PrefixHelper helper) {
		helper.setPrefix("item_prop." + DraconicEvolution.MODID);
		helper.add("receive_energy_from_machine", "Receive RF From Machine");
		helper.add("tick_accel.speed", "Additional Ticks");
		helper.add("chaos_injector.rate", "Blood | Injection Rate | Chaos");
	}

	@Override
	protected void addTranslations() {
		PrefixHelper helper = new PrefixHelper(this);
		blocks(helper);
		items(helper);
		itemGroups(helper);
		modules(helper);
		gui(helper);
		info(helper);
		deaths(helper);
		itemProps(helper);
	}

	@Override
	public void add(Block key, String name) {
		if (key != null) super.add(key, name);
	}

	@Override
	public void add(Item key, String name) {
		if (key != null) super.add(key, name);
	}

	public static class PrefixHelper {
		private LangGenerator generator;
		private String prefix;

		public PrefixHelper(LangGenerator generator) {
			this.generator = generator;
		}

		public void setPrefix(@Nonnull String prefix) {
			this.prefix = prefix + (prefix == "" ? "" : ".");
		}

		public void add(String translationKey, String translation) {
			generator.add(prefix + translationKey, translation);
		}

		public void add(Block key, String name) {
			if (key != null) generator.add(key, name);
		}

		public void add(Item key, String name) {
			if (key != null) generator.add(key, name);
		}

		public void add(ModuleType<?> key, String name) {
			generator.add("module_type." + DraconicEvolution.MODID + "." + key.getName() + ".name", name);
		}

		public void add(Module<?> key, String name) {
			generator.add(key.getItem(), name);
		}
	}
}
