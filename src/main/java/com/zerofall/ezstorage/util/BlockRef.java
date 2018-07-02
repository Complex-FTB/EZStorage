package com.zerofall.ezstorage.util;

import net.minecraft.tileentity.TileEntity;

public class BlockRef {
	public BlockPos pos;
	public net.minecraft.block.Block block;

	public BlockRef(net.minecraft.block.Block block, int x, int y, int z) {
		this.block = block;
		this.pos = new BlockPos(x, y, z);
	}

	public BlockRef(TileEntity entity) {
		this.block = entity.getBlockType();
		this.pos = new BlockPos(entity.xCoord, entity.yCoord, entity.zCoord);
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31*result+(this.block==null ? 0 : this.block.hashCode());

		result = 31*result+(this.pos==null ? 0 : this.pos.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (getClass()!=obj.getClass())
			return false;
		BlockRef other = (BlockRef) obj;
		if (this.block==null) {
			if (other.block!=null)
				return false;
		} else if (!net.minecraft.block.Block.isEqualTo(this.block, other.block))
			return false;
		if (this.pos==null) {
			if (other.pos!=null)
				return false;
		} else if (!this.pos.equals(other.pos))
			return false;
		return true;
	}

	public String toString() {
		return "BlockRef [pos="+this.pos+", block="+this.block+"]";
	}
}
