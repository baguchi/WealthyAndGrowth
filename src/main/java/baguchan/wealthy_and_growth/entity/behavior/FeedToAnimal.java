package baguchan.wealthy_and_growth.entity.behavior;

import baguchan.wealthy_and_growth.WAGConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.EventHooks;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FeedToAnimal extends Behavior<Villager> {
	private static final int HARVEST_DURATION = 200;
	public static final float SPEED_MODIFIER = 0.5F;
	private int timeWorkedSoFar;
	private Animal targetEntity;
	private Animal targetSecondEntity;
	private int usingSlot;

	public FeedToAnimal() {
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
	}

	protected boolean checkExtraStartConditions(ServerLevel p_23174_, Villager p_23175_) {
		if (!EventHooks.canEntityGrief(p_23174_, p_23175_)) {
			return false;
		} else if (p_23175_.getVillagerData().getProfession() != VillagerProfession.FARMER) {
			return false;
		} else {

			List<Animal> animals = p_23175_.level().getNearbyEntities(Animal.class, TargetingConditions.forCombat().range(16D), p_23175_, p_23175_.getBoundingBox().inflate(8, 6, 8));
			List<Animal> copy = Lists.newArrayList(animals);

			if (!copy.isEmpty() && copy.size() >= 2 && copy.size() < 6) {
				Collections.shuffle(copy);
				for (int i = 0; i < p_23175_.getInventory().getContainerSize(); ++i) {
					ItemStack stack = p_23175_.getInventory().getItem(i);

					if (WAGConfig.COMMON.feedWhitelist.get().contains(BuiltInRegistries.ENTITY_TYPE.getKey(copy.get(0).getType()).toString())) {
						if (!copy.get(0).isBaby() && copy.get(0).isFood(stack) && copy.get(0).canFallInLove() && p_23175_.hasLineOfSight(copy.get(0))) {
							targetEntity = copy.get(0);
							Optional<Animal> optional = copy.stream().filter(animal -> {
								return !animal.isBaby() && animal.canFallInLove() && animal.getType() == targetEntity.getType() && animal != targetEntity;
							}).findFirst();
							if (optional.isPresent()) {
								this.usingSlot = i;
								targetSecondEntity = optional.get();
							}
						}
					}
				}
			}

			return this.targetEntity != null && this.targetSecondEntity != null;
		}
	}

	protected void start(ServerLevel p_23177_, Villager p_23178_, long p_23179_) {
		if (this.targetEntity != null) {
			p_23178_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.targetEntity, true));
			p_23178_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(this.targetEntity, true), 0.6F, 1));
		}
	}

	protected void stop(ServerLevel p_23188_, Villager p_23189_, long p_23190_) {

		p_23189_.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
		p_23189_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		this.timeWorkedSoFar = 0;


	}

	protected void tick(ServerLevel p_23188_, Villager p_23189_, long p_23190_) {
		++this.timeWorkedSoFar;
		if (this.targetEntity != null && this.targetEntity.isAlive() && this.targetEntity.canFallInLove()) {
			p_23189_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.targetEntity, true));
			p_23189_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(this.targetEntity, true), 0.6F, 1));

			if (p_23189_.distanceToSqr(this.targetEntity) < 24.0F) {
				this.targetEntity.setInLoveTime(600);
				ItemStack stack = p_23189_.getInventory().getItem(this.usingSlot);
				stack.shrink(1);
			}

		} else if (this.targetSecondEntity != null && this.targetSecondEntity.isAlive() && this.targetSecondEntity.canFallInLove()) {
			p_23189_.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.targetSecondEntity, true));
			p_23189_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(this.targetSecondEntity, true), 0.6F, 1));

			if (p_23189_.distanceToSqr(this.targetSecondEntity) < 24.0F) {
				this.targetSecondEntity.setInLoveTime(600);
				ItemStack stack = p_23189_.getInventory().getItem(this.usingSlot);
				stack.shrink(1);
			}

		}
	}

	protected boolean canStillUse(ServerLevel p_23204_, Villager p_23205_, long p_23206_) {
		ItemStack stack = p_23205_.getInventory().getItem(this.usingSlot);
		return !stack.isEmpty() && this.timeWorkedSoFar < 400 && (this.targetEntity != null && this.targetEntity.isAlive()) && (this.targetSecondEntity != null && this.targetSecondEntity.isAlive()) && (this.targetEntity.canFallInLove() || this.targetSecondEntity.canFallInLove());
	}

	@Override
	protected boolean timedOut(long p_22537_) {
		return false;
	}
}
