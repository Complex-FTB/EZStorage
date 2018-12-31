package com.zerofall.ezstorage.events;

import com.zerofall.ezstorage.tileentity.TileEntityStorageCore;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class XEventHandler {
	@cpw.mods.fml.common.eventhandler.SubscribeEvent
	public void onBlockBreak(final BreakEvent e) {
		if (!e.world.isRemote) {
			final net.minecraft.tileentity.TileEntity tileentity = e.world.getTileEntity(e.x, e.y, e.z);
			if (tileentity instanceof TileEntityStorageCore) {
				final TileEntityStorageCore core = (TileEntityStorageCore) tileentity;
				if (core.inventory.getTotalCount()>0L)
					e.setCanceled(true);
			}
		}
	}

	@cpw.mods.fml.common.eventhandler.SubscribeEvent
	public void onBlockBreak(final BlockEvent.PlaceEvent e) {
		if (!e.world.isRemote) {
			final net.minecraft.tileentity.TileEntity tileentity = e.world.getTileEntity(e.x, e.y, e.z);
			if (tileentity instanceof TileEntityStorageCore) {
				final TileEntityStorageCore core = (TileEntityStorageCore) tileentity;
				if (!core.validateSystem(null) && core.inventory.getTotalCount()==0L){
					e.setCanceled(true);
				}
			}
		}
	}
}
