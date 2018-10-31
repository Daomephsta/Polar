package leviathan143.polar.common.blocks;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSet;

import leviathan143.polar.client.ISpecialRender;
import leviathan143.polar.common.items.ItemBlockRune;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockRune extends Block implements IHasSpecialItemBlock, ISpecialRender
{
	public static enum Variant implements IStringSerializable
	{
		//Blue
		RAL(0),
		SAI(1),
		DEL(2),
		PER(3),
		SEI(4),
		NEI(5),
		//RED
		QER(6),
		DER(7),
		NIM(8),
		MIR(9),
		JO(10),
		TIR(11);
		
		public static final Collection<Variant> 
			BLUE = ImmutableSet.of(RAL, SAI, DEL, PER, SEI, NEI),
			RED = ImmutableSet.of(QER, DER, NIM, MIR, JO, TIR);
		private static final Variant[] IDX_TO_VALUE = new Variant[values().length];
		static
		{
			for(Variant v : values())
			{
				//Standard index
				if (IDX_TO_VALUE[v.getIndex()] != null)
					throw new IllegalArgumentException(
							String.format("Index collision between constants %s and %s", IDX_TO_VALUE[v.getIndex()], v));
				IDX_TO_VALUE[v.getIndex()] = v;
			}
		}
		
		private final int index;
		
		private Variant(int index)
		{
			this.index = index;
		}

		public static Variant fromIndex(int index)
		{
			if(index < values().length) return IDX_TO_VALUE[index];
			throw new IllegalArgumentException("No constant exists with the index " + index);
		}
		
		@Override
		public String getName()
		{
			return name().toLowerCase(Locale.ROOT);
		}
		
		public int getIndex()
		{
			return index;
		}
	}
	
	public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
	
	public BlockRune()
	{
		super(Material.ROCK);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		ItemStack held = placer.getHeldItem(hand);
		return getDefaultState()
			.withProperty(VARIANT, ItemBlockRune.getVariant(held));
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return ItemBlockRune.forVariant(state.getValue(VARIANT));
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		drops.add(ItemBlockRune.forVariant(state.getValue(VARIANT)));
	}
	
	@Override
	public void registerRender()
	{
		//Create blockstate variant strings
		Map<Variant, ModelResourceLocation> variantLocations = Arrays.stream(Variant.values()).collect(Collectors.toMap(Functions.identity(), v -> new ModelResourceLocation(getRegistryName(), VARIANT.getName() + "=" + v.getName())));
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), stack -> variantLocations.get(ItemBlockRune.getVariant(stack)));
		ModelLoader.registerItemVariants(Item.getItemFromBlock(this), variantLocations.values().toArray(new ModelResourceLocation[0]));
	}

	@Override
	public ItemBlock createItemBlock()
	{
		return new ItemBlockRune(this);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT);
	}
	
	//Variant in the first 3 bits, polarity in the last bit
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANT, Variant.fromIndex(meta));
	}
	
	//Variant in the first 3 bits, polarity in the last bit
	@Override
	public int getMetaFromState(IBlockState state)
	{ 
		return state.getValue(VARIANT).getIndex();
	}
}
