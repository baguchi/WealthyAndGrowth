package baguchan.wealthy_and_growth.client;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import baguchan.wealthy_and_growth.register.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WealthyAndGrowth.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FISHING_HOOK.get(), VillagerFishingHookRenderer::new);
    }
}
