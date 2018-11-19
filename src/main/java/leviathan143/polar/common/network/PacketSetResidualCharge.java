package leviathan143.polar.common.network;

import daomephsta.umbra.network.SchedulingMessageHandler;
import io.netty.buffer.ByteBuf;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSetResidualCharge implements IMessage
{
	private Polarity polarity;
	
	public PacketSetResidualCharge(Polarity polarity)
	{
		this.polarity = polarity;
	}
	
	// Required for deserialisation
	public PacketSetResidualCharge() {}

	public static class Handler extends SchedulingMessageHandler<PacketSetResidualCharge>
	{
		@Override
		protected void processMessage(PacketSetResidualCharge message, MessageContext ctx)
		{	
			PlayerDataPolar.get(FMLClientHandler.instance().getClientPlayerEntity()).setResidualPolarity(message.polarity);
		}	
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.polarity = Polarity.fromIndex(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(polarity.getIndex());
	}
}
