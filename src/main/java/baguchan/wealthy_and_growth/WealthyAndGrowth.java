package baguchan.wealthy_and_growth;

import baguchan.wealthy_and_growth.capability.PlayerTargetCapability;
import baguchan.wealthy_and_growth.register.ModEntities;
import baguchan.wealthy_and_growth.register.VillagerFoods;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WealthyAndGrowth.MODID)
public class WealthyAndGrowth
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "wealthy_and_growth";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Capability<PlayerTargetCapability> PLAYER_TARGET_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public WealthyAndGrowth()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WAGConfig.COMMON_SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        VillagerFoods.registerFoods();
    }
}
