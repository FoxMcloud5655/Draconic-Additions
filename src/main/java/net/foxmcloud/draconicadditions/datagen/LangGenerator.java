package net.foxmcloud.draconicadditions.datagen;

import net.foxmcloud.draconicadditions.DAContent;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGenerator extends LanguageProvider {
	public LangGenerator(DataGenerator gen) {
		super(gen, DraconicAdditions.MODID, "en_us");
	}

	private void items(PrefixHelper helper) {
		helper.add(DAContent.chaosHeart,       "Chaos Heart");
		helper.add(DAContent.inertPotatoHelm,  "Inert Potato Helmet");
		helper.add(DAContent.inertPotatoChest, "Inert Potato Chestplate");
		helper.add(DAContent.inertPotatoLegs,  "Inert Potato Leggings");
		helper.add(DAContent.inertPotatoBoots, "Inert Potato Boots");
		helper.add(DAContent.necklaceWyvern,   "Wyvern Necklace");
		helper.add(DAContent.necklaceDraconic, "Draconic Necklace");
		helper.add(DAContent.necklaceChaotic,  "Chaotic Necklace");
	}

	private void itemGroups(PrefixHelper helper) {
		add("itemGroup." + DraconicAdditions.MODID + ".items",   "Draconic Additions");
		add("itemGroup." + DraconicAdditions.MODID + ".modules", "Draconic Additions Modules");
	}

	private void modules(PrefixHelper helper) {
		helper.setPrefix("item.draconicadditions");
		helper.setSuffix("module");
		;    	helper.add("chaotic_auto_feed", "Chaotic Auto Feed Module");
	}

	@Override
	protected void addTranslations() {
		PrefixHelper helper = new PrefixHelper(this);
		items(helper);
		itemGroups(helper);
		modules(helper);
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
		private String suffix;

		public PrefixHelper(LangGenerator generator) {
			this.generator = generator;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix + ".";
		}

		public void setSuffix(String suffix) {
			this.suffix = "_" + suffix;
		}

		public void add(String translationKey, String translation) {
			generator.add(prefix + translationKey + suffix, translation);
		}

		public void add(Block key, String name) {
			if (key != null) generator.add(key, name);
		}

		public void add(Item key, String name) {
			if (key != null) generator.add(key, name);
		}
	}
}
