package com.zerofall.ezstorage.gui;

import com.zerofall.ezstorage.container.ContainerStorageCore;
import com.zerofall.ezstorage.container.ContainerStorageCoreCrafting;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler
		implements IGuiHandler {
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity!=null) {
			if (ID==1)
				return new ContainerStorageCore(player, world, x, y, z);
			if (ID==2) {
				return new ContainerStorageCoreCrafting(player, world, x, y, z);
			}
		}
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity!=null) {
			if (ID==1)
				return new GuiStorageCore(player, world, x, y, z);
			if (ID==2) {
				return new GuiCraftingCore(player, world, x, y, z);
			}
		}
		return null;
	}
}
