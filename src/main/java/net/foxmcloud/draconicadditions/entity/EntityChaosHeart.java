package net.foxmcloud.draconicadditions.entity;

import com.brandon3055.draconicevolution.entity.EntityDragonHeart;

import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class EntityChaosHeart extends EntityDragonHeart {	
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.<Integer>defineId(EntityDragonHeart.class, EntityDataSerializers.INT);
    
    public EntityChaosHeart(EntityType<?> entityType, Level world) {
        super(entityType, world);
        this.renderStack = new ItemStack(DAContent.chaosHeart);
    }
}
