package baguchan.wealthy_and_growth.client;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import baguchan.wealthy_and_growth.register.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = WealthyAndGrowth.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.FISHING_HOOK.get(), VillagerFishingHookRenderer::new);
    }
}
