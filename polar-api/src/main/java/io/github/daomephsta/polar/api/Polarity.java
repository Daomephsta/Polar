package io.github.daomephsta.polar.api;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.StringIdentifiable;

/**
 * Represents the polarity of an object, i.e. which school of Polar magic it uses
 * @author Daomephsta
 */
public enum Polarity implements StringIdentifiable
{	
	NONE(0, PolarAPI.TAB_OTHER),
	RED(1, 0, PolarAPI.TAB_OTHER),
	BLUE(2, 1, PolarAPI.TAB_OTHER);
	
	/**An immutable list of all polarities except {@code NONE}**/
	public static final List<Polarity> POLARISED;
	private static final TrackedDataHandler<Polarity> DATA_SERIALIZER = new TrackedDataHandler<Polarity>()
	{
		@Override
		public void write(PacketByteBuf buf, Polarity value) 
		{
			buf.writeInt(value.getIndex());
		}

		@Override
		public Polarity read(PacketByteBuf buf) 
		{
			return Polarity.fromIndex(buf.readInt());
		}

		public TrackedData<Polarity> create(int id) 
		{
			return new TrackedData<>(id, this);
		}

		@Override
		public Polarity copy(Polarity value) 
		{
			return value;
		}
	}; 
	private static final Polarity[] IDX_TO_VALUE = new Polarity[values().length];
	static
	{
		Polarity[] polarised = new Polarity[2];
		for(Polarity p : values())
		{
			//Standard index
			if (IDX_TO_VALUE[p.getIndex()] != null)
				throw new IllegalArgumentException(
						String.format("Index collision between constants %s and %s", IDX_TO_VALUE[p.getIndex()], p));
			IDX_TO_VALUE[p.getIndex()] = p;
			
			//Polarised index
			if (p.getPolarisedIndex() >= 0)
			{
				if (polarised[p.getPolarisedIndex()] != null)
					throw new IllegalArgumentException(
							String.format("Polarised index collision between constants %s and %s", polarised[p.getPolarisedIndex()], p));
				polarised[p.getPolarisedIndex()] = p;
			}
		}
		POLARISED = ImmutableList.copyOf(polarised);
		TrackedDataHandlerRegistry.register(DATA_SERIALIZER);
	}
	
	private final int index;
	//Index for the subset of constants that are polarised(not NONE). NONE has a polarised index of -1.
	private final int polarisedIndex;
	private final ItemGroup itemGroup;
	private final String translationKey;
	
	private Polarity(int index, ItemGroup itemGroup)
	{
		this(index, -1, itemGroup);
	}
	
	private Polarity(int index, int polarisedIndex, ItemGroup itemGroup)
	{
		this.index = index;
		this.polarisedIndex = polarisedIndex;
		this.itemGroup = itemGroup;
		this.translationKey = PolarAPI.PROVIDER_MOD_ID + ".polarity." + asString();
	}
	
	/**
	 * @param index An integer in [0, 2] that represents a polarity.
	 * @return The enum constant represented by {@code index}.
	 */
	public static Polarity fromIndex(int index)
	{
		if(index < values().length) return IDX_TO_VALUE[index];
		throw new IllegalArgumentException("No constant exists with the index " + index);
	}
	
	/**
	 * @param polarisedIndex An integer in [0, 1] that represents a polarity 
	 * which is not {@code NONE}.
	 * @return The enum constant represented by {@code polarisedIndex}.
	 */
	public static Polarity fromPolarisedIndex(int polarisedIndex)
	{
		if(polarisedIndex < POLARISED.size()) return POLARISED.get(polarisedIndex);
		throw new IllegalArgumentException("No constant exists with the polarised index " + polarisedIndex);
	}
	
	/**
	 * @return A {@code TrackedDataHandler} for {@code Polarity} enum constants.
	 */
	public static TrackedDataHandler<Polarity> getTrackedDataHandler()
	{
		return DATA_SERIALIZER;
	}
	
	/** 
	 * @param stack ItemStack to determine the polarity of.
	 * @return The polarity of {@code stack}.
	 */
	public static Polarity ofStack(ItemStack stack)
	{
		if (stack.getItem() instanceof IPolarisedItem)
			return ((IPolarisedItem) stack.getItem()).getPolarity(stack);
		if (stack.hasTag() && stack.getTag().contains(PolarAPI.PROVIDER_MOD_ID))
		{
			NbtCompound polarTag = stack.getTag().getCompound(PolarAPI.PROVIDER_MOD_ID);
			return Enum.valueOf(Polarity.class, polarTag.getString(CommonWords.POLARITY));
		}
		return Polarity.NONE;
	}
	
	/**
	 * @param ItemStack to determine the existence of 
	 * a polarity for {@code stack}.
	 * @return True if {@code stack} has a polarity.
	 */
	public static boolean isStackPolarised(ItemStack stack)
	{
		if (stack.getItem() instanceof IPolarisedItem) return true;
		if (!stack.hasTag()) return false;
		return stack.getTag().contains(PolarAPI.PROVIDER_MOD_ID) 
				&& stack.getTag().getCompound(PolarAPI.PROVIDER_MOD_ID).contains(CommonWords.POLARITY);
	}
	
	@Override
	public String asString()
	{
		return name().toLowerCase(Locale.ROOT);
	}
	
	/**@return The standard item group for this polarity*/
	public ItemGroup getItemGroup()
	{
		return itemGroup;
	}
	
	/**
	 * @return A string key to be used for localising 
	 * the name of this polarity.
	 */
	public String getTranslationKey()
	{
		return translationKey;
	}
	
	/**
	 * @return An integer in [0, 2] that represents 
	 * this polarity.
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * @return An integer in [0, 1] that represents
	 * this polarity, or -1 if it is {@code NONE}.
	 */
	public int getPolarisedIndex()
	{
		return polarisedIndex;
	}
	
	/**
	 * @return True if this polarity isn't {@code NONE}.
	 */
	public boolean isPolarised()
	{
		return polarisedIndex > -1;
	}
}
