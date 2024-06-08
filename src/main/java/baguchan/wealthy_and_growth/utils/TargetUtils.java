package baguchan.wealthy_and_growth.utils;

import baguchan.wealthy_and_growth.capability.PlayerTargetCapability;
import baguchan.wealthy_and_growth.register.ModAttachs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class TargetUtils {
	public static boolean canTarget(Player player, RandomSource random, float lowerLimit){
		boolean isTarget = false;
		PlayerTargetCapability cap = player.getData(ModAttachs.PLAYER_TARGET.get());
			if (random.nextFloat() > (1.0F - cap.getEffectiveTargetScale()) + lowerLimit) {
				isTarget = true;
			} else {
				isTarget = false;
			}

		return isTarget;
	}
}
