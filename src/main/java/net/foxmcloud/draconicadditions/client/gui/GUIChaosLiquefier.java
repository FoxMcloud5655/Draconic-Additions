package net.foxmcloud.draconicadditions.client.gui;

import com.brandon3055.brandonscore.client.BCSprites;
import com.brandon3055.brandonscore.client.gui.GuiToolkit;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElement;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElementManager;
import com.brandon3055.brandonscore.client.gui.modulargui.ModularGuiContainer;
import com.brandon3055.brandonscore.client.gui.modulargui.templates.TBasicMachine;
import com.brandon3055.brandonscore.inventory.ContainerBCTile;
import com.brandon3055.brandonscore.inventory.ContainerSlotLayout;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquefier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class GUIChaosLiquefier extends ModularGuiContainer<ContainerBCTile<TileChaosLiquefier>> {

	public PlayerEntity player;
	private TileChaosLiquefier tile;

	protected GuiToolkit<GUIChaosLiquefier> toolkit = new GuiToolkit<>(this, GuiToolkit.GuiLayout.DEFAULT).setTranslationPrefix("gui.draconicadditions.chaosliquefier");

	public GUIChaosLiquefier(ContainerBCTile<TileChaosLiquefier> container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		this.tile = container.tile;
		this.player = playerInventory.player;
	}

	@Override
	public void addElements(GuiElementManager manager) {
		TBasicMachine template = toolkit.loadTemplate(new TBasicMachine(this, tile));
		GuiElement chaosSlot = toolkit.createSlot(template.background, container.getSlotLayout().getSlotData(ContainerSlotLayout.SlotType.TILE_INV, 0), null, false);
		chaosSlot.zOffset += 100;
		chaosSlot.setPos(guiLeft() + 78, guiTop() + 32);
		template.addEnergyBar(tile.opStorage);
		template.addEnergyItemSlot(true, container.getSlotLayout().getSlotData(ContainerSlotLayout.SlotType.TILE_INV, 1));
	}
}