package com.zerofall.ezstorage.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeMessage implements IMessage {
	NBTTagCompound recipe;

	public RecipeMessage() {
	}

	public RecipeMessage(NBTTagCompound recipe) {
		this.recipe = recipe;
	}

	public void fromBytes(ByteBuf buf) {
		this.recipe = ByteBufUtils.readTag(buf);
	}

	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, this.recipe);
	}
}
