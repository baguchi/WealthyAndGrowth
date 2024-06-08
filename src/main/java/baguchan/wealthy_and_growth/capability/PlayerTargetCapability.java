package baguchan.wealthy_and_growth.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerTargetCapability implements INBTSerializable<CompoundTag> {
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
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag nbt = new CompoundTag();

		nbt.putFloat("EffectiveTargetScale", this.effectiveTargetScale);

		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		this.effectiveTargetScale = nbt.getFloat("EffectiveTargetScale");
	}
}