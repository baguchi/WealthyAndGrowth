package baguchan.wealthy_and_growth;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

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
		public final ForgeConfigSpec.IntValue patrolNextTick;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> seedWhitelist;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> plantableCropWhitelist;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> cropWhitelist;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> foodWhitelist;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> huntableWhitelist;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> feedWhitelist;

		public Common(ForgeConfigSpec.Builder builder) {
			Predicate<Object> validator = o -> o instanceof String;
			revampedPatrol = builder
					.comment("Enable Pillager Patrol Revamped Mechanic.")
					.define("Revamped Patrol", true);
			patrolNextTick = builder
					.comment("Set the Pillager Patrol Next Tick.")
					.defineInRange("Pillager Patrol Next Tick", 12000, 6000, 48000);
			seedWhitelist = builder
					.comment("Add Item for What Villager can plant seed and compostable [example: farmersdelight:cabbage_seeds]")
					.defineList("Villager's Seed Whitelist"
							, Lists.newArrayList("farmersdelight:cabbage_seeds")
							, validator);
			plantableCropWhitelist = builder
					.comment("Add Item for What Villager can plant crop [example: minecraft:carrot]")
					.defineList("Villager's Plantable Crop Whitelist"
							, Lists.newArrayList("farmersdelight:onion")
							, validator);
			cropWhitelist = builder
					.comment("Add Item for What Villager can eatable about crop(you should set eatable food. when you set non eatable food. villager not eat forever!) [example: minecraft:carrot]")
					.defineList("Villager's Crop Whitelist"
							, Lists.newArrayList("neapolitan:strawberries", "neapolitan:banana", "farmersdelight:cabbage", "farmersdelight:onion", "farmersdelight:tomato")
							, validator);
			foodWhitelist = builder
					.comment("Add Item for What Villager can eatable about food(you should set eatable food. when you set non eatable food. villager not eat forever!) [example: minecraft:apple]")
					.defineList("Villager's Food Whitelist"
							, Lists.newArrayList("minecraft:cod", "minecraft:salmon", "neapolitan:strawberry_scones", "neapolitan:banana_bread", "neapolitan:dried_banana", "neapolitan:chocolate_strawberries"
									, "farmersdelight:cake_slice", "farmersdelight:apple_pie_slice", "farmersdelight:chocolate_pie_slice", "farmersdelight:sweet_berry_cookie", "farmersdelight:honey_cookie")
							, validator);
			huntableWhitelist = builder
					.comment("Add What Butcher Villager can Hunt [example: minecraft:cow]")
					.defineList("Butcher Villager 's Hunt Entity Whitelist"
							, Lists.newArrayList("minecraft:cow", "minecraft:sheep", "minecraft:chicken", "minecraft:rabbit", "minecraft:pig", "earthmobsmod:fancy_chicken", "earthmobsmod:jumbo_rabbit", "earthmobsmod:duck")
							, validator);
			feedWhitelist = builder
					.comment("Add What Farmer Villager can Feed[example: minecraft:cow]")
					.defineList("Farmer Villager 's Feed Entity Whitelist"
							, Lists.newArrayList("minecraft:cow", "minecraft:sheep", "minecraft:chicken", "minecraft:pig", "earthmobsmod:fancy_chicken", "earthmobsmod:duck")
							, validator);
		}
	}
}