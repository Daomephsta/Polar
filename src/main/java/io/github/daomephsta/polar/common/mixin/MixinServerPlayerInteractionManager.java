package io.github.daomephsta.polar.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.daomephsta.polar.common.callbacks.PlayerBreakBlockCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager
{
    @Shadow
    public ServerWorld world;
    @Shadow
    public ServerPlayerEntity player;
    
    @Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "net/minecraft/block/Block.onBreak"), cancellable = true)
    public void polar_tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info)
    {
        if (!PlayerBreakBlockCallback.EVENT.invoker().preBreakBlock(world, pos, player))
            info.setReturnValue(false);
    }
}
