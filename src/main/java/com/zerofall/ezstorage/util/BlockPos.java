package com.zerofall.ezstorage.util;

public class BlockPos {
	public int x;
	public int y;
	public int z;

	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31*result+this.x;
		result = 31*result+this.y;
		result = 31*result+this.z;
		return result;
	}

	public boolean equals(Object obj) {
		if (this==obj)
			return true;
		if (obj==null)
			return false;
		if (getClass()!=obj.getClass())
			return false;
		BlockPos other = (BlockPos) obj;
		if (this.x!=other.x)
			return false;
		if (this.y!=other.y)
			return false;
		if (this.z!=other.z)
			return false;
		return true;
	}
}
