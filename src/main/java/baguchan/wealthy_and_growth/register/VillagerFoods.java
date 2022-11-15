package baguchan.wealthy_and_growth.register;

import baguchan.wealthy_and_growth.WAGConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class VillagerFoods {
	public static final Map<Item, Integer> FOOD_POINTS = Maps.newHashMap();
	public static final Set<Item> WANTED_ITEMS = Sets.newHashSet(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);

	public static void registerFoods(){
		//VANILLA
		FOOD_POINTS.put(Items.BREAD, 4);
		FOOD_POINTS.put(Items.POTATO, 1);
		FOOD_POINTS.put(Items.CARROT, 1);
		FOOD_POINTS.put(Items.BEETROOT, 1);
		FOOD_POINTS.put(Items.PUMPKIN_PIE, 6);
		FOOD_POINTS.put(Items.MELON_SLICE, 2);

		WANTED_ITEMS.add(Items.PUMPKIN);
		WANTED_ITEMS.add(Items.PUMPKIN_PIE);
		WANTED_ITEMS.add(Items.MELON_SLICE);

		for (String name : WAGConfig.COMMON.seedWhitelist.get()) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
			if (item != null) {
				WANTED_ITEMS.add(item);
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
