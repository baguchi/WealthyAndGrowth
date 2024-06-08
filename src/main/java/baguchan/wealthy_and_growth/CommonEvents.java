package baguchan.wealthy_and_growth;

import baguchan.wealthy_and_growth.register.ModAttachs;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = WealthyAndGrowth.MODID)
public class CommonEvents {

	@SubscribeEvent
	public static void onUpdate(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity livingEntity) {
			livingEntity.getData(ModAttachs.PLAYER_TARGET.get()).tick(livingEntity);
		}
	}
}
