package com.zerofall.ezstorage.item;

import com.zerofall.ezstorage.EZStorage;
import net.minecraft.item.Item;

public class EZItem extends Item {
	public EZItem(String name) {
		registerItem(name, this);
	}

	public static void registerItem(String name, Item item) {
		item.setCreativeTab(EZStorage.instance.creativeTab);
		item.setUnlocalizedName(name);
		cpw.mods.fml.common.registry.GameRegistry.registerItem(item, name);
	}
}
