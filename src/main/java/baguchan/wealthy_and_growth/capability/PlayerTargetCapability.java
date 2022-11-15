package baguchan.wealthy_and_growth.capability;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerTargetCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
	private float effectiveTargetScale;

	public void tick(LivingEntity entity) {
		if(entity.level.dayTime() % 1200L == 0) {
			if (entity.level instanceof ServerLevel) {
				if (((ServerLevel) entity.level).isCloseToVillage(entity.blockPosition(), 2)) {
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

	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		return (capability == WealthyAndGrowth.PLAYER_TARGET_CAPABILITY) ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
	}

	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		nbt.putFloat("EffectiveTargetScale", this.effectiveTargetScale);

		return nbt;
	}

	public void deserializeNBT(CompoundTag nbt) {
		this.effectiveTargetScale = nbt.getFloat("EffectiveTargetScale");
	}
}