package baguchan.wealthy_and_growth.entity.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

public class HealVillager extends Behavior<Villager> {
    private long nextOfStartTime;

    public HealVillager() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET,
                MemoryStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel p_23174_, Villager p_23175_) {
        if (p_23174_.getGameTime() - nextOfStartTime < 80L) {
            return false;
        } else if (p_23175_.getVillagerData().getProfession() != VillagerProfession.CLERIC) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void start(ServerLevel serverLevel, Villager villager, long p_22542_) {
        super.start(serverLevel, villager, p_22542_);
        this.nextOfStartTime = serverLevel.getGameTime() + 80;
        LivingEntity livingEntity = null;
        if (villager.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            livingEntity = villager.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        }

        if (livingEntity instanceof Player player) {
            if (villager.getGossips().getReputation(player.getUUID(), test -> true) <= 4) {
                livingEntity = null;
            }
        }

        if (livingEntity != null) {
            Vec3 vec3 = livingEntity.getDeltaMovement();
            double d0 = livingEntity.getX() + vec3.x - villager.getX();
            double d1 = livingEntity.getEyeY() - 1.1F - villager.getY();
            double d2 = livingEntity.getZ() + vec3.z - villager.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            ThrownPotion thrownpotion = new ThrownPotion(villager.level(), villager);
            thrownpotion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, Potions.HEALING));
            thrownpotion.setXRot(thrownpotion.getXRot() - -20.0F);
            thrownpotion.shoot(d0, d1 + d3 * 0.2, d2, 0.75F, 8.0F);
            if (!villager.isSilent()) {
                villager.level()
                        .playSound(
                                null,
                                villager.getX(),
                                villager.getY(),
                                villager.getZ(),
                                SoundEvents.WITCH_THROW,
                                villager.getSoundSource(),
                                1.0F,
                                0.8F + villager.getRandom().nextFloat() * 0.4F
                        );
            }

            serverLevel.addFreshEntity(thrownpotion);
        }

    }
}
