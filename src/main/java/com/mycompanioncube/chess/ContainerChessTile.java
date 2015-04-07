package com.mycompanioncube.chess;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerChessTile extends Container {
	private ChessTileEntity tile;

	public ContainerChessTile(ChessTileEntity te) {
		tile = te;
	}

	public ChessTileEntity getTile() {
		return tile;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}