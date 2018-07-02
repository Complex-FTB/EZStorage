package com.zerofall.ezstorage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class EZTab extends CreativeTabs {
	public EZTab() {
		super("rt.name");
	}

	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return Items.nether_star;
	}
}
