package net.foxmcloud.draconicadditions.utils;

import java.util.function.Predicate;

import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;

import codechicken.lib.texture.TextureUtils.IIconRegister;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;

public class DATextures implements IIconRegister, ISelectiveResourceReloadListener {

	private static TextureMap map;
	private static final String ITEMS_ = DraconicAdditions.MODID_PREFIX + "items/";
	private static final String TOOLS_ = ITEMS_ + "tools/";
	private static final String TOOLS_OBJ_ = TOOLS_ + "obj/";

	@Override
	public void registerIcons(TextureMap textureMap) {
		map = textureMap;
		CHAOTIC_STAFF_OF_POWER = register(TOOLS_ + "chaotic_staff_of_power");
		register(TOOLS_OBJ_ + "chaotic_staff_of_power");
	}

	private static TextureAtlasSprite register(String sprite) {
		return map.registerSprite(new ResourceLocation(sprite));
	}

	public static TextureAtlasSprite CHAOTIC_STAFF_OF_POWER;

	public static final ResourceLocation GUI_ARMOR_GENERATOR = ResourceHelperDE.getResourceRAW(DraconicAdditions.MODID_PREFIX + "textures/gui/armor_generator.png");
	public static final ResourceLocation GUI_ITEM_DRAINER = ResourceHelperDE.getResourceRAW(DraconicAdditions.MODID_PREFIX + "textures/gui/item_drainer.png");
	public static final ResourceLocation GUI_CHAOTIC_GENERATOR = ResourceHelperDE.getResourceRAW(DraconicAdditions.MODID_PREFIX + "textures/gui/chaotic_generator.png");
	public static final ResourceLocation ENTITY_PLUG = ResourceHelperDE.getResourceRAW(DraconicAdditions.MODID_PREFIX + "textures/entities/plug.png");

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {}
}
