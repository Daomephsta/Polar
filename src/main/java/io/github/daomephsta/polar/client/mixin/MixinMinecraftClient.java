package io.github.daomephsta.polar.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.daomephsta.polar.client.MinecraftClientInitCallback;
import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient
{
	@Inject(method = "net/minecraft/client/MinecraftClient.init()V", at = @At("RETURN"))
	private void polar_onInit(CallbackInfo info)
	{
		MinecraftClientInitCallback.EVENT.invoker().onInitClient((MinecraftClient) (Object) this);
	}
}
