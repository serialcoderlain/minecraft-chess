package com.mycompanioncube.chess;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class ChessTileEntity extends TileEntity {
	private int[][] board = new int[8][8];
	private int lastMoveX;
	private int lastMove2X;
	private int lastMoveY;
	private int lastMove2Y;

	@Override
	public void writeToNBT(NBTTagCompound par1) {
		super.writeToNBT(par1);
		writeSyncableDataToNBT(par1);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1) {
		super.readFromNBT(par1);
		readSyncableDataFromNBT(par1);
	}

	public void setBoard(int[][] board1, int lastMoveX, int lastMoveY, int lastMove2X, int lastMove2Y) {
		this.board = board1;
		this.lastMoveX = lastMoveX;
		this.lastMoveY = lastMoveY;
		this.lastMove2X = lastMove2X;
		this.lastMove2Y = lastMove2Y;
	}

	public int[][] getBoard() {
		return board;
	}

	public int getLastMoveX() {
		return lastMoveX;
	}

	public int getLastMoveY() {
		return lastMoveY;
	}

	public int getLastMove2X() {
		return lastMove2X;
	}

	public int getLastMove2Y() {
		return lastMove2Y;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readSyncableDataFromNBT(pkt.getNbtCompound());
	}

	private void readSyncableDataFromNBT(NBTTagCompound par1) {
		if (par1.hasKey("board")) {
			byte[] data = par1.getByteArray("board");

			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					board[x][y] = (data[(x * 8) + y] == 255 ? -1 : data[(x * 8) + y]);
				}
			}
		}

		if (par1.hasKey("last")) {
			byte[] data = par1.getByteArray("last");

			lastMoveX = (data[0] == 255 ? -1 : data[0]);
			lastMoveY = (data[1] == 255 ? -1 : data[1]);
			lastMove2X = (data[2] == 255 ? -1 : data[2]);
			lastMove2Y = (data[3] == 255 ? -1 : data[3]);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncableDataToNBT(syncData);
		return new S35PacketUpdateTileEntity(this.pos, 1, syncData);
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		return super.receiveClientEvent(id, type);
	}

	private void writeSyncableDataToNBT(NBTTagCompound par1) {
		byte[] data = new byte[8 * 8];

		if (board != null) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					data[(x * 8) + y] = (byte) (board[x][y] == -1 ? 255 : board[x][y]);
				}
			}
			par1.setByteArray("board", data);

			byte[] data2 = new byte[4];

			data2[0] = (byte) (lastMoveX == -1 ? 255 : lastMoveX);
			data2[1] = (byte) (lastMoveY == -1 ? 255 : lastMoveY);
			data2[2] = (byte) (lastMove2X == -1 ? 255 : lastMove2X);
			data2[3] = (byte) (lastMove2Y == -1 ? 255 : lastMove2Y);
			par1.setByteArray("last", data2);
		}

	}

}