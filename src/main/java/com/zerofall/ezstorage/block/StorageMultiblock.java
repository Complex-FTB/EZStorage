package com.zerofall.ezstorage.block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;
import com.zerofall.ezstorage.util.BlockRef;
import com.zerofall.ezstorage.util.EZStorageUtils;

import net.minecraft.block.material.Material;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class StorageMultiblock
		extends EZBlock {
	protected StorageMultiblock(final String name, final Material material) {
		super(name, material);
	}

	@Override
	public void onBlockDestroyedByPlayer(final World worldIn, final int x, final int y, final int z, final int metadata) {
		super.onBlockDestroyedByPlayer(worldIn, x, y, z, metadata);
		attemptMultiblock(worldIn, x, y, z);
	}

	@Override
	public void onBlockDestroyedByExplosion(final World worldIn, final int x, final int y, final int z, final Explosion explosionIn) {
		super.onBlockDestroyedByExplosion(worldIn, x, y, z, explosionIn);
		attemptMultiblock(worldIn, x, y, z);
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		super.onBlockAdded(world, x, y, z);
		attemptMultiblock(world, x, y, z);
	}

	public void attemptMultiblock(final World world, final int x, final int y, final int z) {
		if (!world.isRemote)
			if (!(this instanceof BlockStorageCore)) {
				final BlockRef br = new BlockRef(this, x, y, z);
				final TileEntityStorageCore core = findCore(br, world, null);
				if (core!=null)
					core.scanMultiblock();
			} else {
				TileEntityStorageCore core = (TileEntityStorageCore) world.getTileEntity(x, y, z);
				if (core==null) {
					final BlockRef br = new BlockRef(this, x, y, z);
					core = findCore(br, world, null);
				}
				if (core!=null)
					core.scanMultiblock();
			}
	}

	public TileEntityStorageCore findCore(final BlockRef br, final World world, Set<BlockRef> scanned) {
		if (scanned==null)
			scanned = new HashSet<BlockRef>();
		final List<BlockRef> neighbors = EZStorageUtils.getNeighbors(br.pos.x, br.pos.y, br.pos.z, world);
		for (final BlockRef blockRef : neighbors)
			if (blockRef.block instanceof StorageMultiblock) {
				if (blockRef.block instanceof BlockStorageCore)
					return (TileEntityStorageCore) world.getTileEntity(blockRef.pos.x, blockRef.pos.y, blockRef.pos.z);
				if (scanned.add(blockRef)==true) {
					final TileEntityStorageCore entity = findCore(blockRef, world, scanned);
					if (entity!=null)
						return entity;
				}
			}

		return null;
	}
}
