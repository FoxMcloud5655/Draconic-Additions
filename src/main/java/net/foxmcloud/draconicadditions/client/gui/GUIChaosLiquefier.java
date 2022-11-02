package net.foxmcloud.draconicadditions.client.gui;

import com.brandon3055.brandonscore.client.gui.GuiToolkit;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElement;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElementManager;
import com.brandon3055.brandonscore.client.gui.modulargui.ModularGuiContainer;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiLabel;
import com.brandon3055.brandonscore.client.gui.modulargui.templates.TBasicMachine;
import com.brandon3055.brandonscore.inventory.ContainerBCTile;
import com.brandon3055.brandonscore.inventory.ContainerSlotLayout;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquefier;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class GUIChaosLiquefier extends ModularGuiContainer<ContainerBCTile<TileChaosLiquefier>> {
	public Player player;
	private TileChaosLiquefier tile;
	private final String GUITranslation;
	protected GuiToolkit<GUIChaosLiquefier> toolkit;

	public GUIChaosLiquefier(ContainerBCTile<TileChaosLiquefier> container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.tile = container.tile;
		this.player = playerInventory.player;
		GUITranslation = "gui." + DraconicAdditions.MODID + "." + tile.getBlockState().getBlock().getRegistryName().getPath();
		toolkit = new GuiToolkit<>(this, GuiToolkit.GuiLayout.DEFAULT).setTranslationPrefix(GUITranslation);
	}

	@Override
	public void addElements(GuiElementManager manager) {
		TBasicMachine template = toolkit.loadTemplate(new TBasicMachine(this, tile));
		GuiElement chaosSlot = toolkit.createSlot(template.background, container.getSlotLayout().getSlotData(ContainerSlotLayout.SlotType.TILE_INV, 0), null, false);
		chaosSlot.setPos(guiLeft() + 78, guiTop() + 32);
		chaosSlot.setHoverText(I18n.get(GUITranslation + ".chaosSlot.hover"));
		chaosSlot.setHoverTextEnabled(tile.itemHandler.getStackInSlot(0).isEmpty());
		GuiLabel chaosText = toolkit.createHeading("", template.background, true);
		chaosText.setYPos(guiTop() + 14);
		chaosText.setDisplaySupplier(() -> I18n.get(GUITranslation + ".chaosText", tile.chaos.get(), tile.getMaxChaos()));
		template.addEnergyBar(tile.opStorage);
		template.addEnergyItemSlot(true, container.getSlotLayout().getSlotData(ContainerSlotLayout.SlotType.TILE_INV, 1));
	}
}