package com.zerofall.ezstorage;

import com.zerofall.ezstorage.init.EZBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EZTab extends CreativeTabs {
	public EZTab() {
		super("EZStorage");
	}

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(EZBlocks.storage_core);
    }

}
