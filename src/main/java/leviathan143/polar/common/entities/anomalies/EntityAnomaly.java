package leviathan143.polar.common.entities.anomalies;

import io.netty.buffer.ByteBuf;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.ITappable;
import leviathan143.polar.common.advancements.triggers.TriggerRegistry;
import leviathan143.polar.common.config.PolarConfig;
import leviathan143.polar.common.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityAnomaly extends Entity implements IEntityAdditionalSpawnData
{
	private final ITappable tappingHandler;
	private Polarity polarity;
	private long closingTimestamp;
	private boolean open = false;

	public EntityAnomaly(World world)
	{
		super(world);
		setEntityInvulnerable(true);
		setSize(1.1F, 1.1F);
		// 1000-2000 charge
		this.tappingHandler = new AnomalyTappingHandler(this, 1000 + rand.nextInt(1000));
	}

	public EntityAnomaly(World world, Polarity polarity)
	{
		this(world);

		this.polarity = polarity;
		int days = PolarConfig.anomalies.minLifetime + world.rand.nextInt(PolarConfig.anomalies.maxLifetime - PolarConfig.anomalies.minLifetime); // min to max - 1 days
		int additionalTicks = (int) Math.floor(world.rand.nextDouble() * Constants.MC_DAY_TICKS); // Random portion of a day
		// 3 - 8 days
		this.closingTimestamp = world.getTotalWorldTime() + days * Constants.MC_DAY_TICKS + additionalTicks;
	}

	@Override
	protected void entityInit()
	{}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		long closeIn = closingTimestamp - world.getTotalWorldTime();
		if ((closeIn <= 0 && !open) || tappingHandler.getStoredCharge() == 0)
		{
			this.setDead();
			return;
		}

		if (world.getTotalWorldTime() % 10 == 0) irradiateNearbyItems();
	}

	private void irradiateNearbyItems()
	{
		AxisAlignedBB craftingBB = this.getEntityBoundingBox().grow(2.0D);
		for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, craftingBB))
		{
			if (entityItem.getAge() >= 100)
			{
				ItemStack output = AnomalyIrradiationCrafting.getOutput(getPolarity(), entityItem.getItem());
				if (!output.isEmpty())
				{
					entityItem.setDead();

					// Spawn stacks
					if (!world.isRemote)
					{
						// Spawn full stacks
						for (int i = 0; i < output.getCount() / output.getMaxStackSize(); i++)
						{
							ItemStack fullStack = output.copy();
							fullStack.setCount(output.getMaxStackSize());
							// Spawn up to half a block out
							double x = entityItem.posX + (rand.nextDouble() - 0.5D);
							double z = entityItem.posZ + (rand.nextDouble() - 0.5D);
							EntityItem fullStackEntity = new EntityItem(world, x, entityItem.posY, z, fullStack);
							world.spawnEntity(fullStackEntity);
						}
						// Spawn remainder stack
						ItemStack remainderStack = output.copy();
						remainderStack.setCount(output.getCount() % output.getMaxStackSize());
						// Spawn up to half a block out
						double x = entityItem.posX + (rand.nextDouble() - 0.5D);
						double z = entityItem.posZ + (rand.nextDouble() - 0.5D);
						EntityItem remainderStackEntity = new EntityItem(world, x, entityItem.posY, z, remainderStack);
						world.spawnEntity(remainderStackEntity);
					}
				}
			}
		}
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
	{
		if (player instanceof EntityPlayerMP)
			TriggerRegistry.PLAYER_ANOMALY_INTERACTION.trigger((EntityPlayerMP) player);
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		Entity trueSource = source.getTrueSource();
		if (trueSource instanceof EntityPlayerMP)
			TriggerRegistry.PLAYER_ANOMALY_INTERACTION.trigger((EntityPlayerMP) trueSource);
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (player instanceof EntityPlayerMP)
			TriggerRegistry.PLAYER_ANOMALY_INTERACTION.trigger((EntityPlayerMP) player);
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == PolarAPI.CAPABILITY_TAPPABLE ? true : super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == PolarAPI.CAPABILITY_TAPPABLE) return PolarAPI.CAPABILITY_TAPPABLE.cast(tappingHandler);
		return super.getCapability(capability, facing);
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
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.polarity = Polarity.valueOf(compound.getString("polarity"));
		if (compound.hasKey("closeIn")) this.closingTimestamp = world.getTotalWorldTime() + compound.getLong("closeIn");
		else this.closingTimestamp = compound.getLong("closingTimestamp");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("polarity", this.polarity.name());
		compound.setLong("closingTimestamp", this.closingTimestamp);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.polarity = Polarity.fromIndex(additionalData.readInt());
		this.closingTimestamp = additionalData.readLong();
	}

	@Override
	public void writeSpawnData(ByteBuf additionalData)
	{
		additionalData.writeInt(polarity.getIndex());
		additionalData.writeLong(closingTimestamp);
	}
}
