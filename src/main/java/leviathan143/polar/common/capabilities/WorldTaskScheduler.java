package leviathan143.polar.common.capabilities;

import java.util.PriorityQueue;
import java.util.Queue;

import leviathan143.polar.common.Polar;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class WorldTaskScheduler
{
	@CapabilityInject(ScheduledTaskQueue.class)
	private static final Capability<ScheduledTaskQueue> TASK_QUEUE = null;

	public static void initialise()
	{
		CapabilityManager.INSTANCE.register(ScheduledTaskQueue.class, new Capability.IStorage<ScheduledTaskQueue>()
				{
					@Override
					public NBTBase writeNBT(Capability<ScheduledTaskQueue> capability, ScheduledTaskQueue instance, EnumFacing side)
					{
						return null;
					}
					@Override
					public void readNBT(Capability<ScheduledTaskQueue> capability, ScheduledTaskQueue instance, EnumFacing side, NBTBase nbt) {}
				}, () -> null);
	}
	
	public static void scheduleTask(World world, ITask task, int delay)
	{
		ScheduledTaskQueue taskQueue = world.getCapability(TASK_QUEUE, null);
		taskQueue.scheduleTask(task, delay);
	}
	
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event)
	{
		ScheduledTaskQueue taskQueue = event.world.getCapability(TASK_QUEUE, null);
		taskQueue.update();
	}
	
	public static class WorldTaskSchedulerProvider implements ICapabilitySerializable<NBTTagCompound>
	{
		private final ScheduledTaskQueue scheduler;
		
		public WorldTaskSchedulerProvider(World world)
		{
			this.scheduler = new ScheduledTaskQueue(world);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == TASK_QUEUE;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			if(capability == TASK_QUEUE) return TASK_QUEUE.cast(scheduler);
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return scheduler.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			scheduler.deserializeNBT(nbt);
		}
	}

	static class ScheduledTaskQueue implements INBTSerializable<NBTTagCompound>
	{
		private final World world;
		private final Queue<Task> scheduledTasks = new PriorityQueue<>();
		
		public ScheduledTaskQueue(World world)
		{
			this.world = world;
		}

		public void scheduleTask(ITask task, int delay)
		{
			scheduledTasks.add(new Task(world.getTotalWorldTime() + delay, task));
		}
		
		public void update()
		{
			long currentWorldTime = world.getTotalWorldTime();
			while(!scheduledTasks.isEmpty() && scheduledTasks.peek().getExecutionTime() == currentWorldTime)
			{
				scheduledTasks.poll().task.perform(world);
			}
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			
		}
		
		private static class Task implements Comparable<Task>
		{
			private final ITask task;
			private final long executionTime;

			public Task(long executionTime, ITask task)
			{
				this.executionTime = executionTime;
				this.task = task;
			}
			
			public long getExecutionTime()
			{
				return executionTime;
			}

			@Override
			public int compareTo(Task o)
			{
				return executionTime < o.executionTime ? -1 : executionTime > o.executionTime ? 1 : 0;
			}
		}
	}
	
	@FunctionalInterface
	public static interface ITask
	{
		public void perform(World world);
	}
}
