package com.mycompanioncube.chess;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityEvent.CanUpdate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.Unpooled;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ChessGUI extends GuiScreen {
	private static final ResourceLocation texture = new ResourceLocation(Chess.MODID, "textures/gui/chessBoard.png");
	private ChessTileEntity tileEntity;
	private int xSize;
	private int ySize;
	private int clickedTileX = -1;
	private int clickedTileY = -1;

	private int[][] board = new int[8][8];
	private int clickedPiece;
	private int sideClicked = -1;

	private int lastMoveX = -1;
	private int lastMoveY = -1;
	private int lastMove2X = -1;
	private int lastMove2Y = -1;

	public ChessGUI(ChessTileEntity tileEntity) {
		super();

		xSize = 196;
		ySize = 166;

		board = tileEntity.getBoard();

		this.tileEntity = tileEntity;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		drawDefaultBackground();
		GlStateManager.pushMatrix();

		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		{
			int x = ((this.width) - xSize) / 2;
			int y = ((this.height) - ySize) / 2;

			drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}
		GlStateManager.scale(0.5f, 0.5f, 0.5f);

		board = tileEntity.getBoard();
		lastMoveX = tileEntity.getLastMoveX();
		lastMoveY = tileEntity.getLastMoveY();
		lastMove2X = tileEntity.getLastMove2X();
		lastMove2Y = tileEntity.getLastMove2Y();

		int x = ((this.width - xSize) / 2) + 16;
		int y = ((this.height - ySize) / 2);

		int xTile = getXpos(mouseX);
		int yTile = getYpos(mouseY);
		int TILE_SIZE = 16;
		int TILE_SIZEx2 = 32;
		int tilemapX = 180;

		x = ((this.width * 2) - (xSize * 2) + 64) / 2;
		y = ((this.height * 2) - (ySize * 2) - 1) / 2;

		{
			int xTilePos = (x + (lastMoveX * TILE_SIZEx2));
			int yTilePos = (y + (lastMoveY * TILE_SIZEx2));
			if (lastMoveX != -1 && lastMoveY != -1)
				drawRect(xTilePos, yTilePos, xTilePos + TILE_SIZEx2, yTilePos + TILE_SIZEx2, Color.yellow.getRGB());
		}

		{
			int xTilePos = (x + (clickedTileX * TILE_SIZEx2));
			int yTilePos = (y + (clickedTileY * TILE_SIZEx2));
			if (clickedTileX != -1 && clickedTileY != -1)
				drawRect(xTilePos, yTilePos, xTilePos + TILE_SIZEx2, yTilePos + TILE_SIZEx2, Color.green.getRGB());
		}

		{
			int xTilePos = (x + (lastMove2X * TILE_SIZEx2));
			int yTilePos = (y + (lastMove2Y * TILE_SIZEx2));
			if (lastMove2X != -1 && lastMove2Y != -1)
				drawRect(xTilePos, yTilePos, xTilePos + TILE_SIZEx2, yTilePos + TILE_SIZEx2, Color.yellow.getRGB());
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		{
			int xTilePos = (x + (xTile * TILE_SIZEx2));
			int yTilePos = (y + (yTile * TILE_SIZEx2));

			if (xTile != -1 && yTile != -1) {
				//drawRect(xTilePos, yTilePos, xTilePos + TILE_SIZEx2, yTilePos + TILE_SIZEx2, Color.red.getRGB());
				drawTexturedModalRect(xTilePos, yTilePos, 6 * TILE_SIZEx2, tilemapX, TILE_SIZEx2, TILE_SIZEx2);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				if (clickedTileX != -1 && clickedTileY != -1)
					drawTexturedModalRect(xTilePos, yTilePos, (clickedPiece % 6) * TILE_SIZEx2, tilemapX + ((clickedPiece) > 5 ? TILE_SIZEx2 : 0), TILE_SIZEx2, TILE_SIZEx2);

				if (sideClicked != -1)
					drawTexturedModalRect(xTilePos, yTilePos, (sideClicked % 6) * TILE_SIZEx2, tilemapX + ((sideClicked) > 5 ? TILE_SIZEx2 : 0), TILE_SIZEx2, TILE_SIZEx2);
			}
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			for (int x1 = 0; x1 < 8; x1++) {
				for (int y1 = 0; y1 < 8; y1++) {
					if (board[x1][y1] > -1) {
						int xTilePos = (x + ((x1 + 1) * TILE_SIZEx2));
						int yTilePos = (y + ((y1 + 1) * TILE_SIZEx2));

						drawTexturedModalRect(xTilePos, yTilePos, ((board[x1][y1]) % 6) * TILE_SIZEx2, tilemapX + ((board[x1][y1]) > 5 ? TILE_SIZEx2 : 0), TILE_SIZEx2, TILE_SIZEx2);
					}
				}
			}

			int highlight = getSidebarPiece(mouseX, mouseY);

			for (int y1 = PIECES.PAWN_W.ordinal(); y1 <= PIECES.KING_W.ordinal(); y1++) {
				int xTilePos = x - TILE_SIZEx2 + 16;
				int yTilePos = y + (y1 * TILE_SIZEx2) + 32;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				if (highlight == y1 || sideClicked == y1) {
					//drawRect(xTilePos, yTilePos, xTilePos + TILE_SIZEx2, yTilePos + TILE_SIZEx2, Color.green.getRGB());
					drawTexturedModalRect(xTilePos, yTilePos, 6 * TILE_SIZEx2, tilemapX, TILE_SIZEx2, TILE_SIZEx2);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				}
				
				drawTexturedModalRect(xTilePos, yTilePos, (y1 - PIECES.PAWN_W.ordinal()) * TILE_SIZEx2, tilemapX, TILE_SIZEx2, TILE_SIZEx2);
			}
			
			for (int y1 = PIECES.PAWN_B.ordinal(); y1 <= PIECES.KING_B.ordinal(); y1++) {
				int xTilePos = x - TILE_SIZEx2 + 16 + 320;
				int yTilePos = y + ((y1 - 6) * (TILE_SIZEx2)) + 32;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				if (highlight == y1 || sideClicked == y1) {
					//			drawRect(xTilePos, yTilePos, xTilePos + TILE_SIZEx2, yTilePos + TILE_SIZEx2, Color.green.getRGB());
					drawTexturedModalRect(xTilePos, yTilePos, 6 * TILE_SIZEx2, tilemapX, TILE_SIZEx2, TILE_SIZEx2);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				}

				drawTexturedModalRect(xTilePos, yTilePos, (y1 - PIECES.PAWN_B.ordinal()) * TILE_SIZEx2, tilemapX + TILE_SIZEx2, TILE_SIZEx2, TILE_SIZEx2);
			}
		}

		//		fontRendererObj.drawString("CHESS!", ((this.width - (xSize + fontRendererObj.getStringWidth("CHESS!"))) / 2) + (xSize / 2), y - 16, Color.white.getRGB());
		GlStateManager.popMatrix();
		GL11.glDisable(GL11.GL_BLEND);

	}

	int getXpos(int mouseX) {
		int TILE_SIZE = 16;
		int x = ((this.width - xSize) / 2) + 16;

		int xTile = (((mouseX - x)) / TILE_SIZE);

		if (xTile > 0 && xTile < 9) {
			return xTile;
		} else
			return -1;
	}

	int getYpos(int mouseY) {
		int TILE_SIZE = 16;
		int y = (this.height - ySize) / 2;

		int yTile = (((mouseY - y)) / TILE_SIZE);

		if (yTile > 0 && yTile < 9) {
			return yTile;
		} else
			return -1;
	}

	int getSidebarPiece(int mx, int my) {
		int x = ((this.width - xSize) / 2) + 16;
		int y = ((this.height - ySize) / 2);

		int TILE_SIZE = 16;
		int xPos = (mx - x) / TILE_SIZE;
		int yPos = (my - y) / TILE_SIZE;

		if (xPos == 0 && yPos >= 0 && yPos <= 6) {
			return (yPos - 1);
		}

		if ((xPos == 9 || xPos == 10) && yPos >= 0 && yPos <= 6) {
			return PIECES.KING_W.ordinal() + (yPos);
		}

		return -1;
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		tileEntity.getWorld().markBlockForUpdate(tileEntity.getPos());
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		clickedTileX = getXpos(mouseX);
		clickedTileY = getYpos(mouseY);

		if (clickedTileX != -1 && clickedTileY != -1) {
			clickedPiece = board[clickedTileX - 1][clickedTileY - 1];
		} else {
			if (sideClicked == -1)
				sideClicked = getSidebarPiece(mouseX, mouseY);
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	private boolean isSameColour(int piece, int piece2) {
		if (piece == -1 || piece2 == -1)
			return false;

		if (piece >= PIECES.PAWN_B.ordinal() && piece2 >= PIECES.PAWN_B.ordinal()) {
			return true;
		}

		if (piece < PIECES.PAWN_B.ordinal() && piece2 < PIECES.PAWN_B.ordinal()) {
			return true;
		}

		return false;
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		int tileX = getXpos(mouseX);
		int tileY = getYpos(mouseY);

		if (tileX != -1 && tileY != -1) {
			if (clickedTileX > -1 && clickedTileY > -1 && !(tileX == clickedTileX && tileY == clickedTileY)) {
				if (board[clickedTileX - 1][clickedTileY - 1] != -1) {
					if (!isSameColour(board[tileX - 1][tileY - 1], clickedPiece)) {
						board[tileX - 1][tileY - 1] = clickedPiece;
						board[clickedTileX - 1][clickedTileY - 1] = -1;

						lastMoveX = tileX;
						lastMoveY = tileY;

						lastMove2X = clickedTileX;
						lastMove2Y = clickedTileY;

						tileEntity.setBoard(board, lastMoveX, lastMoveY, lastMove2X, lastMove2Y);

						NBTTagCompound nbtTagCompound = new NBTTagCompound();
						tileEntity.writeToNBT(nbtTagCompound);
						Chess.instance.net.sendToServer(new ChessUpdateMessage("WOOOOOOOOOOOOO", tileEntity.getPos(), nbtTagCompound));
					}
				}
			}

			if (sideClicked != -1) {
				board[tileX - 1][tileY - 1] = sideClicked;
				tileEntity.setBoard(board, -1, -1, tileX, tileY);

				NBTTagCompound nbtTagCompound = new NBTTagCompound();
				tileEntity.writeToNBT(nbtTagCompound);
				Chess.instance.net.sendToServer(new ChessUpdateMessage("WOOOOOOOOOOOOO", tileEntity.getPos(), nbtTagCompound));
			}
		} else {
			if (clickedTileX > -1 && clickedTileY > -1 && !(tileX == clickedTileX && tileY == clickedTileY)) {
				board[clickedTileX - 1][clickedTileY - 1] = -1;
				tileEntity.setBoard(board, -1, -1, clickedTileX, clickedTileY);

				NBTTagCompound nbtTagCompound = new NBTTagCompound();
				tileEntity.writeToNBT(nbtTagCompound);
				Chess.instance.net.sendToServer(new ChessUpdateMessage("WOOOOOOOOOOOOO", tileEntity.getPos(), nbtTagCompound));
			}
		}
		clickedTileX = -1;
		clickedTileY = -1;
		sideClicked = -1;
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}
}