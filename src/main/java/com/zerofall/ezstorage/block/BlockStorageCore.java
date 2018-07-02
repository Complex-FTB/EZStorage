package com.zerofall.ezstorage.block;

import com.zerofall.ezstorage.EZStorage;
import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockStorageCore extends EZBlockContainer {
	public BlockStorageCore() {
		super("storage_core", Material.wood);
	}

	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityStorageCore();
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		TileEntityStorageCore tileEntity = (TileEntityStorageCore) world.getTileEntity(x, y, z);
		if (tileEntity.inventory.getTotalCount()>0L) {
			super.breakBlock(world, x, y, z, block, metadata);
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if (!world.isRemote) {
			TileEntityStorageCore tileEntity = (TileEntityStorageCore) world.getTileEntity(x, y, z);
			if (tileEntity.hasCraftBox) {
				player.openGui(EZStorage.instance, 2, world, x, y, z);
			} else {
				player.openGui(EZStorage.instance, 1, world, x, y, z);
			}
		}

		return true;
	}
}
