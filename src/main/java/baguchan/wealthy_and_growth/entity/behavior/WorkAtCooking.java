package baguchan.wealthy_and_growth.entity.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class WorkAtCooking extends WorkAtPoi {

    private RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    protected void useWorkstation(ServerLevel p_24790_, Villager p_24791_) {
        Optional<GlobalPos> optional = p_24791_.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if (!optional.isEmpty()) {
            GlobalPos globalpos = optional.get();
            BlockState blockstate = p_24790_.getBlockState(globalpos.pos());
            if (blockstate.is(Blocks.SMOKER)) {
                this.makeMeat(p_24790_, p_24791_, globalpos, blockstate);
            }

        }
    }

    private void makeMeat(ServerLevel p_24793_, Villager p_24794_, GlobalPos p_24795_, BlockState p_24796_) {
        BlockPos blockpos = p_24795_.pos();
        if (quickCheck == null) {
            quickCheck = RecipeManager.createCheck(RecipeType.SMOKING);
        }

        Optional<? extends AbstractCookingRecipe> smokingRecipeRecipeHolder = quickCheck.getRecipeFor(p_24794_.getInventory(), p_24793_);
        SimpleContainer simplecontainer = p_24794_.getInventory();
        int k = simplecontainer.getContainerSize();
        BlockState blockstate = p_24796_;

        if (smokingRecipeRecipeHolder.isPresent()) {
            for (int l = 0; l < k; ++l) {
                ItemStack stack = smokingRecipeRecipeHolder.get().assemble(simplecontainer);
                if (smokingRecipeRecipeHolder.get().getIngredients().get(0).test(simplecontainer.getItem(l))) {
                    simplecontainer.setItem(l, stack);
                    break;
                }
            }
        }

        this.spawnComposterFillEffects(p_24793_, p_24796_, blockpos, blockstate);
    }

    private void spawnComposterFillEffects(ServerLevel p_24798_, BlockState p_24799_, BlockPos p_24800_, BlockState p_24801_) {
        p_24798_.levelEvent(1500, p_24800_, p_24801_ != p_24799_ ? 1 : 0);
    }
}