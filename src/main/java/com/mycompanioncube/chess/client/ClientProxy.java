package com.mycompanioncube.chess.client;

import com.mycompanioncube.chess.Chess;
import com.mycompanioncube.chess.ChessBoard;
import com.mycompanioncube.chess.CommonProxy;
import com.mycompanioncube.chess.GUIHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Chess.instance.chessBoard), 0,
				new ModelResourceLocation(Chess.MODID + ":" + ((ChessBoard) Chess.instance.chessBoard).getName(), "inventory"));

		// blocks
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(Chess.instance.chessBoard), 0,
				new ModelResourceLocation(Chess.MODID + ":" + ((ChessBoard) Chess.instance.chessBoard).getName(), "inventory"));
		
		
		// GUI
		NetworkRegistry.INSTANCE.registerGuiHandler(Chess.instance, new GUIHandler());
	}

	
}