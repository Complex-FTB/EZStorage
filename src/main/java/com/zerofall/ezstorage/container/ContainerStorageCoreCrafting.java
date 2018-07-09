package com.zerofall.ezstorage.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerStorageCoreCrafting extends ContainerStorageCore {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new net.minecraft.inventory.InventoryCraftResult();
	private World worldObj;

	public ContainerStorageCoreCrafting(final EntityPlayer player, final World world, final int x, final int y, final int z) {
		super(player, world, x, y, z);
		this.worldObj = world;
		addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 116, 117));

		for (int i = 0; i<3; i++)
			for (int j = 0; j<3; j++)
				addSlotToContainer(new Slot(this.craftMatrix, j+i*3, 44+j*18, 99+i*18));
		onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(final IInventory inventoryIn) {
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
		final Slot slotObject = (Slot) this.inventorySlots.get(index);
		if (slotObject!=null&&slotObject.getHasStack()) {
			if (slotObject instanceof SlotCrafting) {
				final ItemStack[] recipe = new ItemStack[9];
				for (int i = 0; i<9; i++)
					recipe[i] = this.craftMatrix.getStackInSlot(i);

				ItemStack itemstack1 = slotObject.getStack();
				ItemStack itemstack = null;
				final ItemStack original = itemstack1.copy();
				int crafted = 0;
				final int maxStackSize = itemstack1.getMaxStackSize();
				final int crafting = itemstack1.stackSize;
				for (int i = 0; i<itemstack1.getMaxStackSize(); i++) {
					if (!slotObject.getHasStack()||!slotObject.getStack().isItemEqual(itemstack1))
						break;
					if (crafting>=maxStackSize)
						return null;
					itemstack1 = slotObject.getStack();
					itemstack = itemstack1.copy();
					if (crafted+itemstack1.stackSize>itemstack1.getMaxStackSize())
						return null;
					final boolean merged = mergeItemStack(itemstack1, rowCount()*9, rowCount()*9+36, true);
					if (!merged)
						return null;

					crafted += itemstack.stackSize;
					slotObject.onSlotChange(itemstack1, itemstack);
					slotObject.onPickupFromSlot(playerIn, itemstack1);

					if (slotObject.getStack()==null||!original.isItemEqual(slotObject.getStack()))
						tryToPopulateCraftingGrid(recipe);
				}

				if (itemstack==null||itemstack1.stackSize==itemstack.stackSize)
					return null;

				return itemstack;
			}
			final ItemStack stackInSlot = slotObject.getStack();
			slotObject.putStack(this.tileEntity.inventory.input(stackInSlot));
		}

		return null;
	}

	@Override
	public ItemStack slotClick(final int slotId, final int clickedButton, final int mode, final EntityPlayer playerIn) {
		if (slotId>=0&&mode==0&&clickedButton==0) {
			final ItemStack[] recipe = new ItemStack[9];
			for (int i = 0; i<9; i++)
				recipe[i] = this.craftMatrix.getStackInSlot(i);
			final ItemStack result = super.slotClick(slotId, clickedButton, mode, playerIn);
			if (result!=null) {
				final Slot slotObject = (Slot) this.inventorySlots.get(slotId);
				if (slotObject!=null&&slotObject instanceof SlotCrafting)
					tryToPopulateCraftingGrid(recipe);
			}
			return result;
		}
		final ItemStack result = super.slotClick(slotId, clickedButton, mode, playerIn);
		return result;
	}

	private void tryToPopulateCraftingGrid(final ItemStack[] recipe) {
		for (int j = 0; j<recipe.length; j++)
			if (
				recipe[j]!=null&&
						recipe[j].stackSize<=1
			) {

				recipe[j].stackSize = 1;

				final Slot slot = getSlotFromInventory(this.craftMatrix, j);
				if (slot!=null) {
					final ItemStack retreived = this.tileEntity.inventory.getItems(new ItemStack[] { recipe[j] });
					if (retreived!=null)
						slot.putStack(retreived);
				}
			}
	}

	@Override
	protected int playerInventoryY() {
		return 162;
	}

	@Override
	protected int rowCount() {
		return 4;
	}

	@Override
	public void onContainerClosed(final EntityPlayer playerIn) {
		for (int i = 0; i<9; i++) {
			final ItemStack stack = this.craftMatrix.getStackInSlot(i);
			if (stack!=null) {
				final ItemStack result = this.tileEntity.input(stack);
				if (result!=null)
					playerIn.dropPlayerItemWithRandomChoice(result, false);
			}
		}
		super.onContainerClosed(playerIn);
	}
}
