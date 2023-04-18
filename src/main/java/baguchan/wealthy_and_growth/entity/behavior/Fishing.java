package baguchan.wealthy_and_growth.entity.behavior;

import baguchan.wealthy_and_growth.api.IFishing;
import baguchan.wealthy_and_growth.entity.VillagerFishingHook;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;

public class Fishing extends Behavior<Villager> {
	private static final int HARVEST_DURATION = 200;
	public static final float SPEED_MODIFIER = 0.5F;
	@Nullable
	private BlockPos aboveWaterPos;
	private long nextOkStartTime;
	private int timeWorkedSoFar;
	private final List<BlockPos> validWaterAroundVillager = Lists.newArrayList();

	public Fishing() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
	}

	protected boolean checkExtraStartConditions(ServerLevel p_23174_, Villager p_23175_) {
		if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(p_23174_, p_23175_)) {
			return false;
		} else if (p_23175_.getVillagerData().getProfession() != VillagerProfession.FISHERMAN) {
			return false;
		} else if (p_23175_.hasExcessFood()) {
			return false;
		} else {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = p_23175_.blockPosition().mutable();
			this.validWaterAroundVillager.clear();

			for (int i = -2; i <= 2; ++i) {
				for (int j = -2; j <= 2; ++j) {
					for (int k = -2; k <= 2; ++k) {
						blockpos$mutableblockpos.set(p_23175_.getX() + (double) i, p_23175_.getY() + (double) j, p_23175_.getZ() + (double) k);
						if (this.validPos(blockpos$mutableblockpos, p_23174_)) {
							this.validWaterAroundVillager.add(new BlockPos(blockpos$mutableblockpos));
						}
					}
				}
			}

			this.aboveWaterPos = this.getValidWater(p_23174_);
			return this.aboveWaterPos != null;
		}
	}

	@Nullable
	private BlockPos getValidWater(ServerLevel p_23165_) {
		return this.validWaterAroundVillager.isEmpty() ? null : this.validWaterAroundVillager.get(p_23165_.getRandom().nextInt(this.validWaterAroundVillager.size()));
	}

	private boolean validPos(BlockPos p_23181_, ServerLevel p_23182_) {
		BlockState blockstate = p_23182_.getBlockState(p_23181_);
		BlockState blockstate2 = p_23182_.getBlockState(p_23181_.below());
		return blockstate.isAir() && blockstate2.getFluidState().is(FluidTags.WATER);
	}

	protected void start(ServerLevel p_23177_, Villager p_23178_, long p_23179_) {
		if (this.aboveWaterPos != null) {
			p_23178_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveWaterPos));
			p_23178_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveWaterPos), 0.5F, 3));
		}
		if (p_23178_ instanceof IFishing iFishing) {
			if (iFishing.getFishingHook() == null) {

				p_23177_.playSound((Player) null, p_23178_.getX(), p_23178_.getY(), p_23178_.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (p_23177_.getRandom().nextFloat() * 0.4F + 0.8F));
				VillagerFishingHook villagerFishingHook = new VillagerFishingHook(p_23178_, p_23177_, 0, 1);

				double d0 = (double) this.aboveWaterPos.getX() - p_23178_.getX();
				double d1 = (double) this.aboveWaterPos.getY() - p_23178_.getEyeY();
				double d2 = (double) this.aboveWaterPos.getZ() - p_23178_.getZ();

				villagerFishingHook.setDeltaMovement(d0 * 0.3D, d1 * 0.3D, d2 * 0.3D);
				iFishing.setFishingHook(villagerFishingHook);
				p_23177_.addFreshEntity(villagerFishingHook);

				p_23178_.gameEvent(GameEvent.ITEM_INTERACT_START);
			}
		}
	}

	protected void stop(ServerLevel p_23188_, Villager p_23189_, long p_23190_) {
		if (p_23189_ instanceof IFishing iFishing) {
			if (iFishing.getFishingHook() != null) {
				iFishing.getFishingHook().retrieve(new ItemStack(Items.FISHING_ROD));
				p_23188_.playSound((Player) null, p_23189_.getX(), p_23189_.getY(), p_23189_.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (p_23189_.getRandom().nextFloat() * 0.4F + 0.8F));
				p_23189_.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
			}
			iFishing.setFishingHook(null);
		}

		p_23189_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
		p_23189_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		this.timeWorkedSoFar = 0;
		this.nextOkStartTime = p_23190_ + 40L;
	}

	protected void tick(ServerLevel p_23188_, Villager p_23189_, long p_23190_) {
		++this.timeWorkedSoFar;
		if (p_23189_ instanceof IFishing iFishing) {
			if (iFishing.getFishingHook() != null && (iFishing.getFishingHook().nibble > 0)) {
				iFishing.getFishingHook().retrieve(new ItemStack(Items.FISHING_ROD));
				p_23188_.playSound((Player) null, p_23189_.getX(), p_23189_.getY(), p_23189_.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (p_23189_.getRandom().nextFloat() * 0.4F + 0.8F));
				p_23189_.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
				iFishing.setFishingHook(null);
			}
		}
	}

	protected boolean canStillUse(ServerLevel p_23204_, Villager p_23205_, long p_23206_) {
		if (p_23205_ instanceof IFishing iFishing) {
			if (iFishing.getFishingHook() != null) {
				if (iFishing.getFishingHook().currentState == VillagerFishingHook.FishHookState.BOBBING || iFishing.getFishingHook().currentState == VillagerFishingHook.FishHookState.FLYING) {
					return true;
				}
			}
		}
		return this.timeWorkedSoFar < 100;
	}

	@Override
	protected boolean timedOut(long p_22537_) {
		return false;
	}
}
