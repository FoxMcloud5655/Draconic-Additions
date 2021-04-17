package net.foxmcloud.draconicadditions.entity;

import java.util.List;

import com.brandon3055.brandonscore.client.particle.BCEffectHandler;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.brandonscore.utils.FeatureUtils;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.client.DEParticles;
import com.brandon3055.draconicevolution.entity.EntityDragonHeart;

import net.foxmcloud.draconicadditions.DAFeatures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityChaosHeart extends EntityDragonHeart {

    public EntityChaosHeart(World world) {
        super(world);
        this.renderStack = new ItemStack(DAFeatures.chaosHeart);
    }

    public EntityChaosHeart(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.renderStack = new ItemStack(DAFeatures.chaosHeart);
    }
    
    @Override
    public void onUpdate() {
    	if (!world.isRemote) {
    		int age = getAge();
    		if (age == 801) {
        		List<EntityDragonHeart> dragonHearts = world.getEntitiesWithinAABB(EntityDragonHeart.class, new AxisAlignedBB(this.posX - 1, this.posY - 1, this.posZ - 1, this.posX + 1, this.posY + 1, this.posZ + 1));
        		if (!dragonHearts.isEmpty()) {
        			for(int i = 0; i < dragonHearts.size(); i++) {
        				if (!(dragonHearts.get(i) instanceof EntityChaosHeart))
        					dragonHearts.get(i).setDead();
        			}
        		}
        	}
            if (age == 1279) {
                drop();
                setAge(1281);
            }
    	}
        super.onUpdate();
    }
    
    private void drop() {
        EntityPlayer player = world.getClosestPlayerToEntity(this, 512);

        if (player != null) {
            BCEffectHandler.spawnFX(DEParticles.DRAGON_HEART, world, new Vec3D(this), new Vec3D(player), 128D, 0, 0, 0, 1);
            FeatureUtils.dropItemNoDellay(new ItemStack(DAFeatures.chaosHeart), world, new Vec3D(player).toVector3());
        }
        else {
            FeatureUtils.dropItemNoDellay(new ItemStack(DAFeatures.chaosHeart), world, new Vec3D(this).toVector3());
        }

        setDead();
    }
}
