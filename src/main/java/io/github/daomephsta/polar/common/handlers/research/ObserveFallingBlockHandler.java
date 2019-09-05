/*TODO Observer Falling Block Handler
 * package io.github.daomephsta.polar.common.handlers.research;
 * 
 * import java.util.*; import java.util.stream.Collectors;
 * 
 * import
 * io.github.daomephsta.polar.common.advancements.triggers.TriggerRegistry;
 * import io.github.daomephsta.polar.common.handlers.research.
 * ObserveFallingBlockHandler.FallingBlockTracker.Status; import
 * net.minecraft.entity.item.EntityFallingBlock; import
 * net.minecraft.entity.player.EntityPlayerMP; import net.minecraft.world.World;
 * import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
 * 
 * public class ObserveFallingBlockHandler { private static Map<World,
 * Collection<FallingBlockTracker>> trackedFallingBlocks = new WeakHashMap<>();
 * 
 * public static void trackFallingBlock(EntityFallingBlock fallingBlock,
 * EntityPlayerMP entityPlayer) { World world = fallingBlock.getEntityWorld();
 * if (world.isRemote) return; if
 * (TriggerRegistry.OBSERVE_FALLING_BLOCK.hasListeners(entityPlayer.
 * getAdvancements())) getTrackers(fallingBlock.getEntityWorld()).add(new
 * FallingBlockTracker(entityPlayer, fallingBlock)); }
 * 
 * public static Collection<FallingBlockTracker> getTrackers(World world) {
 * return trackedFallingBlocks.computeIfAbsent(world, k ->
 * Collections.newSetFromMap(new WeakHashMap<>())); }
 * 
 * public static void tick(World world, Phase phase) { if (phase == Phase.END)
 * return; Collection<FallingBlockTracker> notFalling =
 * getTrackers(world).stream() .filter(tracker -> tracker.getStatus() !=
 * Status.FALLING) .collect(Collectors.toSet()); for
 * (Iterator<FallingBlockTracker> iter = notFalling.iterator(); iter.hasNext();)
 * { FallingBlockTracker tracker = iter.next(); if (tracker.getStatus() ==
 * Status.HIT_GROUND &&
 * tracker.fallingBlock.getOrigin().distanceSq(tracker.fallingBlock.getPosition(
 * )) >= 32 * 32) {
 * TriggerRegistry.OBSERVE_FALLING_BLOCK.trigger(tracker.entityPlayer); }
 * iter.remove(); } }
 * 
 * public static class FallingBlockTracker { private final EntityPlayerMP
 * entityPlayer; private final EntityFallingBlock fallingBlock;
 * 
 * public FallingBlockTracker(EntityPlayerMP entityPlayer, EntityFallingBlock
 * fallingBlock) { this.entityPlayer = entityPlayer; this.fallingBlock =
 * fallingBlock; }
 * 
 * public Status getStatus() { if (!fallingBlock.isEntityAlive()) { if
 * (fallingBlock.onGround) return Status.HIT_GROUND; else return Status.FALLING;
 * } if (!fallingBlock.isAddedToWorld()) return Status.INVALID; return
 * Status.INVALID; }
 * 
 * public static enum Status { FALLING, HIT_GROUND, INVALID; } } }
 */