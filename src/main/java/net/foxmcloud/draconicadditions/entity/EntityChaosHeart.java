package net.foxmcloud.draconicadditions.entity;

import java.util.List;

import com.brandon3055.brandonscore.utils.FeatureUtils;
import com.brandon3055.draconicevolution.client.DEParticles;
import com.brandon3055.draconicevolution.entity.EntityDragonHeart;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityChaosHeart extends EntityDragonHeart {
	
    private static final DataParameter<Integer> AGE = EntityDataManager.<Integer>defineId(EntityChaosHeart.class, DataSerializers.INT);

    public EntityChaosHeart(EntityType<?> entityType, World world) {
        super(entityType, world);
        this.renderStack = new ItemStack(DAContent.chaosHeart);
    }
}
