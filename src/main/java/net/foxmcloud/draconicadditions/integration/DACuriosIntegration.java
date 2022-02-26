package net.foxmcloud.draconicadditions.integration;

import java.util.function.Function;

import com.brandon3055.draconicevolution.integration.equipment.CuriosIntegration;

import net.foxmcloud.draconicadditions.items.DAContent;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class DACuriosIntegration extends CuriosIntegration {
	public static final Tags.IOptionalNamedTag<Item> CHARM_TAG = ItemTags.createOptional(new ResourceLocation("curios", "charm"));
	public static final Tags.IOptionalNamedTag<Item> BACK_TAG = ItemTags.createOptional(new ResourceLocation("curios", "back"));

	public static void generateTags(Function<Tags.IOptionalNamedTag<Item>, TagsProvider.Builder<Item>> builder) {
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
