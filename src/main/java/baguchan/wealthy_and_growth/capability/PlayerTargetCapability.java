package baguchan.wealthy_and_growth.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class PlayerTargetCapability implements ValueIOSerializable {
	private float effectiveTargetScale;

	public void tick(LivingEntity entity) {
		if (entity.level().dayTime() % 1200L == 0) {
			if (entity.level() instanceof ServerLevel) {
				if (((ServerLevel) entity.level()).isCloseToVillage(entity.blockPosition(), 2)) {
					setEffectiveTargetScale(getEffectiveTargetScale() + 0.01F);
				} else {
					setEffectiveTargetScale(getEffectiveTargetScale() - 0.04F);
				}
			}

		}
	}

	public void setEffectiveTargetScale(float effectiveTargetScale) {
		float f = Mth.clamp(effectiveTargetScale, 0.0F , 1.0F);
		this.effectiveTargetScale = f;
	}

	public float getEffectiveTargetScale() {
		return effectiveTargetScale;
	}

	@Override
	public void serialize(ValueOutput valueOutput) {
		CompoundTag nbt = new CompoundTag();

		nbt.putFloat("EffectiveTargetScale", this.effectiveTargetScale);

	}

	@Override
	public void deserialize(ValueInput valueInput) {
		this.effectiveTargetScale = valueInput.getFloatOr("EffectiveTargetScale", 0);

	}
}