package com.zerofall.ezstorage.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

public class EZInventory {
	public List<ItemGroup> inventory;
	public long maxItems = 0L;

	public EZInventory() {
		this.inventory = new ArrayList<ItemGroup>();
	}

	public ItemStack input(final ItemStack itemStack) {
		if (getTotalCount()>=this.maxItems)
			return itemStack;
		final long space = this.maxItems-getTotalCount();

		final int amount = (int) Math.min(space, itemStack.stackSize);
		return mergeStack(itemStack, amount);
	}

	public void sort() {
		Collections.sort(this.inventory, new ItemGroup.CountComparator());
	}

	private ItemStack mergeStack(final ItemStack itemStack, final int amount) {
		for (final ItemGroup group : this.inventory)
			if (stacksEqual(group.itemStack, itemStack)) {
				group.count += amount;
				itemStack.stackSize -= amount;
				if (itemStack.stackSize<=0)
					return null;
				return itemStack;
			}

		this.inventory.add(new ItemGroup(itemStack, amount));
		itemStack.stackSize -= amount;
		if (itemStack.stackSize<=0)
			return null;
		return itemStack;
	}

	public ItemStack getItemsAt(final int index, final int type) {
		if (index>=this.inventory.size())
			return null;
		final ItemGroup group = this.inventory.get(index);
		final ItemStack stack = group.itemStack.copy();
		int size = (int) Math.min(stack.getMaxStackSize(), group.count);
		if (size>1)
			if (type==1)
				size /= 2;
			else if (type==2)
				size = 1;
		stack.stackSize = size;
		group.count -= size;
		if (group.count<=0L)
			this.inventory.remove(index);
		return stack;
	}

	public ItemStack getItems(final ItemStack[] itemStacks) {
		for (final ItemGroup group : this.inventory)
			for (final ItemStack itemStack : itemStacks)
				if (stacksEqual(group.itemStack, itemStack)) {
					if (group.count>=itemStack.stackSize) {
						final ItemStack stack = group.itemStack.copy();
						stack.stackSize = itemStack.stackSize;
						group.count -= itemStack.stackSize;
						if (group.count<=0L)
							this.inventory.remove(group);
						return stack;
					}
					return null;
				}
		return null;
	}

	public int slotCount() {
		return this.inventory.size();
	}

	public static boolean stacksEqual(final ItemStack stack1, final ItemStack stack2) {
		if (stack1==null&&stack2==null)
			return true;
		if (stack1==null||stack2==null)
			return false;
		if (
			stack1.getItem()==stack2.getItem()&&
					stack1.getItemDamage()==stack2.getItemDamage()&&
					stack1.getTagCompound()==stack2.getTagCompound()
		)
			return true;

		return false;
	}

	public long getTotalCount() {
		long count = 0L;
		for (final ItemGroup group : this.inventory)
			count += group.count;
		return count;
	}

	@Override
	public String toString() {
		return this.inventory.toString();
	}
}
