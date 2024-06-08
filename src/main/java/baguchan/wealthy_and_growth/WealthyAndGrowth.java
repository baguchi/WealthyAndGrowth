package baguchan.wealthy_and_growth;

import baguchan.wealthy_and_growth.register.ModAttachs;
import baguchan.wealthy_and_growth.register.ModEntities;
import baguchan.wealthy_and_growth.register.VillagerFoods;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WealthyAndGrowth.MODID)
public class WealthyAndGrowth
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wealthy_and_growth";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public WealthyAndGrowth(ModContainer modContainer, IEventBus modBus)
    {

        // Register the commonSetup method for modloading
        modBus.addListener(this::commonSetup);
        ModAttachs.ATTACHMENT_TYPES.register(modBus);
        ModEntities.ENTITIES_REGISTRY.register(modBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, WAGConfig.COMMON_SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        VillagerFoods.registerFoods();
    }
}
