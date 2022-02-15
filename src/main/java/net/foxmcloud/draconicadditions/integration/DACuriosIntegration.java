package net.foxmcloud.draconicadditions.integration;

import java.util.function.Function;

import com.brandon3055.draconicevolution.integration.equipment.CuriosIntegration;

import net.foxmcloud.draconicadditions.DAContent;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

/**
 * Created by brandon3055 on 6/1/21
 */
public class DACuriosIntegration extends CuriosIntegration {

	public static final Tags.IOptionalNamedTag<Item> CHARM_TAG = ItemTags.createOptional(new ResourceLocation("curios", "charm"));

	public static void generateTags(Function<Tags.IOptionalNamedTag<Item>, TagsProvider.Builder<Item>> builder) {
		builder.apply(CHARM_TAG).add(
				DAContent.necklaceWyvern,
				DAContent.necklaceDraconic,
				DAContent.necklaceChaotic
				);
	}
}
