package net.foxmcloud.draconicadditions.client.gui;

import com.brandon3055.brandonscore.client.BCGuiSprites;
import com.brandon3055.brandonscore.client.gui.GuiToolkit;
import com.brandon3055.brandonscore.client.gui.GuiToolkit.LayoutPos;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElement;
import com.brandon3055.brandonscore.client.gui.modulargui.GuiElementManager;
import com.brandon3055.brandonscore.client.gui.modulargui.ModularGuiContainer;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiLabel;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiTexture;
import com.brandon3055.brandonscore.client.gui.modulargui.templates.TBasicMachine;
import com.brandon3055.brandonscore.client.gui.modulargui.templates.TGuiBase;
import com.brandon3055.brandonscore.inventory.ContainerBCTile;
import com.brandon3055.brandonscore.inventory.ContainerSlotLayout;
import com.brandon3055.draconicevolution.client.gui.modular.TModularMachine;
import com.brandon3055.draconicevolution.inventory.ContainerDETile;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosInfuser;
import net.foxmcloud.draconicadditions.inventory.ContainerDATile;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GUIChaosInfuser extends ModularGuiContainer<ContainerDATile<TileChaosInfuser>> {
	private TileChaosInfuser tile;
	private final String GUITranslation;
	protected GuiToolkit<GUIChaosInfuser> toolkit;

	public GUIChaosInfuser(ContainerDATile<TileChaosInfuser> container, Inventory inv, Component title) {
		super(container, inv, title);
		this.tile = container.tile;
		GUITranslation = "gui." + DraconicAdditions.MODID + "." + tile.getBlockState().getBlock().getRegistryName().getPath();
		toolkit = new GuiToolkit<>(this, 200, 170).setTranslationPrefix(GUITranslation);
	}

	@Override
	public void addElements(GuiElementManager manager) {
		TBasicMachine template = new TModularMachine(this, tile, false);
		template.background = GuiTexture.newDynamicTexture(xSize(), ySize(), () -> BCGuiSprites.getThemed("background_dynamic"));
		template.background.onReload(guiTex -> guiTex.setPos(guiLeft(), guiTop()));
		toolkit.loadTemplate(template);
		template.playerSlots = toolkit.createPlayerSlots(template.background, false);
		toolkit.placeInside(template.playerSlots, template.background, LayoutPos.BOTTOM_CENTER, 0, -7);
		GuiElement chaosSlot = toolkit.createSlot(template.background, container.getSlotLayout().getSlotData(ContainerSlotLayout.SlotType.TILE_INV, 0), null, false);
		toolkit.placeOutside(chaosSlot, template.playerSlots, LayoutPos.TOP_CENTER, 0, -template.background.ySize() / 6);
		chaosSlot.setHoverText(I18n.get(GUITranslation + ".chaosSlot.hover"));
		chaosSlot.setHoverTextEnabled(tile.itemHandler.getStackInSlot(0).isEmpty());
		GuiLabel chaosText = toolkit.createHeading("", template.background, true);
		chaosText.setYPos(guiTop() + 14);
		chaosText.setDisplaySupplier(() -> I18n.get("info.da.storedChaos", tile.chaos.get(), tile.getMaxChaos()));
		template.addEnergyBar(tile.opStorage, false);
		template.addEnergyItemSlot(true, container.getSlotLayout().getSlotData(ContainerSlotLayout.SlotType.TILE_INV, 1));
	}
}