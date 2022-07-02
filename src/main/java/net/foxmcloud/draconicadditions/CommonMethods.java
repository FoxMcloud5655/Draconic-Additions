package net.foxmcloud.draconicadditions;

import com.brandon3055.brandonscore.client.particle.BCParticle;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.ItemNBTHelper;
import com.brandon3055.draconicevolution.client.DEParticles;
import com.brandon3055.draconicevolution.handlers.DESounds;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;

import codechicken.lib.vec.Vector3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonMethods {

	public static final DamageSource chaosBurst = new DamageSource("chaosBurst").bypassArmor();
	private static final short gracePeriod = 100;

	//Must be called every tick, else check will fail.
	public static boolean cheatCheck(ItemStack stack, World world) {
		long containerTime = ItemNBTHelper.getLong(stack, "cheatCheck", 0);
		long serverTime = world.getGameTime();
		boolean isCheating = false;
		if (containerTime < serverTime - gracePeriod && containerTime > gracePeriod)  {
			isCheating = true;
		}
		ItemNBTHelper.setLong(stack, "cheatCheck", serverTime);
		return isCheating;
	}

	public static void explodeEntity(Vector3 pos, World world) {
		world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), DESounds.beam, SoundCategory.MASTER, 0.25F, 0.5F);
		world.playSound(null, new BlockPos(pos.x, pos.y, pos.z), DESounds.fusionComplete, SoundCategory.MASTER, 1.0F, 2.0F);
		if (world.isClientSide) {
			for (int i = 0; i < 5; i++) {
				//BCEffectHandler.spawnFX(DEParticles.ARROW_SHOCKWAVE, world, pos, pos, 128D, 2);
			}
		}
	}
}