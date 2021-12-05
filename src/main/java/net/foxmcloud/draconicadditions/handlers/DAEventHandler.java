package net.foxmcloud.draconicadditions.handlers;

import com.brandon3055.brandonscore.registry.ModFeatureParser;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.entity.EntityChaosGuardian;
import com.brandon3055.draconicevolution.handlers.CustomArmorHandler;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;

import net.foxmcloud.draconicadditions.CommonMethods;
import net.foxmcloud.draconicadditions.DAFeatures;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.capabilities.ChaosInBloodProvider;
import net.foxmcloud.draconicadditions.capabilities.IChaosInBlood;
import net.foxmcloud.draconicadditions.entity.EntityChaosHeart;
import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.PlayerEntitySP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DAEventHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
		if (event.getPlayerEntity() != null) {
			float newDigSpeed = event.getOriginalSpeed();
			CustomArmorHandler.ArmorSummery summery = new CustomArmorHandler.ArmorSummery().getSummery(event.getPlayerEntity());
			if (summery == null) {
				return;
			}

			if (event.getPlayerEntity().isInsideOfMaterial(Material.WATER)) {
				if (summery.armorStacks.get(3).getItem() == DAFeatures.chaoticHelm) {
					newDigSpeed *= 5f;
				}
			}

			if (!event.getPlayerEntity().onGround) {
				if (summery.armorStacks.get(2).getItem() == DAFeatures.chaoticChest) {
					newDigSpeed *= 5f;
				}
			}

			if (newDigSpeed != event.getOriginalSpeed()) {
				event.setNewSpeed(newDigSpeed);
			}
		}
	}

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void guiMouse(MouseInputEvent.Pre event) {
		if (event.getGui() instanceof GuiContainer) {
			GuiContainer inventory = (GuiContainer) event.getGui();
			Slot slot = inventory.getSlotUnderMouse();
			if (slot != null) {
				ItemStack stack = slot.getStack();
				if (stack != null) {
					PlayerEntitySP player = event.getGui().mc.player;
					if (stack.getItem() instanceof ChaosContainer && !player.isCreative()) {
						event.setCanceled(((ChaosContainer) stack.getItem()).getChaos(stack) > 0);
					}
					else if (stack.getItem() == DAFeatures.chaoticChest) {
						event.setCanceled(ItemNBTHelper.getBoolean(stack, "injecting", false) || ItemNBTHelper.getBoolean(stack, "bloodIsChaos", false));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDropEvent(LivingDropsEvent event) {
        if (!event.getEntity().world.isClientSide && event.getEntity() instanceof EntityChaosGuardian) {
            if (ModFeatureParser.isEnabled(DAFeatures.chaosHeart)) {
                EntityChaosHeart heart = new EntityChaosHeart(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ);
                event.getEntity().world.spawnEntity(heart);
            }
        }
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;
    	IChaosInBlood pCap = player.getCapability(ChaosInBloodProvider.PLAYER_CAP, null);
		if (pCap != null && player.isEntityAlive()) {
			Potion wither = Potion.getPotionFromResourceLocation("wither");
			Potion regen = Potion.getPotionFromResourceLocation("regeneration");
			Potion resistance = Potion.getPotionFromResourceLocation("resistance");
			Potion strength = Potion.getPotionFromResourceLocation("strength");
			if (pCap.getChaos() > 0) {
				float chaos = pCap.getChaos();
				float damageAbsorbed = CommonMethods.subtractShielding(player, chaos, 0.075F, 0.2F);
				if (event.side == Side.CLIENT) {
					player.addPotionEffect(new PotionEffect(wither, 2, 0, false, false));
					player.addPotionEffect(new PotionEffect(regen, 2, 0, false, false));
				}
				player.addPotionEffect(new PotionEffect(resistance, 2, 3, false, false));
				player.addPotionEffect(new PotionEffect(strength, 2, (int)chaos, false, false));
				if (damageAbsorbed < chaos - 0.1 && !player.isCreative()) {
					player.attackEntityFrom(CommonMethods.chaosBurst, 1000);
				}
				if (player.getHealth() > 0) {
					player.setHealth(chaos);
				}
			}
			else if (!pCap.hasChaos() && pCap.hadChaosLastUpdate()) {
				if (event.side == Side.CLIENT) {
					player.removePotionEffect(regen);
					player.removePotionEffect(wither);
					player.removePotionEffect(resistance);
					player.removePotionEffect(strength);
				}
				else {
					player.clearActivePotions();
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getItemStack().getItem() instanceof ChaosContainer) {
			if (((ChaosContainer)event.getItemStack().getItem()).onLeftClickEntity(event.getItemStack(), event.getPlayerEntity(), event.getTarget())) {
				return;
			}
		}
		float chaosDamageRatio = 10.0F;
		PlayerEntity player = event.getPlayerEntity();
		IChaosInBlood pCap = player.getCapability(ChaosInBloodProvider.PLAYER_CAP, null);
		if (pCap != null && player.isEntityAlive() && pCap.getChaos() > 0 && event.getTarget() instanceof EntityLiving) {
			EntityLiving target = (EntityLiving)event.getTarget();
			float damageToDeal = Math.min(target.getHealth(), pCap.getChaos() * chaosDamageRatio);
			boolean wasDamaged = target.attackEntityFrom(CommonMethods.chaosBurst, damageToDeal);
			if (wasDamaged) {
				pCap.removeChaos(damageToDeal / chaosDamageRatio);
				CommonMethods.explodeEntity(target.getPosition(), target.world);
			}
		}
	}
	
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
    	if (!event.isWasDeath()) {
	    	PlayerEntity player = event.getPlayerEntity();
	    	IChaosInBlood newCap = player.getCapability(ChaosInBloodProvider.PLAYER_CAP, null);
	    	IChaosInBlood oldCap = event.getOriginal().getCapability(ChaosInBloodProvider.PLAYER_CAP, null);
	    	newCap.setChaos(oldCap.getChaos());
    	}
    }
    
    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent<Entity> e) {
    	Entity obj = e.getObject();
    	if (obj instanceof PlayerEntity && !(obj instanceof FakePlayer)) {
    		PlayerEntity player = (PlayerEntity) obj;
    		e.addCapability(ResourceHelperDE.getResourceRAW(DraconicAdditions.MODID_PREFIX + "chaos_in_blood"), new ChaosInBloodProvider());
    	}
    }
}
