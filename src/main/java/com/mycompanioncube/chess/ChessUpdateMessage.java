package com.mycompanioncube.chess;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ChessUpdateMessage implements IMessage, IMessageHandler<ChessUpdateMessage, IMessage> {

	private String text;
	private int x;
	private int y;
	private int z;
	private NBTTagCompound tags;

	public ChessUpdateMessage() {
	}

	public ChessUpdateMessage(String string, BlockPos blockPos, NBTTagCompound tags) {
		this.text = string;
		this.tags = tags;
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		text = ByteBufUtils.readUTF8String(buf);
		x = ByteBufUtils.readVarInt(buf, 5);
		y = ByteBufUtils.readVarInt(buf, 5);
		z = ByteBufUtils.readVarInt(buf, 5);
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, text);
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
		ByteBufUtils.writeTag(buf, tags);
	}

	@Override
	public IMessage onMessage(ChessUpdateMessage message, MessageContext ctx) {
		EntityPlayer thePlayer = null;
		if (ctx.side.isClient()) {
		} else {
			thePlayer = ctx.getServerHandler().playerEntity;
		}

		ChessTileEntity entity = (ChessTileEntity) thePlayer.worldObj.getTileEntity(new BlockPos(message.x, message.y, message.z));
		entity.readFromNBT(message.tags);
		entity.getWorld().markBlockForUpdate(new BlockPos(message.x, message.y, message.z));
		entity.markDirty();
		return null;
	}
}