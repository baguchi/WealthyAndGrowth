package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.register.VillagerFoods;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillager {
	@Shadow
	private int foodLevel;

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
									return;
								}
							}
						}
					}
				}

			}
			callbackInfo.cancel();
	}

	@Shadow
	private boolean hungry() {
		return false;
	}

	private int countFoodPointsInInventory() {
		SimpleContainer simplecontainer = this.getInventory();
		return VillagerFoods.FOOD_POINTS.entrySet().stream().mapToInt((p_186300_) -> {
			return simplecontainer.countItem(p_186300_.getKey()) * p_186300_.getValue();
		}).sum();
	}

	@Inject(at = @At("HEAD"), method = "wantsToPickUp", cancellable = true)
	public void wantsToPickUp(ItemStack p_35543_, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		Item item = p_35543_.getItem();
		callbackInfoReturnable.setReturnValue((VillagerFoods.WANTED_ITEMS.contains(item) || this.getVillagerData().getProfession().requestedItems().contains(item)) && this.getInventory().canAddItem(p_35543_));
	}

	@Shadow
	public VillagerData getVillagerData() {
		return null;
	}

	@Override
	public ItemStack eat(Level p_21067_, ItemStack p_21068_) {
		if (p_21068_.isEdible()) {
			heal(p_21068_.getFoodProperties(this).getNutrition());
		}

		return super.eat(p_21067_, p_21068_);
	}
}
