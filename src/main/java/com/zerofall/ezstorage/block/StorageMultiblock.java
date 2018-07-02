package com.zerofall.ezstorage.block;

import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;
import com.zerofall.ezstorage.util.BlockPos;
import com.zerofall.ezstorage.util.BlockRef;
import com.zerofall.ezstorage.util.EZStorageUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class StorageMultiblock
		extends EZBlock {
	protected StorageMultiblock(String name, Material material) {
		super(name, material);
	}

	public void onBlockDestroyedByPlayer(World worldIn, int x, int y, int z, int metadata) {
		super.onBlockDestroyedByPlayer(worldIn, x, y, z, metadata);
		attemptMultiblock(worldIn, x, y, z);
	}

	public void onBlockDestroyedByExplosion(World worldIn, int x, int y, int z, Explosion explosionIn) {
		super.onBlockDestroyedByExplosion(worldIn, x, y, z, explosionIn);
		attemptMultiblock(worldIn, x, y, z);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		attemptMultiblock(world, x, y, z);
	}

	public void attemptMultiblock(World world, int x, int y, int z) {
		if (!world.isRemote) {
			if (!(this instanceof BlockStorageCore)) {
				BlockRef br = new BlockRef(this, x, y, z);
				TileEntityStorageCore core = findCore(br, world, null);
				if (core!=null) {
					core.scanMultiblock();
				}
			} else {
				TileEntityStorageCore core = (TileEntityStorageCore) world.getTileEntity(x, y, z);
				if (core==null) {
					BlockRef br = new BlockRef(this, x, y, z);
					core = findCore(br, world, null);
				}
				if (core!=null) {
					core.scanMultiblock();
				}
			}
		}
	}

	public TileEntityStorageCore findCore(BlockRef br, World world, Set<BlockRef> scanned) {
		if (scanned==null) {
			scanned = new HashSet();
		}
		List<BlockRef> neighbors = EZStorageUtils.getNeighbors(br.pos.x, br.pos.y, br.pos.z, world);
		for (BlockRef blockRef : neighbors) {
			if ((blockRef.block instanceof StorageMultiblock)) {
				if ((blockRef.block instanceof BlockStorageCore)) {
					return (TileEntityStorageCore) world.getTileEntity(blockRef.pos.x, blockRef.pos.y, blockRef.pos.z);
				}
				if (scanned.add(blockRef)==true) {
					TileEntityStorageCore entity = findCore(blockRef, world, scanned);
					if (entity!=null) {
						return entity;
					}
				}
			}
		}

		return null;
	}
}
