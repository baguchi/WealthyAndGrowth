package baguchan.wealthy_and_growth.entity.behavior;

import baguchan.wealthy_and_growth.register.VillagerFoods;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

public class EatFoodAndHeal extends Behavior<Villager> {
	public EatFoodAndHeal() {
		super(ImmutableMap.of());
	}

	protected boolean checkExtraStartConditions(ServerLevel p_23174_, Villager p_23175_) {
		if (p_23175_.getHealth() >= p_23175_.getMaxHealth()) {
			return false;
		} else {
			return p_23175_.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && startHealing(p_23175_);
		}
	}

	public boolean startHealing(Villager villager) {
		if (countFoodPointsInInventory(villager) != 0) {
			for (int i = 0; i < villager.getInventory().getContainerSize(); ++i) {
				ItemStack itemstack = villager.getInventory().getItem(i);
				if (!itemstack.isEmpty()) {
					Integer integer = VillagerFoods.FOOD_POINTS.get(itemstack.getItem());
					if (integer != null) {
						villager.setItemInHand(InteractionHand.MAIN_HAND, itemstack.split(1));
						villager.startUsingItem(InteractionHand.MAIN_HAND);
						return true;
					}
				}
			}

		}
		return false;
	}

	private int countFoodPointsInInventory(Villager villager) {
		SimpleContainer simplecontainer = villager.getInventory();
		return VillagerFoods.FOOD_POINTS.entrySet().stream().mapToInt((p_186300_) -> {
			return simplecontainer.countItem(p_186300_.getKey()) * p_186300_.getValue();
		}).sum();
	}

	protected boolean canStillUse(ServerLevel p_23204_, Villager p_23205_, long p_23206_) {
		return p_23205_.isUsingItem();
	}

	protected boolean timedOut(long p_24152_) {
		return false;
	}
}
