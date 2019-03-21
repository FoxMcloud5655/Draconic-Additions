package net.foxmcloud.draconicadditions.client.creativetab;

import com.brandon3055.brandonscore.registry.ModFeatureParser;
import com.brandon3055.draconicevolution.client.creativetab.DETab;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DATab extends DETab {
	private String label;
	private int tab;

	static ItemStack itemStackChaotic = ItemStack.EMPTY;

	public DATab(int id, String modid, String label, int tab) {
		super(id, modid, label, tab);
		this.label = label;
		this.tab = tab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		if (itemStackChaotic.isEmpty()) {
			if (ModFeatureParser.isEnabled(DAFeatures.chaoticHelm)) {
				itemStackChaotic = new ItemStack(DAFeatures.chaoticHelm);
				DAFeatures.chaoticHelm.modifyEnergy(itemStackChaotic, DAFeatures.chaoticHelm.getMaxEnergyStored(itemStackChaotic));
			}
			else if (ModFeatureParser.isEnabled(DAFeatures.chaoticEnergyCore)) {
				itemStackChaotic = new ItemStack(DAFeatures.chaoticEnergyCore);
			}
			else itemStackChaotic = new ItemStack(Items.ENDER_EYE);
		}
		return itemStackChaotic;
	}
}
