
package io.github.daomephsta.polar.common.handlers.research;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import dev.onyxstudios.cca.api.v3.entity.TrackingStartCallback;
import io.github.daomephsta.polar.common.advancements.triggers.PolarCriteria;
import io.github.daomephsta.polar.common.handlers.research.ObserveFallingBlockHandler.FallingBlockTracker.Status;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class ObserveFallingBlockHandler
{
	private static Map<World, Collection<FallingBlockTracker>> trackedFallingBlocks = new WeakHashMap<>();
	
	public static void initialise()
	{
		TrackingStartCallback.EVENT.register(ObserveFallingBlockHandler::trackFallingBlock);
		ServerTickEvents.END_WORLD_TICK.register(ObserveFallingBlockHandler::tick);
	}

	private static void trackFallingBlock(ServerPlayerEntity entityPlayer, Entity entity)
	{
		if (entity instanceof FallingBlockEntity)
		{
			FallingBlockEntity fallingBlock = (FallingBlockEntity) entity;;
			World world = fallingBlock .getEntityWorld();
			if (world.isClient)
				return;
			if (PolarCriteria.OBSERVE_FALLING_BLOCK.hasHandler(entityPlayer.getAdvancementTracker()))
				getTrackers(fallingBlock.getEntityWorld()).add(new FallingBlockTracker(entityPlayer, fallingBlock));
		}
	}

	private static Collection<FallingBlockTracker> getTrackers(World world)
	{
		return trackedFallingBlocks.computeIfAbsent(world,
				k -> Collections.newSetFromMap(new WeakHashMap<>()));
	}

	private static void tick(World world)
	{
		Collection<FallingBlockTracker> notFalling = getTrackers(world).stream()
				.filter(tracker -> tracker.getStatus() != Status.FALLING)
				.collect(Collectors.toSet());
		for (Iterator<FallingBlockTracker> iter = notFalling.iterator(); iter
				.hasNext();)
		{
			FallingBlockTracker tracker = iter.next();
			if (tracker.getStatus() == Status.HIT_GROUND
					&& tracker.fallingBlock.getFallingBlockPos().getSquaredDistance(tracker.fallingBlock.getPos(), false) >= 32 * 32)
			{
			    PolarCriteria.OBSERVE_FALLING_BLOCK.handle(tracker.entityPlayer);
			}
			iter.remove();
		}
	}

	static class FallingBlockTracker
	{
		private final ServerPlayerEntity entityPlayer;
		private final FallingBlockEntity fallingBlock;

		private FallingBlockTracker(ServerPlayerEntity entityPlayer, FallingBlockEntity fallingBlock)
		{
			this.entityPlayer = entityPlayer;
			this.fallingBlock = fallingBlock;
		}

		private Status getStatus()
		{
			if (!fallingBlock.isAlive())
			{
				if (fallingBlock.isOnGround())
					return Status.HIT_GROUND;
				else
					return Status.FALLING;
			}
			return Status.INVALID;
		}

		static enum Status
		{
			FALLING, HIT_GROUND, INVALID;
		}
	}
}
