package io.github.daomephsta.polar.common.entities.anomalies;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.config.PolarConfig;
import io.github.daomephsta.polar.common.core.Constants;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import io.github.daomephsta.polar.common.network.PacketTypes;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAnomaly extends Entity
{
	static
	{
		EntityComponentCallback.event(EntityAnomaly.class).register((entity, components) -> 
			components.put(PolarAPI.CHARGE_STORAGE, new AnomalyChargeStorage(entity, 1000 + entity.random.nextInt(1000))));
	}
	private Polarity polarity;
	private long closingTimestamp;
	private boolean open = false;

	public EntityAnomaly(World world)
	{
		super(EntityRegistry.ANOMALY, world);
		setInvulnerable(true);
		// 1000-2000 charge
	}

	public EntityAnomaly(World world, Polarity polarity)
	{
		this(world);

		this.polarity = polarity;
		int days = PolarConfig.anomalies.minLifetime + world.getRandom().nextInt(PolarConfig.anomalies.maxLifetime - PolarConfig.anomalies.minLifetime); // min to max - 1 days
		int additionalTicks = (int) Math.floor(world.getRandom().nextDouble() * Constants.MC_DAY_TICKS); // Random portion of a day
		// 3 - 8 days
		this.closingTimestamp = world.getTime() + days * Constants.MC_DAY_TICKS + additionalTicks;
	}

	@Override
	public void tick()
	{
		super.tick();
		long closeIn = closingTimestamp - world.getTime();
		if ((closeIn <= 0 && !open) || IPolarChargeStorage.get(this).getStoredCharge() == 0)
		{
			this.remove();
			return;
		}
		if (world.getTime() % 10 == 0) irradiateNearbyItems();
	}

	private void irradiateNearbyItems()
	{
		Box irradiationBounds = this.getBoundingBox().expand(2.0D);
		for (ItemEntity entityItem : world.getEntities(ItemEntity.class, irradiationBounds))
		{
			if (entityItem.getAge() >= 100)
			{
				ItemStack output = AnomalyIrradiationCrafting.getOutput(getPolarity(), entityItem.getStack());
				if (!output.isEmpty())
				{
					entityItem.remove();

					// Spawn stacks
					if (!world.isClient)
					{
						// Spawn full stacks
						for (int i = 0; i < output.getCount() / output.getCount(); i++)
						{
							ItemStack fullStack = output.copy();
							fullStack.setCount(output.getMaxCount());
							// Spawn up to half a block out
							double x = entityItem.x + (random.nextDouble() - 0.5D);
							double z = entityItem.z + (random.nextDouble() - 0.5D);
							ItemEntity fullStackEntity = new ItemEntity(world, x, entityItem.y, z, fullStack);
							world.spawnEntity(fullStackEntity);
						}
						// Spawn remainder stack
						ItemStack remainderStack = output.copy();
						remainderStack.setCount(output.getCount() % output.getMaxCount());
						// Spawn up to half a block out
						double x = entityItem.x + (random.nextDouble() - 0.5D);
						double z = entityItem.z + (random.nextDouble() - 0.5D);
						ItemEntity remainderStackEntity = new ItemEntity(world, x, entityItem.y, z, remainderStack);
						world.spawnEntity(remainderStackEntity);
					}
				}
			}
		}
	}
	
	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d vec, Hand hand)
	{
		/*TODO Advancements
		TriggerRegistry.PLAYER_ANOMALY_INTERACTION.trigger((EntityPlayerMP) player);*/
		return ActionResult.SUCCESS;
	}
	
	@Override
	public boolean damage(DamageSource source, float amount)
	{
		Entity trueSource = source.getAttacker();
		/*TODO Advancements
		TriggerRegistry.PLAYER_ANOMALY_INTERACTION.trigger((EntityPlayerMP) trueSource);*/
		return super.damage(source, amount);
	}
	
	@Override
	public void onPlayerCollision(PlayerEntity player)
	{
			/*TODO Advancements
			TriggerRegistry.PLAYER_ANOMALY_INTERACTION.trigger(player);*/
	}
	
	@Override
	public boolean collides()
	{
		return true;
	}

	public Polarity getPolarity()
	{
		return polarity;
	}

	public boolean isOpen()
	{
		return open;
	}

	public void open()
	{
		this.open = true;
	}

	public void close()
	{
		this.open = false;
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag)
	{
		this.polarity = Polarity.valueOf(tag.getString("polarity"));
		if (tag.containsKey("closeIn")) this.closingTimestamp = world.getTime() + tag.getLong("closeIn");
		else this.closingTimestamp = tag.getLong("closingTimestamp");
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag)
	{
		tag.putString("polarity", this.polarity.name());
		tag.putLong("closingTimestamp", this.closingTimestamp);
	}
	
	@Override
	protected void initDataTracker() {}
	
	@Override
	public Packet<?> createSpawnPacket()
	{
		return PacketTypes.SPAWN_ENTITY.toPacket(this);
	}
}
