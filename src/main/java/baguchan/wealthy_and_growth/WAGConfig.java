package baguchan.wealthy_and_growth;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class WAGConfig {
	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static {
		Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class Common {

		public final ForgeConfigSpec.BooleanValue revampedPatrol;

		public Common(ForgeConfigSpec.Builder builder) {
			revampedPatrol = builder
					.comment("Enable Pillager Patrol Revamped Mechanic.")
					.define("Revamped Patrol", true);
		}
	}
}