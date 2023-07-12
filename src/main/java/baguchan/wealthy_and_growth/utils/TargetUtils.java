package baguchan.wealthy_and_growth.utils;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class TargetUtils {
	public static boolean canTarget(Player player, RandomSource random, float lowerLimit){
		final boolean[] isTarget = new boolean[1];
		player.getCapability(WealthyAndGrowth.PLAYER_TARGET_CAPABILITY).ifPresent(cap ->{
			if (random.nextFloat() < (1.0F - cap.getEffectiveTargetScale()) + lowerLimit) {
				isTarget[0] = true;
			} else {
				isTarget[0] = false;
			}
		});
		return isTarget[0];
	}
}
