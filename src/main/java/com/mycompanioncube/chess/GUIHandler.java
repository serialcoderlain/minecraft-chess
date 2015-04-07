package com.mycompanioncube.chess;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
	private static final int CHESS_GUI = 30;

	public static int getGuiID() {
		return CHESS_GUI;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case CHESS_GUI:
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof ChessTileEntity) {
				return new ContainerChessTile((ChessTileEntity) tileEntity);
			}
		default:
			return null;
		}
		
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case CHESS_GUI:
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof ChessTileEntity) {
				return new ChessGUI((ChessTileEntity) tileEntity);
			}
		default:
			return null;
		}
	}
}