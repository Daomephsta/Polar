package io.github.daomephsta.polar.common.blocks;

import java.util.Collection;
import java.util.Locale;

import com.google.common.collect.ImmutableSet;

import io.github.daomephsta.polar.common.items.itemblocks.ItemBlockRune;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

//TODO Flatten
public class BlockRune extends Block implements IHasSpecialBlockItem
{
	public static enum Variant implements StringIdentifiable
	{
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
		public String asString()
		{
			return name().toLowerCase(Locale.ROOT);
		}
		
		public int getIndex()
		{
			return index;
		}
	}
	
	public static final EnumProperty<Variant> VARIANT = EnumProperty.of("variant", Variant.class);
	
	public BlockRune()
	{
		super(FabricBlockSettings.of(Material.STONE).build());
	}

	@Override
	public BlockItem createBlockItem()
	{
		return new ItemBlockRune(this);
	}
	
	@Override
	protected void appendProperties(
			Builder<Block, BlockState> stateFactory$Builder_1)
	{
		stateFactory$Builder_1.add(VARIANT);
	}
}
