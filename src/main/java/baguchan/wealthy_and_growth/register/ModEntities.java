package baguchan.wealthy_and_growth.register;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import baguchan.wealthy_and_growth.entity.VillagerFishingHook;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WealthyAndGrowth.MODID);

    public static final RegistryObject<EntityType<VillagerFishingHook>> FISHING_HOOK = ENTITIES_REGISTRY.register("fishing_hook", () -> EntityType.Builder.<VillagerFishingHook>of(VillagerFishingHook::new, MobCategory.MISC).noSave().noSummon().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5).build(prefix("fishing_hook")));

    private static String prefix(String path) {
        return WealthyAndGrowth.MODID + "." + path;
    }
}
