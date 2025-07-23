package baguchan.wealthy_and_growth.entity.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.List;

public class HarvestPumpkinAndMelon extends Behavior<Villager> {
	private static final int HARVEST_DURATION = 200;
	public static final float SPEED_MODIFIER = 0.5F;
	@Nullable
	private BlockPos aboveFruitPos;
	private long nextOkStartTime;
	private int timeWorkedSoFar;
	private final List<BlockPos> validFruitAroundVillager = Lists.newArrayList();

	public HarvestPumpkinAndMelon() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
	}

	protected boolean checkExtraStartConditions(ServerLevel p_23174_, Villager p_23175_) {
		if (!EventHooks.canEntityGrief(p_23174_, p_23175_)) {
			return false;
		} else if (p_23175_.getVillagerData().profession() != VillagerProfession.FARMER) {
			return false;
		} else {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = p_23175_.blockPosition().mutable();
			this.validFruitAroundVillager.clear();

			for(int i = -1; i <= 1; ++i) {
				for(int j = -1; j <= 1; ++j) {
					for(int k = -1; k <= 1; ++k) {
						blockpos$mutableblockpos.set(p_23175_.getX() + (double)i, p_23175_.getY() + (double)j, p_23175_.getZ() + (double)k);
						if (this.validPos(blockpos$mutableblockpos, p_23174_)) {
							this.validFruitAroundVillager.add(new BlockPos(blockpos$mutableblockpos));
						}
					}
				}
			}

			this.aboveFruitPos = this.getValidFruit(p_23174_);
			return this.aboveFruitPos != null;
		}
	}

	@Nullable
	private BlockPos getValidFruit(ServerLevel p_23165_) {
		return this.validFruitAroundVillager.isEmpty() ? null : this.validFruitAroundVillager.get(p_23165_.getRandom().nextInt(this.validFruitAroundVillager.size()));
	}

	private boolean validPos(BlockPos p_23181_, ServerLevel p_23182_) {
		BlockState blockstate = p_23182_.getBlockState(p_23181_);
		BlockState blockstate2 = p_23182_.getBlockState(p_23181_.below());
		BlockState blockstate3 = p_23182_.getBlockState(p_23181_.above());
		Block block = blockstate.getBlock();
		return (block == Blocks.PUMPKIN || block == Blocks.MELON) && blockstate2.is(BlockTags.DIRT) && blockstate3.isAir();
	}

	protected void start(ServerLevel p_23177_, Villager p_23178_, long p_23179_) {
		if (p_23179_ > this.nextOkStartTime && this.aboveFruitPos != null) {
			p_23178_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFruitPos));
			p_23178_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFruitPos), 0.5F, 1));
		}

	}

	protected void stop(ServerLevel p_23188_, Villager p_23189_, long p_23190_) {
		p_23189_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
		p_23189_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		this.timeWorkedSoFar = 0;
		this.nextOkStartTime = p_23190_ + 40L;
	}

	protected void tick(ServerLevel p_23196_, Villager p_23197_, long p_23198_) {
		if (this.aboveFruitPos != null && this.aboveFruitPos.closerThan(p_23197_.blockPosition(), 2.5D)) {
			if (this.aboveFruitPos != null && p_23198_ > this.nextOkStartTime) {
				BlockState blockstate = p_23196_.getBlockState(this.aboveFruitPos);
				Block block = blockstate.getBlock();
				if (block == Blocks.PUMPKIN || block == Blocks.MELON) {
					p_23196_.destroyBlock(this.aboveFruitPos, true, p_23197_);
					this.validFruitAroundVillager.remove(this.aboveFruitPos);
					this.aboveFruitPos = this.getValidFruit(p_23196_);
					if (this.aboveFruitPos != null) {
						this.nextOkStartTime = p_23198_ + 20L;
						p_23197_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFruitPos), 0.5F, 1));
						p_23197_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFruitPos));
					}
				}
			}

			++this.timeWorkedSoFar;
		}
	}

	protected boolean canStillUse(ServerLevel p_23204_, Villager p_23205_, long p_23206_) {
		return this.timeWorkedSoFar < 200;
	}
}
