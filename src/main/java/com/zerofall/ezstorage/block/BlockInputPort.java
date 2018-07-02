package com.zerofall.ezstorage.block;

import com.zerofall.ezstorage.tileentity.TileEntityInputPort;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInputPort extends EZBlockContainer {
	public BlockInputPort() {
		super("input_port", Material.iron);
	}

	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityInputPort();
	}
}
