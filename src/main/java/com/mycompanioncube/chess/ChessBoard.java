package com.mycompanioncube.chess;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChessBoard extends Block implements ITileEntityProvider {
	private final String name = "blockChessBoard";
	public static final PropertyEnum HALF = PropertyEnum.create("half", BlockSlab.EnumBlockHalf.class);

	protected ChessBoard(Material materialIn) {
		super(materialIn);
		setHardness(0.5F);
		setStepSound(Block.soundTypeWood);
		setUnlocalizedName(Chess.MODID + "." + name);
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.13F, 1.0F);
	}
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ChessTileEntity();
	}

	public String getName() {
		return name;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof ChessTileEntity) {
			((ChessTileEntity) tileEntity).setBoard(PIECES.emptyBoard(), -1, -1, -1, -1);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			playerIn.openGui(Chess.instance, GUIHandler.getGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

}