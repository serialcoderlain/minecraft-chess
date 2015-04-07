package com.mycompanioncube.chess;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Chess.MODID, name = "Chess", version = Chess.VERSION)
public class Chess {

	// Basics
	public static final String MODID = "chess";
	public static final String VERSION = "0.0.1";

	@Instance(value = "chess")
	public static Chess instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.mycompanioncube.chess.client.ClientProxy", serverSide = "com.mycompanioncube.chess.CommonProxy")
	public static CommonProxy proxy;

	// Chess board block
	public final static Block chessBoard = new ChessBoard(Material.wood);

	public final static GUIHandler handler = new GUIHandler();
	public SimpleNetworkWrapper net = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerBlock(chessBoard, "blockChessBoard");
		chessBoard.setHarvestLevel("axe", 0);
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();

		GameRegistry.registerTileEntity(com.mycompanioncube.chess.ChessTileEntity.class, "chessTileEntity");

		GameRegistry.addShapedRecipe(new ItemStack(chessBoard, 1), new Object[] { "ABA", "BAB", "CCC", 'A', new ItemStack(Blocks.obsidian, 1, 0), 'B',
				new ItemStack(Blocks.quartz_block, 1, 15), 'C', new ItemStack(Blocks.planks, 1, 1) });

		net.registerMessage(ChessUpdateMessage.class, ChessUpdateMessage.class, 0, Side.SERVER);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}