package baguchan.wealthy_and_growth.mixin;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractIllager.class)
public abstract class AbstractIllagerMixin extends Raider {
	protected AbstractIllagerMixin(EntityType<? extends Raider> p_37839_, Level p_37840_) {
		super(p_37839_, p_37840_);
	}

	@Override
	public void die(DamageSource p_37847_) {
		super.die(p_37847_);
		if(p_37847_.getEntity() instanceof Player) {
			p_37847_.getEntity().getCapability(WealthyAndGrowth.PLAYER_TARGET_CAPABILITY).ifPresent(cap -> {
				if(isPatrolLeader()){
					cap.setEffectiveTargetScale(cap.getEffectiveTargetScale() + 0.1F);
				}else {
					cap.setEffectiveTargetScale(cap.getEffectiveTargetScale() + 0.01F);
				}

			});
		}
	}
}
