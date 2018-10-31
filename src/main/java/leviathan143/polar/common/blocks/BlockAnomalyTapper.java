package leviathan143.polar.common.blocks;

import java.util.Random;

import daomephsta.umbra.bitmanipulation.IBitEncoderDecoder;
import leviathan143.polar.api.CommonWords;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.items.itemblocks.ItemBlockAnomalyTapper;
import leviathan143.polar.common.tileentities.TileEntityAnomalyTapper;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnomalyTapper extends BlockDirectional implements IHasSpecialItemBlock
{
	public static final IProperty<Polarity> POLARITY = PropertyEnum.create(CommonWords.POLARITY, Polarity.class, Polarity.POLARISED);
	
	public BlockAnomalyTapper()
	{
		super(Material.WOOD);
		setDefaultState(blockState.getBaseState()
			.withProperty(POLARITY, Polarity.RED)
			.withProperty(FACING, EnumFacing.UP));
		setCreativeTab(CreativeTabs.MISC);
		setHardness(2.0F);
		setResistance(5.0F);
		setSoundType(SoundType.WOOD);
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		TileEntityAnomalyTapper tapper = (TileEntityAnomalyTapper) worldIn.getTileEntity(pos);
		//The TE can be null at this time
		if(tapper != null && tapper.getAttachedAnomaly() != null)
		{
			double travelTime = 25.0D;
			for(int p = 0; p < 8; p++)
			{
				worldIn.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 
					(tapper.getAttachedAnomaly().posX - pos.getX() - 0.5D) / travelTime, 
					(tapper.getAttachedAnomaly().posY - pos.getY()) / travelTime, 
					(tapper.getAttachedAnomaly().posZ - pos.getZ() - 0.5D) / travelTime);
			}
		}
	}
	
	//Direction in the first 3 bits, polarity in the last bit
	@Override
	public int getMetaFromState(IBlockState state)
	{	
		IBitEncoderDecoder bitEncoderDecoder = IBitEncoderDecoder.fixedBitCount(4);
		bitEncoderDecoder.encode(0, 3, state.getValue(FACING).getIndex());
		bitEncoderDecoder.encode(3, state.getValue(POLARITY).getPolarisedIndex());
		return bitEncoderDecoder.decode();
	}

	//Direction in the first 3 bits, polarity in the last bit
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBitEncoderDecoder bitEncoderDecoder = IBitEncoderDecoder.fixedBitCount(4);
		bitEncoderDecoder.encode(meta);
		return getDefaultState()
			.withProperty(FACING, EnumFacing.byIndex(bitEncoderDecoder.decode(0, 3)))
			.withProperty(POLARITY, Polarity.fromPolarisedIndex(bitEncoderDecoder.decode(3)));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, POLARITY);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState()
			.withProperty(FACING, facing)
			.withProperty(POLARITY, getPolarity(placer.getHeldItem(hand)));
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return ItemBlockAnomalyTapper.forPolarity(state.getValue(POLARITY));
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.add(ItemBlockAnomalyTapper.forPolarity(state.getValue(POLARITY)));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityAnomalyTapper();
	}

	@Override
	public ItemBlock createItemBlock()
	{
		return new ItemBlockAnomalyTapper(this);
	}
	
	private Polarity getPolarity(ItemStack itemBlockStack)
	{
		return Polarity.valueOf(itemBlockStack.getTagCompound().getString(CommonWords.POLARITY));
	}
}
