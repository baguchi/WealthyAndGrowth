package baguchan.wealthy_and_growth.api;

import baguchan.wealthy_and_growth.entity.VillagerFishingHook;

import javax.annotation.Nullable;

public interface IFishing {
    @Nullable
    VillagerFishingHook getFishingHook();

    void setFishingHook(@Nullable VillagerFishingHook fishingHook);
}
