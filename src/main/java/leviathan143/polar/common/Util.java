package leviathan143.polar.common;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.*;

public class Util
{
	public static final DataSerializer<Long> DATA_SERIALIZER_LONG = new DataSerializer<Long>()
	{
		@Override
		public void write(PacketBuffer buf, Long value) {buf.writeLong(value);}

		@Override
		public Long read(PacketBuffer buf) throws IOException {return buf.readLong();}

		@Override
		public DataParameter<Long> createKey(int id) {return new DataParameter<>(id, this);}

		@Override
		public Long copyValue(Long value) {return value;}
	};
	
	static
	{
		DataSerializers.registerSerializer(DATA_SERIALIZER_LONG);
	}
}
