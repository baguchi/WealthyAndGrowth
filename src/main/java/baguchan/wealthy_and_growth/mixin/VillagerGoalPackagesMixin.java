package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.entity.behavior.HarvestPumpkinAndMelon;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.*;
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
	@Inject(method = ("getWorkPackage"), at = @At("RETURN"))
	private static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getWorkPackage(VillagerProfession profession, float p_24591_, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>>> ci) {
		List<Pair<Integer, ? extends Behavior<? super Villager>>> copy = new ArrayList<>(ci.getReturnValue());
		if (profession.equals(VillagerProfession.FARMER)) {
			copy.add(Pair.of(6, new HarvestPumpkinAndMelon()));
		}
		return ImmutableList.copyOf(copy);
	}

	@Shadow
	private static Pair<Integer, ? extends Behavior<? super Villager>> getMinimalLookBehavior() {
		return null;
	}
}
