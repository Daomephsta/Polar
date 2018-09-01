package leviathan143.polar.common.blocks;

import java.util.Random;

import leviathan143.polar.api.CommonWords;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.client.ISpecialRender;
import leviathan143.polar.common.items.IPolarisedItem;
import leviathan143.polar.common.items.ItemBlockAnomalyTapper;
import leviathan143.polar.common.tileentities.TileEntityAnomalyTapper;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockAnomalyTapper extends BlockDirectional implements IHasSpecialItemBlock, ISpecialRender
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
	
	@Override
	public int getMetaFromState(IBlockState state)
	{	
		int meta = state.getValue(FACING).getIndex();
		meta |= state.getValue(POLARITY).getPolarisedIndex() << 3;
		return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState();
		//ANDing with 0b1110 ensures we're only dealing with the first 3 bits
		state.withProperty(FACING, EnumFacing.getFront(meta & 0b0111));
		/* ANDing with 0b0001 ensures we're only dealing with the last bit.
		 * The right shift shifts the polarity from the last bit to the actual number */
		state.withProperty(POLARITY, Polarity.fromPolarisedIndex((meta & 0b1000) >> 3));
		return state;
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
		return IPolarisedItem.getPolarisedStack(BlockRegistry.ANOMALY_TAPPER, state.getValue(POLARITY));
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.add(IPolarisedItem.getPolarisedStack(BlockRegistry.ANOMALY_TAPPER, state.getValue(POLARITY)));
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
	
	@Override
	public void registerRender()
	{
		//Create blockstate variant strings
		ModelResourceLocation redTapper = new ModelResourceLocation(getRegistryName(), String.format("%s=%s,%s=%s", BlockAnomalyTapper.FACING.getName(), EnumFacing.UP.getName(), BlockAnomalyTapper.POLARITY.getName(), Polarity.RED.getName())),
			blueTapper = new ModelResourceLocation(getRegistryName(), String.format("%s=%s,%s=%s", BlockAnomalyTapper.FACING.getName(), EnumFacing.UP.getName(), BlockAnomalyTapper.POLARITY.getName(), Polarity.BLUE.getName()));
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), stack -> getPolarity(stack) == Polarity.RED ? redTapper : blueTapper);
		ModelLoader.registerItemVariants(Item.getItemFromBlock(this), redTapper, blueTapper);
	}
	
	private Polarity getPolarity(ItemStack itemBlockStack)
	{
		return Polarity.valueOf(itemBlockStack.getTagCompound().getString(CommonWords.POLARITY));
	}
}
