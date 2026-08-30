package net.foxmcloud.draconicadditions.blocks.reactor.tileentity;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.*;
import com.brandon3055.brandonscore.utils.MathUtils;
import com.brandon3055.brandonscore.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * Created by brandon3055 on 20/01/2017.
 */
public abstract class TileFakeReactorComponent extends TileBCore {

	private final ManagedVec3I coreOffset       = register(new ManagedVec3I("core_offset", DataFlags.SAVE_NBT_SYNC_TILE));
	public final ManagedEnum<Direction> facing  = register(new ManagedEnum<>("facing", Direction.UP, DataFlags.SAVE_NBT_SYNC_TILE));
	public final ManagedBool isBound            = register(new ManagedBool("is_bound", DataFlags.SAVE_NBT_SYNC_TILE));
	public final ManagedEnum<RSMode> rsMode     = register(new ManagedEnum<>("rs_mode", RSMode.TEMP, DataFlags.SAVE_NBT_SYNC_TILE));
	public final ManagedInt rsPower             = register(new ManagedInt("rs_power", DataFlags.SAVE_NBT_SYNC_TILE, DataFlags.TRIGGER_UPDATE));
	public float animRotation = 0;
	public float animRotationSpeed = 0;
	private TileFakeReactorCore cachedCore = null;
	public boolean coreFalureIminent = false;
	private boolean moveCheckComplete = false;

	public TileFakeReactorComponent(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
		super(tileEntityTypeIn, pos, state);
	}

	@Override
	public int getAccessDistanceSq() {
		return 256;
	}

	//region update

	@Override
	public void tick() {
		super.tick();
		moveCheckComplete = false;

		if (level.isClientSide) {
			TileFakeReactorCore core = tryGetCore();
			if (core != null) {
				animRotationSpeed = core.shieldAnimationState * 15F;
				coreFalureIminent = core.reactorState.get() == TileFakeReactorCore.ReactorState.BEYOND_HOPE;
			}
			else {
				coreFalureIminent = false;
				animRotationSpeed = 0;
			}

			animRotation += animRotationSpeed;
			if (coreFalureIminent && level.random.nextInt(10) == 0) {
				animRotation += (level.random.nextDouble() - 0.5) * 360;
				if (level.random.nextBoolean()) {
					level.addParticle(ParticleTypes.LARGE_SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble(), worldPosition.getZ() + level.random.nextDouble(), 0, 0, 0);
				}
				else {
					level.addParticle(ParticleTypes.CLOUD, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble(), worldPosition.getZ() + level.random.nextDouble(), 0, 0, 0);
				}
			}
		}
		else {
			TileFakeReactorCore core = getCachedCore();

			if (core != null) {
				int rs = rsMode.get().getRSSignal(core);
				if (rs != rsPower.get()) {
					rsPower.set(rs);
					level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
				}
			}
		}
	}

	//endregion

	//region============== Structure ==============

	/**
	 * Called by the core itself to validate this component and bind it to the core.
	 * This should only be called once the core has determined that this component is pointed at the core.
	 * This ignores this components current active isBound state because if the core is calling this method then this component can not possibly be bound to any other core.!
	 */
	public void bindToCore(TileFakeReactorCore core) {
		isBound.set(true);
		coreOffset.set(getCoreOffset(core.getBlockPos()));
	}

	/**
	 * Finds the core if it iss location is not already stored and pokes it. Core then validates or revalidates the structure.
	 */
	public void pokeCore() {
		if (isBound.get()) {
			TileFakeReactorCore core = checkAndGetCore();
			if (core != null) {
				core.pokeCore(this, facing.get().getOpposite());
				return;
			}
		}

		for (int i = 1; i < TileFakeReactorCore.COMPONENT_MAX_DISTANCE; i++) {
			BlockPos searchPos = worldPosition.relative(facing.get(), i);
			if (!level.isEmptyBlock(searchPos)) {
				BlockEntity tile = level.getBlockEntity(searchPos);
				if (tile instanceof TileFakeReactorCore && i > 1) {
					//I want this to poke the core regardless of weather or not the core structure is already valid in case this is an energy injector. The core will decide what to do.
					((TileFakeReactorCore) tile).pokeCore(this, facing.get().getOpposite());
				}
				return;
			}
		}
	}

	public void invalidateComponent() {
		isBound.set(false);
	}

	//endregion ===================================

	//region Player Interaction

	public void onPlaced() {
		if (level.isClientSide) {
			return;
		}
		pokeCore();
	}

	public void onBroken() {
		if (level.isClientSide) {
			return;
		}

		TileFakeReactorCore core = checkAndGetCore();
		if (core != null) {
			core.componentBroken(this, facing.get().getOpposite());
		}
	}

	public void onActivated(Player player) {
		if (level.isClientSide) {
			return;
		}

		pokeCore();
		TileFakeReactorCore core = checkAndGetCore();
		if (core != null) {
			core.onComponentClicked(player, this);
		}
	}

	public void setRSMode(Player player, RSMode rsMode) {
		if (level.isClientSide) {
			TileFakeReactorCore core = tryGetCore();
			if (core != null) {
				core.sendPacketToServer(output -> output.writeString(rsMode.name()).writePos(getBlockPos()), 99);
			}
		}
		else {
			this.rsMode.set(rsMode);
		}
	}

	@Override
	public void receivePacketFromClient(MCDataInput data, ServerPlayer client, int id) {
		if (id == 0) {
			setRSMode(client, RSMode.valueOf(data.readString()));
		}
	}

	//endregion

	//region Getters & Setters

	protected BlockPos getCorePos() {
		return worldPosition.subtract(coreOffset.get().getPos());
	}

	protected Vec3I getCoreOffset(BlockPos corePos) {
		return new Vec3I(worldPosition.subtract(corePos));
	}

	/**
	 * @return The core this component is bound to or null if not bound or core is nolonger at bound position. Invalidates the block if the core could not be found.
	 */
	protected TileFakeReactorCore checkAndGetCore() {
		if (!isBound.get()) {
			return null;
		}

		BlockEntity tile = level.getBlockEntity(getCorePos());
		if (tile instanceof TileFakeReactorCore) {
			return (TileFakeReactorCore) tile;
		}

		if (level.isAreaLoaded(getCorePos(), 16)) {
			invalidateComponent();
		}

		return null;
	}

	public TileFakeReactorCore tryGetCore() {
		if (!isBound.get()) {
			return null;
		}

		BlockEntity tile = level.getBlockEntity(getCorePos());
		if (tile instanceof TileFakeReactorCore) {
			return (TileFakeReactorCore) tile;
		}
		return null;
	}

	public TileFakeReactorCore getCachedCore() {
		if (isBound.get()) {
			BlockPos corePos = getCorePos();
			LevelChunk coreChunk = level.getChunkAt(corePos);

			if (!level.isLoaded(corePos)) {
				cachedCore = null;
				return null;
			}

			BlockEntity tileAtPos = coreChunk.getBlockEntity(corePos, LevelChunk.EntityCreationType.CHECK);
			if (tileAtPos == null || cachedCore == null || tileAtPos != cachedCore || tileAtPos.isRemoved()) {
				BlockEntity tile = level.getBlockEntity(corePos);

				if (tile instanceof TileFakeReactorCore) {
					cachedCore = (TileFakeReactorCore) tile;
				}
				else {
					cachedCore = null;
					isBound.set(false);
				}
			}
		}
		return cachedCore;
	}

	//endregion

	public enum RSMode {
		TEMP {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				return (int) ((tile.temperature.get() / TileFakeReactorCore.MAX_TEMPERATURE) * 15D);
			}
		},
		TEMP_INV {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				return 15 - TEMP.getRSSignal(tile);
			}
		},
		FIELD {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				double value = tile.shieldCharge.get() / tile.maxShieldCharge.get();
				value -= 0.05;
				value *= 1.2;
				return (int) (value * 15);
			}
		},
		FIELD_INV {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				return 15 - FIELD.getRSSignal(tile);
			}
		},
		SAT {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				return (int) (((double) tile.saturation.get() / (double) tile.maxSaturation.get()) * 15D);
			}
		},
		SAT_INV {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				return 15 - SAT.getRSSignal(tile);
			}
		},
		FUEL {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				double value = tile.convertedFuel.get() / (tile.convertedFuel.get() + tile.reactableFuel.get());
				value += 0.1;
				value = MathUtils.map(value, 0.1, 1, 0, 1);
				return (int) (value * 15);
			}
		},
		FUEL_INV {
			@Override
			public int getRSSignal(TileFakeReactorCore tile) {
				return 15 - FUEL.getRSSignal(tile);
			}
		};

		public abstract int getRSSignal(TileFakeReactorCore tile);
	}
}
