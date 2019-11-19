package net.foxmcloud.draconicadditions.client.model;

import com.brandon3055.draconicevolution.client.model.ArmorModelHelper;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.client.renderer.texture.TextureMap;

public class DAArmorModelHelper extends ArmorModelHelper {

	private final String[] resources = {
		"models/armor/potato_helmet",
		"models/armor/potato_body",
		"models/armor/potato_right_arm",
		"models/armor/potato_left_arm",
		"models/armor/potato_belt",
		"models/armor/potato_right_leg",
		"models/armor/potato_left_leg",
		"models/armor/potato_right_boot",
		"models/armor/potato_left_boot",
		"models/armor/chaotic_helmet",
		"models/armor/chaotic_body",
		"models/armor/chaotic_right_arm",
		"models/armor/chaotic_left_arm",
		"models/armor/chaotic_belt",
		"models/armor/chaotic_right_leg",
		"models/armor/chaotic_left_leg",
		"models/armor/chaotic_right_boot",
		"models/armor/chaotic_left_boot"
	};

	public void registerIcons(TextureMap textureMap) {
		for (String resource : resources) {
			textureMap.registerSprite(ResourceHelperDE.getResourceRAW(DraconicAdditions.MODID_PREFIX + resource));
		}
		DAFeatures.potatoHelm.model = null;
		DAFeatures.potatoChest.model = null;
		DAFeatures.potatoLegs.model = null;
		DAFeatures.potatoBoots.model = null;
		DAFeatures.chaoticHelm.model = null;
		DAFeatures.chaoticChest.model = null;
		DAFeatures.chaoticLegs.model = null;
		DAFeatures.chaoticBoots.model = null;
	}
}
