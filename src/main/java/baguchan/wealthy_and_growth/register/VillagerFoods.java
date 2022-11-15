package baguchan.wealthy_and_growth.register;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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
	}
}
