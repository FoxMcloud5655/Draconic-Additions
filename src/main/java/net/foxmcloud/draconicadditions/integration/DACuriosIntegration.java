package net.foxmcloud.draconicadditions.integration;

import java.util.function.Function;

import com.brandon3055.draconicevolution.integration.equipment.CuriosIntegration;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class DACuriosIntegration extends CuriosIntegration {
	public static final TagKey<Item> CHARM_TAG = ItemTags.create(new ResourceLocation("curios", "charm"));
	public static final TagKey<Item> BACK_TAG = ItemTags.create(new ResourceLocation("curios", "back"));
	
	public static void generateTags(Function<TagKey<Item>, TagsProvider.TagAppender<Item>> builder) {
		builder.apply(CHARM_TAG).add(
				DAContent.necklaceWyvern,
				DAContent.necklaceDraconic,
				DAContent.necklaceChaotic
		);
		builder.apply(BACK_TAG).add(
				DAContent.harnessWyvern,
				DAContent.harnessDraconic,
				DAContent.harnessChaotic
		);
	}
}
