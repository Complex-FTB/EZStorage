package com.zerofall.ezstorage;

import com.zerofall.ezstorage.init.EZBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class EZTab extends CreativeTabs {
	public EZTab() {
		super("EZStorage");
	}

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(EZBlocks.storage_core);
    }

}
