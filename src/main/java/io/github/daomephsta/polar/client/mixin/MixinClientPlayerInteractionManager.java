package io.github.daomephsta.polar.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.daomephsta.polar.common.callbacks.PlayerBreakBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager
{    
    @Shadow
    private @Final MinecraftClient client;
    
    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "net/minecraft/block/Block.onBreak"), cancellable = true)
    public void polar_tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info)
    {
        if (!PlayerBreakBlockCallback.EVENT.invoker().preBreakBlock(client.world, pos, client.player))
            info.setReturnValue(false);
    }
}