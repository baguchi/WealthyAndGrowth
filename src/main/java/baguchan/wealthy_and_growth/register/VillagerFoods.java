package baguchan.wealthy_and_growth.register;

import baguchan.wealthy_and_growth.WAGConfig;
import baguchan.wealthy_and_growth.WealthyAndGrowth;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = WealthyAndGrowth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VillagerFoods {
	public static final Map<Item, Integer> FOOD_POINTS = Maps.newHashMap();
	public static final Set<Item> WANTED_ITEMS = Sets.newHashSet();

	public static final List<Item> COMPOSTABLE_ITEMS = Lists.newArrayList();
	public static final List<Item> PLANTS_ITEMS = Lists.newArrayList();


	public static void registerFoods() {
		FOOD_POINTS.clear();
		WANTED_ITEMS.clear();
		COMPOSTABLE_ITEMS.clear();
		PLANTS_ITEMS.clear();

		//VANILLA
		FOOD_POINTS.put(Items.BREAD, 4);
		FOOD_POINTS.put(Items.POTATO, 1);
		FOOD_POINTS.put(Items.CARROT, 1);
		FOOD_POINTS.put(Items.BEETROOT, 1);
		FOOD_POINTS.put(Items.PUMPKIN_PIE, 6);
		FOOD_POINTS.put(Items.MELON_SLICE, 2);

		WANTED_ITEMS.add(Items.BREAD);
		WANTED_ITEMS.add(Items.POTATO);
		WANTED_ITEMS.add(Items.CARROT);
		WANTED_ITEMS.add(Items.BEETROOT);
		WANTED_ITEMS.add(Items.BEETROOT_SEEDS);
		WANTED_ITEMS.add(Items.WHEAT_SEEDS);
		WANTED_ITEMS.add(Items.WHEAT);
		WANTED_ITEMS.add(Items.PUMPKIN);
		WANTED_ITEMS.add(Items.PUMPKIN_PIE);
		WANTED_ITEMS.add(Items.MELON_SLICE);

		COMPOSTABLE_ITEMS.add(Items.BEETROOT_SEEDS);
		COMPOSTABLE_ITEMS.add(Items.WHEAT_SEEDS);

		PLANTS_ITEMS.add(Items.BEETROOT_SEEDS);
		PLANTS_ITEMS.add(Items.WHEAT_SEEDS);
		PLANTS_ITEMS.add(Items.POTATO);
		PLANTS_ITEMS.add(Items.CARROT);

		for (String name : WAGConfig.COMMON.seedWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
				COMPOSTABLE_ITEMS.add(item);
				PLANTS_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.plantableCropWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
				PLANTS_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.cropWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				FOOD_POINTS.put(item, 2);
				WANTED_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.foodWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				FOOD_POINTS.put(item, 4);
				WANTED_ITEMS.add(item);
			}
		}
	}

	@SubscribeEvent
	public static void reloadConfig(ModConfigEvent.Reloading event) {
		FOOD_POINTS.clear();
		WANTED_ITEMS.clear();
		COMPOSTABLE_ITEMS.clear();
		PLANTS_ITEMS.clear();

		//VANILLA
		FOOD_POINTS.put(Items.BREAD, 4);
		FOOD_POINTS.put(Items.POTATO, 1);
		FOOD_POINTS.put(Items.CARROT, 1);
		FOOD_POINTS.put(Items.BEETROOT, 1);
		FOOD_POINTS.put(Items.PUMPKIN_PIE, 6);
		FOOD_POINTS.put(Items.MELON_SLICE, 2);

		WANTED_ITEMS.add(Items.BREAD);
		WANTED_ITEMS.add(Items.POTATO);
		WANTED_ITEMS.add(Items.CARROT);
		WANTED_ITEMS.add(Items.BEETROOT);
		WANTED_ITEMS.add(Items.BEETROOT_SEEDS);
		WANTED_ITEMS.add(Items.WHEAT_SEEDS);
		WANTED_ITEMS.add(Items.WHEAT);
		WANTED_ITEMS.add(Items.PUMPKIN);
		WANTED_ITEMS.add(Items.PUMPKIN_PIE);
		WANTED_ITEMS.add(Items.MELON_SLICE);

		COMPOSTABLE_ITEMS.add(Items.BEETROOT_SEEDS);
		COMPOSTABLE_ITEMS.add(Items.WHEAT_SEEDS);

		for (String name : WAGConfig.COMMON.seedWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
				COMPOSTABLE_ITEMS.add(item);
				PLANTS_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.plantableCropWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
				PLANTS_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.cropWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				FOOD_POINTS.put(item, 2);
				WANTED_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.foodWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				FOOD_POINTS.put(item, 4);
				WANTED_ITEMS.add(item);
			}
		}
	}
}
