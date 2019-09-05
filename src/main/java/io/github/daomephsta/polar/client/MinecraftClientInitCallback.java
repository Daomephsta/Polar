package io.github.daomephsta.polar.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public interface MinecraftClientInitCallback
{
	public static final Event<MinecraftClientInitCallback> EVENT = EventFactory.createArrayBacked(MinecraftClientInitCallback.class, 
			callbacks -> client ->
			{
				for (MinecraftClientInitCallback callback : callbacks)
				{
					callback.onInitClient(client);
				}
			});
	
	public void onInitClient(MinecraftClient client);
}
