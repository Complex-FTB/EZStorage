package com.zerofall.ezstorage.util;

import net.minecraft.item.ItemStack;

public class ItemGroup {
	public ItemStack itemStack;
	public long count;

	public ItemGroup(ItemStack itemStack) {
		this.itemStack = itemStack;
		this.count = itemStack.stackSize;
	}

	public ItemGroup(ItemStack itemStack, long count) {
		this.itemStack = itemStack;
		this.count = count;
	}

	public String toString() {
		return this.itemStack.getDisplayName()+":"+this.count;
	}

	public static class CountComparator implements java.util.Comparator<ItemGroup> {
		public int compare(ItemGroup o1, ItemGroup o2) {
			Long l1 = Long.valueOf(o1.count);
			Long l2 = Long.valueOf(o2.count);
			return l2.compareTo(l1);
		}
	}
}
