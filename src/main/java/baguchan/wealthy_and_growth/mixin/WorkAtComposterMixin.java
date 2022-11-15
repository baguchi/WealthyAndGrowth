package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.WAGConfig;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WorkAtComposter.class)
public class WorkAtComposterMixin {

	@Inject(at = @At("HEAD"), method = "useWorkstation", cancellable = true)
	protected void useWorkstation(ServerLevel p_24790_, Villager p_24791_, CallbackInfo callbackInfo) {
		Optional<GlobalPos> optional = p_24791_.getBrain().getMemory(MemoryModuleType.JOB_SITE);
		if (optional.isPresent()) {
			GlobalPos globalpos = optional.get();
			BlockState blockstate = p_24790_.getBlockState(globalpos.pos());
			if (blockstate.is(Blocks.COMPOSTER)) {
				this.makeBread(p_24791_);
				this.makeFood(p_24791_);
				this.compostItems(p_24790_, p_24791_, globalpos, blockstate);
			}

		}
		callbackInfo.cancel();

	}

	@Shadow
	private void compostItems(ServerLevel p_24793_, Villager p_24794_, GlobalPos p_24795_, BlockState p_24796_) {
	}

	@Shadow
	private void makeBread(Villager p_24803_) {

	}

	private void makeFood(Villager p_24803_) {
		SimpleContainer simplecontainer = p_24803_.getInventory();
		if (simplecontainer.countItem(Items.PUMPKIN) <= 4) {
			int i = simplecontainer.countItem(Items.PUMPKIN);
			if (i != 0) {
				int i1 = i * 2;
				simplecontainer.removeItemType(Items.PUMPKIN, i);
				ItemStack itemstack = simplecontainer.addItem(new ItemStack(Items.PUMPKIN_PIE, i1));
				if (!itemstack.isEmpty()) {
					p_24803_.spawnAtLocation(itemstack, 0.5F);
				}

			}
		}
	}
}
