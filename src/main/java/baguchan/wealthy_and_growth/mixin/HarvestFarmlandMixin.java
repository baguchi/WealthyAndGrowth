package baguchan.wealthy_and_growth.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.HarvestFarmland;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HarvestFarmland.class)
public class HarvestFarmlandMixin extends Behavior<Villager> {
	@Shadow
	private BlockPos aboveFarmlandPos;
	@Shadow
	private long nextOkStartTime;
	@Shadow
	private int timeWorkedSoFar;
	@Shadow
	@Final
	private List<BlockPos> validFarmlandAroundVillager = Lists.newArrayList();

	public HarvestFarmlandMixin() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
	}

	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	protected void tick(ServerLevel p_23196_, Villager p_23197_, long p_23198_, CallbackInfo callbackInfo) {
		if (this.aboveFarmlandPos == null || this.aboveFarmlandPos.closerToCenterThan(p_23197_.position(), 1.0D)) {
			if (this.aboveFarmlandPos != null && p_23198_ > this.nextOkStartTime) {
				BlockState blockstate = p_23196_.getBlockState(this.aboveFarmlandPos);
				Block block = blockstate.getBlock();
				Block block1 = p_23196_.getBlockState(this.aboveFarmlandPos.below()).getBlock();
				if (block instanceof CropBlock && ((CropBlock) block).isMaxAge(blockstate)) {
					p_23196_.destroyBlock(this.aboveFarmlandPos, true, p_23197_);
				}

				if (blockstate.isAir() && block1 instanceof FarmBlock && p_23197_.hasFarmSeeds()) {
					SimpleContainer simplecontainer = p_23197_.getInventory();

					for (int i = 0; i < simplecontainer.getContainerSize(); ++i) {
						ItemStack itemstack = simplecontainer.getItem(i);
						boolean flag = false;
						if (!itemstack.isEmpty()) {
							if (itemstack.is(Items.WHEAT_SEEDS)) {
								BlockState blockstate1 = Blocks.WHEAT.defaultBlockState();
								p_23196_.setBlockAndUpdate(this.aboveFarmlandPos, blockstate1);
								flag = true;
							} else if (itemstack.is(Items.POTATO)) {
								BlockState blockstate2 = Blocks.POTATOES.defaultBlockState();
								p_23196_.setBlockAndUpdate(this.aboveFarmlandPos, blockstate2);
								flag = true;
							} else if (itemstack.is(Items.CARROT)) {
								BlockState blockstate3 = Blocks.CARROTS.defaultBlockState();
								p_23196_.setBlockAndUpdate(this.aboveFarmlandPos, blockstate3);
								flag = true;
							} else if (itemstack.is(Items.BEETROOT_SEEDS)) {
								BlockState blockstate4 = Blocks.BEETROOTS.defaultBlockState();
								p_23196_.setBlockAndUpdate(this.aboveFarmlandPos, blockstate4);
								flag = true;
							} else if (itemstack.getItem() instanceof ItemNameBlockItem) {
								BlockState blockstate5 = ((ItemNameBlockItem) itemstack.getItem()).getBlock().defaultBlockState();
								p_23196_.setBlockAndUpdate(aboveFarmlandPos, blockstate5);

								flag = true;
							}
						}

						if (flag) {
							p_23196_.playSound((Player) null, (double) this.aboveFarmlandPos.getX(), (double) this.aboveFarmlandPos.getY(), (double) this.aboveFarmlandPos.getZ(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
							itemstack.shrink(1);
							if (itemstack.isEmpty()) {
								simplecontainer.setItem(i, ItemStack.EMPTY);
							}
							break;
						}
					}
				}

				if (block instanceof CropBlock && !((CropBlock) block).isMaxAge(blockstate)) {
					this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
					this.aboveFarmlandPos = this.getValidFarmland(p_23196_);
					if (this.aboveFarmlandPos != null) {
						this.nextOkStartTime = p_23198_ + 20L;
						p_23197_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
						p_23197_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
					}
				}
			}

			++this.timeWorkedSoFar;
		}
		callbackInfo.cancel();
	}

	@Shadow
	private BlockPos getValidFarmland(ServerLevel p_23165_) {
		return null;
	}
}
