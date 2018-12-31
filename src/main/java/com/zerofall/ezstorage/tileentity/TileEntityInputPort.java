package com.zerofall.ezstorage.tileentity;

import com.zerofall.ezstorage.util.BlockRef;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntityInputPort
		extends TileEntity
		implements IInventory, IUpdatePlayerListBox, ISidedInventory {
	private ItemStack[] inv = new ItemStack[1];

	public TileEntityStorageCore core;

	public int getSizeInventory() {
		return 1;
	}

	public ItemStack getStackInSlot(int index) {
		return this.inv[index];
	}

	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = getStackInSlot(index);
		if (stack!=null) {
			if (stack.stackSize<=count) {
				setInventorySlotContents(index, null);
			} else {
				stack = stack.splitStack(count);
				if (stack.stackSize==0) {
					setInventorySlotContents(index, null);
				}
			}
		}
		return stack;
	}

	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = getStackInSlot(index);
		if (stack!=null) {
			setInventorySlotContents(index, null);
		}
		return stack;
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inv[index] = stack;
		if ((stack!=null)&&(stack.stackSize>getInventoryStackLimit())) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	public void updateEntity() {
		if (this.core!=null) {
			ItemStack stack = this.inv[0];
			if ((stack!=null)&&(stack.stackSize>0)) {
				if (this.core.isPartOfMultiblock(new BlockRef(this))) {
					this.inv[0] = this.core.input(stack);
				} else {
					this.core = null;
				}
			}
		}
	}

	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		int[] slots = new int[1];
		slots[0] = 0;
		return slots;
	}

	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return true;
	}

	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}

	public String getInventoryName() {
		return "input_port";
	}

	public boolean hasCustomInventoryName() {
		return false;
	}

	public void openInventory() {
	}

	public void closeInventory() {
	}

	public void update() {
	}
}
