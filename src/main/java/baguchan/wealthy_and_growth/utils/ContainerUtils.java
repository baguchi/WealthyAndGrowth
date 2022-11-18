package baguchan.wealthy_and_growth.utils;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ContainerUtils {
	public static boolean hasAnyOf(SimpleContainer inventory, List<Item> p_18950_) {
		for (int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack itemstack = inventory.getItem(i);
			if (p_18950_.contains(itemstack.getItem()) && itemstack.getCount() > 0) {
				return true;
			}
		}

		return false;
	}
}
