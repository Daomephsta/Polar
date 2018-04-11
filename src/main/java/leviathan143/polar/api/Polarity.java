package leviathan143.polar.api;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.*;
import net.minecraft.util.IStringSerializable;

public enum Polarity implements IStringSerializable
{	
	NONE(0),
	RED(1),
	BLUE(2);
	
	private static final DataSerializer<Polarity> DATA_SERIALIZER = new DataSerializer<Polarity>()
	{
		@Override
		public void write(PacketBuffer buf, Polarity value) {buf.writeInt(value.getIndex());}

		@Override
		public Polarity read(PacketBuffer buf) throws IOException {return Polarity.fromIndex(buf.readInt());}

		@Override
		public DataParameter<Polarity> createKey(int id) {return new DataParameter<>(id, this);}

		@Override
		public Polarity copyValue(Polarity value) {return value;}
	}; 
	private static final Polarity[] IDX_TO_VALUE = new Polarity[values().length];
	static
	{
		for(Polarity p : values())
		{
			if (IDX_TO_VALUE[p.getIndex()] != null)
				throw new IllegalArgumentException(
						String.format("Index collision between constants %s and %s", IDX_TO_VALUE[p.getIndex()], p));
			IDX_TO_VALUE[p.getIndex()] = p;
		}
		DataSerializers.registerSerializer(DATA_SERIALIZER);
	}
	
	private final int index;
	
	private Polarity(int index)
	{
		this.index = index;
	}
	
	public static Polarity fromIndex(int index)
	{
		if(index < values().length) return IDX_TO_VALUE[index];
		throw new IllegalArgumentException("No constant exists with the index " + index);
	}
	
	public static DataSerializer<Polarity> getDataSerializer()
	{
		return DATA_SERIALIZER;
	}
	
	@Override
	public String getName()
	{
		return name();
	}
	
	public int getIndex()
	{
		return index;
	}
}
