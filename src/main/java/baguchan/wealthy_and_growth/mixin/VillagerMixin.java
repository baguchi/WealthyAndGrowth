package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.api.IFishing;
import baguchan.wealthy_and_growth.entity.VillagerFishingHook;
import baguchan.wealthy_and_growth.register.VillagerFoods;
import baguchan.wealthy_and_growth.utils.ContainerUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager implements IFishing {
	@Shadow
	private int foodLevel;
	@Nullable
	public VillagerFishingHook fishing;

	public VillagerMixin(EntityType<? extends AbstractVillager> p_35267_, Level p_35268_) {
		super(p_35267_, p_35268_);
	}

	@Inject(at = @At("HEAD"), method = "eatUntilFull", cancellable = true)
	private void eatUntilFull(CallbackInfo callbackInfo) {
		if (this.hungry() && this.countFoodPointsInInventory() != 0) {
			for (int i = 0; i < this.getInventory().getContainerSize(); ++i) {
				ItemStack itemstack = this.getInventory().getItem(i);
				if (!itemstack.isEmpty()) {
					Integer integer = VillagerFoods.FOOD_POINTS.get(itemstack.getItem());
					if (integer != null) {
						int j = itemstack.getCount();

						for (int k = j; k > 0; --k) {
							this.foodLevel = (byte) (this.foodLevel + integer);
							this.getInventory().removeItem(i, 1);
							if (!this.hungry()) {
								callbackInfo.cancel();
								break;
							}
						}
					}
				}
			}

		}
	}

	@Shadow
	private boolean hungry() {
		return false;
	}

	@Nullable
	public VillagerFishingHook getFishingHook() {
		return fishing;
	}

	public void setFishingHook(@Nullable VillagerFishingHook fishing) {
		this.fishing = fishing;
	}

	private int countFoodPointsInInventory() {
		SimpleContainer simplecontainer = this.getInventory();
		return VillagerFoods.FOOD_POINTS.entrySet().stream().mapToInt((p_186300_) -> {
			return simplecontainer.countItem(p_186300_.getKey()) * p_186300_.getValue();
		}).sum();
	}

	@Inject(at = @At("RETURN"), method = "wantsToPickUp", cancellable = true)
	public void wantsToPickUp(ServerLevel p_376823_, ItemStack p_35543_, CallbackInfoReturnable<Boolean> cir) {
		Item item = p_35543_.getItem();
		cir.setReturnValue((VillagerFoods.WANTED_ITEMS.contains(item) || cir.getReturnValue() || this.getVillagerData().getProfession().requestedItems().contains(item)) && this.getInventory().canAddItem(p_35543_));
	}

	@Override
	public void stopUsingItem() {
		FoodProperties foodProperties = this.getUseItem().get(DataComponents.FOOD);
		if (foodProperties != null) {
			this.heal(foodProperties.nutrition());
		}
		super.stopUsingItem();
	}


	@Shadow
	public VillagerData getVillagerData() {
		return null;
	}

	@Inject(at = @At("RETURN"), method = "hasFarmSeeds", cancellable = true)
	public void hasFarmSeeds(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		callbackInfoReturnable.setReturnValue(ContainerUtils.hasAnyOf(this.getInventory(), VillagerFoods.PLANTS_ITEMS) || callbackInfoReturnable.getReturnValue());
	}
}
