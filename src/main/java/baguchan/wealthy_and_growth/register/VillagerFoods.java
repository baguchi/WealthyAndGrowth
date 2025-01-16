package baguchan.wealthy_and_growth.register;

import baguchan.wealthy_and_growth.WAGConfig;
import baguchan.wealthy_and_growth.WealthyAndGrowth;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

@EventBusSubscriber(modid = WealthyAndGrowth.MODID, bus = EventBusSubscriber.Bus.MOD)
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
		FOOD_POINTS.put(Items.PUMPKIN_PIE, 6);
		FOOD_POINTS.put(Items.MELON_SLICE, 2);
		FOOD_POINTS.put(Items.PORKCHOP, 1);
		FOOD_POINTS.put(Items.BEEF, 1);
		FOOD_POINTS.put(Items.MUTTON, 1);
		FOOD_POINTS.put(Items.CHICKEN, 1);
		FOOD_POINTS.put(Items.COOKED_PORKCHOP, 3);
		FOOD_POINTS.put(Items.COOKED_BEEF, 3);
		FOOD_POINTS.put(Items.COOKED_MUTTON, 3);
		FOOD_POINTS.put(Items.COOKED_CHICKEN, 3);

		WANTED_ITEMS.add(Items.PUMPKIN);
		WANTED_ITEMS.add(Items.PUMPKIN_PIE);
		WANTED_ITEMS.add(Items.MELON_SLICE);
		WANTED_ITEMS.add(Items.PORKCHOP);
		WANTED_ITEMS.add(Items.BEEF);
		WANTED_ITEMS.add(Items.MUTTON);
		WANTED_ITEMS.add(Items.CHICKEN);
		WANTED_ITEMS.add(Items.COOKED_PORKCHOP);
		WANTED_ITEMS.add(Items.COOKED_BEEF);
		WANTED_ITEMS.add(Items.COOKED_MUTTON);
		WANTED_ITEMS.add(Items.COOKED_CHICKEN);

		for (String name : WAGConfig.COMMON.seedWhitelist.get()) {
			Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
				COMPOSTABLE_ITEMS.add(item);
				PLANTS_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.plantableCropWhitelist.get()) {
			Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
				PLANTS_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.cropWhitelist.get()) {
			Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(name));
			if (item != null) {
				FOOD_POINTS.put(item, 2);
				WANTED_ITEMS.add(item);
			}
		}

		for (String name : WAGConfig.COMMON.foodWhitelist.get()) {
			Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(name));
			if (item != null) {
				FOOD_POINTS.put(item, 4);
				WANTED_ITEMS.add(item);
			}
		}
	}

	@SubscribeEvent
	public static void reloadConfig(ModConfigEvent.Reloading event) {
		registerFoods();
	}
}
