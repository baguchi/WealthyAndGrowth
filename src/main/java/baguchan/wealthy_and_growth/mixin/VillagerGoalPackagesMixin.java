package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.WAGConfig;
import baguchan.wealthy_and_growth.entity.behavior.*;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(VillagerGoalPackages.class)
public class VillagerGoalPackagesMixin {
	@Inject(method = ("getCorePackage"), at = @At("RETURN"), cancellable = true)
	private static void getCorePackage(VillagerProfession profession, float p_24591_, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>>> ci) {
		List<Pair<Integer, ? extends Behavior<? super Villager>>> copy = new ArrayList<>(ci.getReturnValue());

		if (WAGConfig.COMMON.eatFood.get()) {
			copy.add(Pair.of(0, new EatFoodAndHeal()));
		}
		ci.setReturnValue(ImmutableList.copyOf(copy));
	}

	@Inject(method = ("getIdlePackage"), at = @At("RETURN"), cancellable = true)
	private static void getIdlePackage(VillagerProfession profession, float p_24591_, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>>> ci) {
		List<Pair<Integer, ? extends Behavior<? super Villager>>> copy = new ArrayList<>(ci.getReturnValue());
		if (profession.equals(VillagerProfession.FARMER)) {
			//copy.add(Pair.of(1, new FeedToAnimal()));
		}
		if (profession.equals(VillagerProfession.CLERIC)) {
			if (WAGConfig.COMMON.healWithPotion.get()) {
				copy.add(Pair.of(1, new HealVillager()));
			}
		}
		ci.setReturnValue(ImmutableList.copyOf(copy));
	}

	@Inject(method = ("getWorkPackage"), at = @At("RETURN"), cancellable = true)
	private static void getWorkPackage(VillagerProfession profession, float p_24591_, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>>> ci) {
		List<Pair<Integer, ? extends Behavior<? super Villager>>> copy = new ArrayList<>(ci.getReturnValue());
		if (profession.equals(VillagerProfession.FARMER)) {
			if (WAGConfig.COMMON.harvestPumpkinAndMelon.get()) {
				copy.add(Pair.of(2, new HarvestPumpkinAndMelon()));
			}
			//copy.add(Pair.of(2, new FeedToAnimal()));
		}
		if (profession.equals(VillagerProfession.CLERIC)) {
			if (WAGConfig.COMMON.healWithPotion.get()) {
				copy.add(Pair.of(2, new HealVillager()));
			}
		}


		if (profession.equals(VillagerProfession.FISHERMAN)) {
			if (WAGConfig.COMMON.fishing.get()) {
				copy.add(Pair.of(0, new Fishing()));
			}
		}
		if (profession.equals(VillagerProfession.BUTCHER)) {
			if (WAGConfig.COMMON.cookingMeat.get()) {
				copy.removeIf(integerPair -> {
					return integerPair.getSecond() instanceof WorkAtPoi;
				});
				copy.add(Pair.of(7, new WorkAtCooking()));
			}
			//copy.add(Pair.of(2, new Hunting()));
		}
		ci.setReturnValue(ImmutableList.copyOf(copy));
	}

	@Shadow
	private static Pair<Integer, ? extends Behavior<? super Villager>> getMinimalLookBehavior() {
		return null;
	}
}
