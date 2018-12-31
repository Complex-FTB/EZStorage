package com.zerofall.ezstorage.block;

import com.zerofall.ezstorage.EZConfiguration;
import net.minecraft.block.material.Material;

public class BlockCondensedStorage extends BlockStorage {
	public BlockCondensedStorage() {
		super("condensed_storage_box", Material.iron);
	}

	public int getCapacity() {
		return EZConfiguration.condensedCapacity;
	}
}
