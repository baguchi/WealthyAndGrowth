package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.register.VillagerFoods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorkAtComposter.class)
public class WorkAtComposterMixin {

    @Inject(at = @At("HEAD"), method = "compostItems", cancellable = true)
    private void compostItems(ServerLevel p_24793_, Villager p_24794_, GlobalPos p_24795_, BlockState p_24796_, CallbackInfo ci) {
        BlockPos blockpos = p_24795_.pos();
        if (p_24796_.getValue(ComposterBlock.LEVEL) == 8) {
            p_24796_ = ComposterBlock.extractProduce(p_24794_, p_24796_, p_24793_, blockpos);
        }

        int i = 20;
        int j = 10;
        int[] aint = new int[VillagerFoods.COMPOSTABLE_ITEMS.size()];
        SimpleContainer simplecontainer = p_24794_.getInventory();
        int k = simplecontainer.getContainerSize();
        BlockState blockstate = p_24796_;

        for (int l = k - 1; l >= 0 && i > 0; --l) {
            ItemStack itemstack = simplecontainer.getItem(l);
            int i1 = VillagerFoods.COMPOSTABLE_ITEMS.indexOf(itemstack.getItem());
            if (i1 != -1) {
                int j1 = itemstack.getCount();
                int k1 = aint[i1] + j1;
                aint[i1] = k1;
                int l1 = Math.min(Math.min(k1 - 10, i), j1);
                if (l1 > 0) {
                    i -= l1;

                    for (int i2 = 0; i2 < l1; ++i2) {
                        blockstate = ComposterBlock.insertItem(p_24794_, blockstate, p_24793_, itemstack, blockpos);
                        if (blockstate.getValue(ComposterBlock.LEVEL) == 7) {
                            this.spawnComposterFillEffects(p_24793_, p_24796_, blockpos, blockstate);
                            ci.cancel();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Shadow
    private void spawnComposterFillEffects(ServerLevel p_24798_, BlockState p_24799_, BlockPos p_24800_, BlockState p_24801_) {
    }


    @Inject(at = @At("HEAD"), method = "makeBread")
    private void wealthyAndGrowth$makeFood(ServerLevel p_376900_, Villager p_24803_, CallbackInfo ci) {
        SimpleContainer simplecontainer = p_24803_.getInventory();
        if (simplecontainer.countItem(Items.PUMPKIN_PIE) <= 36) {
            int i = simplecontainer.countItem(Items.PUMPKIN);
            if (i != 0) {
                int i1 = i * 2;
                simplecontainer.removeItemType(Items.PUMPKIN, i);
                ItemStack itemstack = simplecontainer.addItem(new ItemStack(Items.PUMPKIN_PIE, i1));
                if (!itemstack.isEmpty()) {
                    p_24803_.spawnAtLocation(p_376900_, itemstack, 0.5F);
                }

            }
        }
    }
}
