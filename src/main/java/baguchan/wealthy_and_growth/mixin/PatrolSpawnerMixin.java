package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.WAGConfig;
import baguchan.wealthy_and_growth.utils.TargetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {
	@Shadow
	private int nextTick;

	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	public void tick(ServerLevel level, boolean p_64571_, boolean p_64572_, CallbackInfoReturnable<Integer> callbackinfo) {
		if (WAGConfig.COMMON.revampedPatrol.get()) {
			if (!p_64571_) {
				callbackinfo.setReturnValue(0);
			} else if (!level.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)) {
				callbackinfo.setReturnValue(0);
			} else {
				RandomSource random = level.random;
				--this.nextTick;
				if (this.nextTick > 0) {
					callbackinfo.setReturnValue(0);
				} else {
					this.nextTick += 12000 + random.nextInt(1200);
					long i = level.getDayTime() / 24000L;
					if (i >= 5L && level.isDay()) {
						int j = level.players().size();
						if (j < 1) {
							callbackinfo.setReturnValue(0);
						} else {
							Player player = level.players().get(random.nextInt(j));

							if (!TargetUtils.canTarget(player, random, 0.2F)) {
								callbackinfo.setReturnValue(0);
							}
							if (player.isSpectator()) {
								callbackinfo.setReturnValue(0);
							} else if (level.isCloseToVillage(player.blockPosition(), 2)) {
								callbackinfo.setReturnValue(0);
							} else {
								int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
								int l = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
								BlockPos.MutableBlockPos blockpos$mutableblockpos = player.blockPosition().mutable().move(k, 0, l);
								int i1 = 10;
								if (!level.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10)) {
									callbackinfo.setReturnValue(0);
								} else {
									Holder<Biome> biome = level.getBiome(blockpos$mutableblockpos);
									if (biome.is(Tags.Biomes.IS_MUSHROOM)) {
										callbackinfo.setReturnValue(0);
									} else {
										int j1 = 0;
										int k1 = (int) Math.ceil((double) level.getCurrentDifficultyAt(blockpos$mutableblockpos).getEffectiveDifficulty()) + 1;

										for (int l1 = 0; l1 < k1; ++l1) {
											++j1;
											blockpos$mutableblockpos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockpos$mutableblockpos).getY());
											if (l1 == 0) {
												if (!this.spawnPatrolMember(level, blockpos$mutableblockpos, random, true)) {
													break;
												}
											} else {
												this.spawnPatrolMember(level, blockpos$mutableblockpos, random, false);
											}

											blockpos$mutableblockpos.setX(blockpos$mutableblockpos.getX() + random.nextInt(5) - random.nextInt(5));
											blockpos$mutableblockpos.setZ(blockpos$mutableblockpos.getZ() + random.nextInt(5) - random.nextInt(5));
										}

										callbackinfo.setReturnValue(j1);
									}
								}
							}
						}

					} else {
						callbackinfo.setReturnValue(0);
					}
				}
			}
		}
	}

	@Shadow
	private boolean spawnPatrolMember(ServerLevel p_64565_, BlockPos p_64566_, RandomSource p_64567_, boolean p_64568_) {
		return false;
	}
}
