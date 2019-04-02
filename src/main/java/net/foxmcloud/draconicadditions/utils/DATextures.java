package net.foxmcloud.draconicadditions.utils;

import codechicken.lib.texture.TextureUtils.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class DATextures implements IIconRegister, IResourceManagerReloadListener {
	
    private static TextureMap map;
    private static final String ITEMS_ = "draconicadditions:items/";
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
    
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}
}
