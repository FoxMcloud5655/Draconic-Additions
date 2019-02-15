package net.foxmcloud.draconicadditions.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InfusedPotatoArmor extends ItemArmor {
    private static ArmorMaterial potatoMaterial = EnumHelper.addArmorMaterial("infusedPotatoArmor", DraconicAdditions.MODID_PREFIX + "inert_potato_armor", -1, new int[]{1, 1, 2, 1}, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

	public InfusedPotatoArmor(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(potatoMaterial, renderIndexIn, equipmentSlotIn);
		this.setMaxDamage(-1);
	}
    
	public InfusedPotatoArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		this.setMaxDamage(-1);
	}
	
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	ItemStack returnItem;
    	ItemStack itemStack = playerIn.getHeldItem(handIn);
    	if (!itemStack.hasTagCompound()) {
    		itemStack.setTagCompound(new NBTTagCompound());
    		ItemArmor item = (ItemArmor)itemStack.getItem();
    		EntityEquipmentSlot slot = item.getEquipmentSlot();
    		PotatoArmor armor;
    		if (slot.equals(EntityEquipmentSlot.HEAD)) armor = DAFeatures.potatoHelm;
    		else if (slot.equals(EntityEquipmentSlot.CHEST)) armor = DAFeatures.potatoChest;
    		else if (slot.equals(EntityEquipmentSlot.LEGS)) armor = DAFeatures.potatoLegs;
    		else if (slot.equals(EntityEquipmentSlot.FEET)) armor = DAFeatures.potatoBoots;
    		else throw new Error();
    		ItemStack armorItem = new ItemStack(armor);
    		NBTTagCompound nbt = new NBTTagCompound();
    		nbt.setFloat("Energy", ArmorStats.POTATO_BASE_CAPACITY);
    		armorItem.setTagCompound(nbt);
    		//playerIn.setHeldItem(handIn, armorItem);
    		playerIn.sendStatusMessage(new TextComponentTranslation("msg.da.infusedTransformation"), true);
    		returnItem = armorItem;
    	}
    	else returnItem = itemStack;
    	return new ActionResult<ItemStack>(EnumActionResult.FAIL, returnItem);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
    	tooltip.add(I18n.format("item.draconicadditions:infused_potato.lore"));
    	tooltip.add(I18n.format("item.draconicadditions:infused_potato.lore2"));
    }
}
