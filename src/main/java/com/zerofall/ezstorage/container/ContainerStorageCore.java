package com.zerofall.ezstorage.container;

import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerStorageCore extends Container {
	public TileEntityStorageCore tileEntity;

	public ContainerStorageCore(EntityPlayer player, World world, int x, int y, int z) {
		this.tileEntity = ((TileEntityStorageCore) world.getTileEntity(x, y, z));
		int startingY = 18;
		int startingX = 8;
		net.minecraft.inventory.IInventory inventory = new InventoryBasic("title", false, rowCount()*9);
		for (int i = 0; i<rowCount(); i++) {
			for (int j = 0; j<9; j++) {
				int test1 = j+i*9;
				addSlotToContainer(new Slot(inventory, j+i*9, startingX+j*18, startingY+i*18));
			}
		}

		bindPlayerInventory(player.inventory);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i<3; i++) {
			for (int j = 0; j<9; j++) {
				int test2 = j+i*9+rowCount()*9;
				addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9,
						playerInventoryX()+j*18, playerInventoryY()+i*18));
			}
		}

		for (int i = 0; i<9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, playerInventoryX()+i*18, playerInventoryY()+58));
		}
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stack = null;
		Slot slotObject = (Slot) this.inventorySlots.get(index);
		if ((slotObject!=null)&&(slotObject.getHasStack())) {
			ItemStack stackInSlot = slotObject.getStack();
			slotObject.putStack(this.tileEntity.inventory.input(stackInSlot));
		}
		return null;
	}

	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		if ((slotId<rowCount()*9)&&(slotId>=0)) {
			return null;
		}
		return super.slotClick(slotId, clickedButton, mode, playerIn);
	}

	public ItemStack customSlotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		int itemIndex = slotId;
		ItemStack heldStack = playerIn.inventory.getItemStack();
		if (heldStack==null) {
			int type = 0;
			if (clickedButton==1) {
				type = 1;
			}
			ItemStack stack = this.tileEntity.inventory.getItemsAt(itemIndex, type);
			if (stack==null) {
				return null;
			}

			if ((clickedButton==0)&&(mode==1)) {
				if (!mergeItemStack(stack, rowCount()*9, rowCount()*9+36, true)) {
					this.tileEntity.inventory.input(stack);
				}
			} else {
				playerIn.inventory.setItemStack(stack);
			}
			return stack;
		}
		playerIn.inventory.setItemStack(this.tileEntity.inventory.input(heldStack));

		return null;
	}

	protected int playerInventoryX() {
		return 8;
	}

	protected int playerInventoryY() {
		return 140;
	}

	protected int rowCount() {
		return 6;
	}

	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if (!playerIn.worldObj.isRemote) {
			this.tileEntity.sortInventory();
		}
	}
}
