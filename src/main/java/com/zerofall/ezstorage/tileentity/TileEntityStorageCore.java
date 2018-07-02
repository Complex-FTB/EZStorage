package com.zerofall.ezstorage.tileentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zerofall.ezstorage.block.BlockCraftingBox;
import com.zerofall.ezstorage.block.BlockSearchBox;
import com.zerofall.ezstorage.block.BlockStorage;
import com.zerofall.ezstorage.block.BlockStorageCore;
import com.zerofall.ezstorage.block.StorageMultiblock;
import com.zerofall.ezstorage.util.BlockRef;
import com.zerofall.ezstorage.util.EZInventory;
import com.zerofall.ezstorage.util.EZStorageUtils;
import com.zerofall.ezstorage.util.ItemGroup;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

public class TileEntityStorageCore extends TileEntity implements IUpdatePlayerListBox {

	public EZInventory inventory;

	Set<BlockRef> multiblock = new HashSet<BlockRef>();
	private boolean firstTick = false;
	public boolean hasCraftBox = false;
	public boolean hasSearchBox = false;

	public TileEntityStorageCore() {
		this.inventory = new EZInventory();
	}

	public ItemStack input(final ItemStack stack) {
		final ItemStack result = this.inventory.input(stack);
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		markDirty();
		return result;
	}

	public void sortInventory() {
		this.inventory.sort();
		updateTileEntity();
	}

	public void updateTileEntity() {
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		markDirty();
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, getBlockMetadata(), nbtTag);
	}

	@Override
	public void writeToNBT(final NBTTagCompound paramNBTTagCompound) {
		super.writeToNBT(paramNBTTagCompound);
		final NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i<this.inventory.slotCount(); i++) {
			final ItemGroup group = this.inventory.inventory.get(i);
			if (group!=null&&group.itemStack!=null&&group.count>0L) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Index", (byte) i);
				group.itemStack.writeToNBT(nbttagcompound1);
				nbttagcompound1.setLong("InternalCount", group.count);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		paramNBTTagCompound.setTag("Internal", nbttaglist);
		paramNBTTagCompound.setLong("InternalMax", this.inventory.maxItems);
		paramNBTTagCompound.setBoolean("hasSearchBox", this.hasSearchBox);
		paramNBTTagCompound.setBoolean("hasCraftBox", this.hasCraftBox);
	}

	@Override
	public void readFromNBT(final NBTTagCompound paramNBTTagCompound) {
		super.readFromNBT(paramNBTTagCompound);
		final NBTTagList nbttaglist = paramNBTTagCompound.getTagList("Internal", 10);

		if (nbttaglist!=null) {
			this.inventory.inventory = new ArrayList<ItemGroup>();
			for (int i = 0; i<nbttaglist.tagCount(); i++) {
				final NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				final int j = nbttagcompound1.getByte("Index")&0xFF;
				final ItemStack stack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
				final long count = nbttagcompound1.getLong("InternalCount");
				final ItemGroup group = new ItemGroup(stack, count);
				this.inventory.inventory.add(group);
			}
		}
		final long maxItems = paramNBTTagCompound.getLong("InternalMax");
		this.inventory.maxItems = maxItems;
		this.hasSearchBox = paramNBTTagCompound.getBoolean("hasSearchBox");
		this.hasCraftBox = paramNBTTagCompound.getBoolean("hasCraftBox");
	}

	public void scanMultiblock() {
		this.inventory.maxItems = 0L;
		this.hasCraftBox = false;
		this.hasSearchBox = false;
		this.multiblock = new HashSet<BlockRef>();
		final BlockRef ref = new BlockRef(this);
		this.multiblock.add(ref);
		getValidNeighbors(ref);
		for (final BlockRef blockRef : this.multiblock)
			if (blockRef.block instanceof BlockStorage) {
				final BlockStorage sb = (BlockStorage) blockRef.block;
				this.inventory.maxItems += sb.getCapacity();
			}
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	private void getValidNeighbors(final BlockRef br) {
		final List<BlockRef> neighbors = EZStorageUtils.getNeighbors(br.pos.x, br.pos.y, br.pos.z, this.worldObj);
		for (final BlockRef blockRef : neighbors)
			if (
				blockRef.block instanceof StorageMultiblock&&
						this.multiblock.add(blockRef)==true&&validateSystem()==true
			) {
				if (blockRef.block instanceof com.zerofall.ezstorage.block.BlockInputPort) {
					final TileEntityInputPort entity = (TileEntityInputPort) this.worldObj.getTileEntity(blockRef.pos.x, blockRef.pos.y, blockRef.pos.z);
					entity.core = this;
				}
				if (blockRef.block instanceof BlockCraftingBox)
					this.hasCraftBox = true;
				if (blockRef.block instanceof BlockSearchBox)
					this.hasSearchBox = true;
				getValidNeighbors(blockRef);
			}
	}

	public boolean validateSystem() {
		int count = 0;
		for (final BlockRef ref : this.multiblock) {
			if (ref.block instanceof BlockStorageCore)
				count++;
			if (count>1) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("You can only have 1 Storage Core per system!"));
				return false;
			}
		}
		return true;
	}

	public boolean isPartOfMultiblock(final BlockRef blockRef) {
		if (
			this.multiblock!=null&&
					this.multiblock.contains(blockRef)
		)
			return true;

		return false;
	}

	@Override
	public void updateEntity() {
		if (
			!this.firstTick&&
					this.worldObj!=null
		) {
			this.firstTick = true;
			scanMultiblock();
		}
	}

	@Override
	public void update() {
	}
}
