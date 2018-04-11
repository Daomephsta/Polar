package leviathan143.polar.common.entities;


import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Util;
import leviathan143.polar.common.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityAnomaly extends Entity
{
	private static final int MIN_DAYS = 3, MAX_DAYS = 8;
	private static final DataParameter<Polarity> POLARITY = EntityDataManager.createKey(EntityAnomaly.class, Polarity.getDataSerializer());
	private static final DataParameter<Long> CLOSING_TIMESTAMP = EntityDataManager.createKey(EntityAnomaly.class, Util.DATA_SERIALIZER_LONG);
	
	public EntityAnomaly(World world)
	{
		super(world);
		setEntityInvulnerable(true);
		setSize(0.8F, 0.8F);
		
	}

	@Override
	protected void entityInit() 
	{
		getDataManager().register(POLARITY, world.rand.nextBoolean() ? Polarity.RED : Polarity.BLUE);
		int days = MIN_DAYS + world.rand.nextInt(MAX_DAYS - MIN_DAYS); //3 - 7 days
		int additionalTicks = (int) Math.floor(world.rand.nextDouble() * Constants.MC_DAY_TICKS); //Random portion of a day
		//3 - 8 days
		getDataManager().register(CLOSING_TIMESTAMP, world.getTotalWorldTime() + days * Constants.MC_DAY_TICKS + additionalTicks);
	}
	
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		long closeIn = getDataManager().get(CLOSING_TIMESTAMP) - world.getTotalWorldTime();
		if(closeIn <= 0)
		{
			this.setDead();
			return;
		}
	}
	
	public Polarity getPolarity()
	{
		return getDataManager().get(POLARITY);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		getDataManager().set(POLARITY, Polarity.valueOf(compound.getString("polarity")));
		if(compound.hasKey("closeIn")) getDataManager().set(CLOSING_TIMESTAMP, world.getTotalWorldTime() + compound.getLong("closeIn"));
		else getDataManager().set(CLOSING_TIMESTAMP, compound.getLong("closingTimestamp"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("polarity", dataManager.get(POLARITY).name());
		compound.setLong("closingTimestamp", getDataManager().get(CLOSING_TIMESTAMP));
	}
}
