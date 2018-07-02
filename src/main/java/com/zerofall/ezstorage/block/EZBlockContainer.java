package com.zerofall.ezstorage.block;

import com.zerofall.ezstorage.EZStorage;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EZBlockContainer
		extends StorageMultiblock implements ITileEntityProvider {
	protected EZBlockContainer(String name, Material materialIn) {
		super(name, materialIn);
		setBlockName(name);
		setCreativeTab(EZStorage.instance.creativeTab);
		this.isBlockContainer = true;
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		super.breakBlock(world, x, y, z, block, metadata);
		world.removeTileEntity(x, y, z);
	}

	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventParam) {
		super.onBlockEventReceived(world, x, y, z, eventId, eventParam);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		return tileentity==null ? false : tileentity.receiveClientEvent(eventId, eventParam);
	}

	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}
}
