package baguchan.wealthy_and_growth.utils;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;

import java.util.List;

public class ContainerUtils {
	public static boolean hasAnyOf(SimpleContainer inventory, List<Item> p_18950_) {
		return inventory.hasAnyMatching((p_216873_) -> {
			return !p_216873_.isEmpty() && p_18950_.contains(p_216873_.getItem());
		});
	}
}
