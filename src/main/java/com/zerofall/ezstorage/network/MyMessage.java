package com.zerofall.ezstorage.network;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MyMessage implements cpw.mods.fml.common.network.simpleimpl.IMessage {
	public int index;
	public int button;
	public int mode;

	public MyMessage() {
	}

	public MyMessage(int index, int button, int mode) {
		this.index = index;
		this.button = button;
		this.mode = mode;
	}

	public void fromBytes(io.netty.buffer.ByteBuf buf) {
		this.index = ByteBufUtils.readVarInt(buf, 5);
		this.button = ByteBufUtils.readVarInt(buf, 5);
		this.mode = ByteBufUtils.readVarInt(buf, 5);
	}

	public void toBytes(io.netty.buffer.ByteBuf buf) {
		ByteBufUtils.writeVarInt(buf, this.index, 5);
		ByteBufUtils.writeVarInt(buf, this.button, 5);
		ByteBufUtils.writeVarInt(buf, this.mode, 5);
	}
}
