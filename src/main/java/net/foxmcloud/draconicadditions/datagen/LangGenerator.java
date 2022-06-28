package net.foxmcloud.draconicadditions.datagen;

import javax.annotation.Nonnull;

import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleType;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.items.DAContent;
import net.foxmcloud.draconicadditions.items.DAModules;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGenerator extends LanguageProvider {
	public LangGenerator(DataGenerator gen) {
		super(gen, DraconicAdditions.MODID, "en_us");
	}

	private void items(PrefixHelper helper) {
		helper.add(DAContent.chaosHeart,         "Chaos Heart");
		helper.add(DAContent.inertPotatoHelm,    "Inert Potato Helmet");
		helper.add(DAContent.inertPotatoChest,   "Inert Potato Chestplate");
		helper.add(DAContent.inertPotatoLegs,    "Inert Potato Leggings");
		helper.add(DAContent.inertPotatoBoots,   "Inert Potato Boots");
		helper.add(DAContent.infusedPotatoHelm,  "Infused Potato Helmet");
		helper.add(DAContent.infusedPotatoChest, "Infused Potato Chestplate");
		helper.add(DAContent.infusedPotatoLegs,  "Infused Potato Leggings");
		helper.add(DAContent.infusedPotatoBoots, "Infused Potato Boots");
		helper.add(DAContent.necklaceWyvern,     "Wyvern Necklace");
		helper.add(DAContent.necklaceDraconic,   "Draconic Necklace");
		helper.add(DAContent.necklaceChaotic,    "Chaotic Necklace");
		helper.add(DAContent.harnessWyvern,      "Wyvern Harness");
		helper.add(DAContent.harnessDraconic,    "Draconic Harness");
		helper.add(DAContent.harnessChaotic,     "Chaotic Harness");
	}

	private void itemGroups(PrefixHelper helper) {
		helper.setPrefix("itemGroup." + DraconicAdditions.MODID);
		helper.add("items",   "Draconic Additions");
		helper.add("modules", "Draconic Additions Modules");
	}

	private void modules(PrefixHelper helper) {
		helper.setPrefix("module." + DraconicAdditions.MODID);
		helper.add(DAModules.chaoticAutoFeed,   "Chaotic Auto Feed Module");
		helper.add(DAModules.draconicTickAccel, "Draconic Tick Accelerator");
		helper.add(DAModules.chaoticTickAccel,  "Chaotic Tick Accelerator");
        helper.add(ModuleTypes.TICK_ACCEL,      "Tick Accelerator");
        helper.add("tick_accel.name",           "Additional Ticks");
        helper.add("tick_accel.value",          "%s Ticks");
	}

	private void info(PrefixHelper helper) {
		helper.setPrefix("info.da");
		helper.add("harnessdim.stopTravel", "Something prevents you from teleporting into this dimension...");
		helper.add("modular_harness.cantmove", "This block doesn't seem to budge...");
		helper.add("modular_harness.storeSuccess", "You place the machine into your harness.");
		helper.add("modular_harness.placeSuccess", "You take the machine off of your harness and set it back down.");
		helper.add("modular_harness.stored_block", "Currently Stored: ");
		helper.add("modular_harness.op_cost", "OP Cost: ");
		helper.add("modular_harness.op_cost.value", "%s OP/t");
	}
	
	private void itemProps(PrefixHelper helper) {
		helper.setPrefix("item_prop." + DraconicEvolution.MODID);
		helper.add("receive_energy_from_machine", "Receive RF From Machine");
		helper.add("tick_accel_speed", "Additional Ticks");
	}

	@Override
	protected void addTranslations() {
		PrefixHelper helper = new PrefixHelper(this);
		items(helper);
		itemGroups(helper);
		modules(helper);
		info(helper);
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