package leviathan143.polar.common.entities;


import daomephsta.umbra.network.datasync.UmbraDataSerializers;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.ITappable;
import leviathan143.polar.common.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class EntityAnomaly extends Entity
{
	private static final int MIN_DAYS = 3, MAX_DAYS = 8;
	//TODO: Replace both data parameters with data sent using IEntityAdditionalSpawnData
	private static final DataParameter<Polarity> POLARITY = EntityDataManager.createKey(EntityAnomaly.class, Polarity.getDataSerializer());
	private static final DataParameter<Long> CLOSING_TIMESTAMP = EntityDataManager.createKey(EntityAnomaly.class, UmbraDataSerializers.LONG);
	private final ITappable tappingHandler;
	
	public EntityAnomaly(World world)
	{
		super(world);
		setEntityInvulnerable(true);
		setSize(0.8F, 0.8F);
		//1000-2000 charge
		this.tappingHandler = new AnomalyTappingHandler(this, 1000 + rand.nextInt(1000));
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
		if(closeIn <= 0 || tappingHandler.getStoredCharge() == 0)
		{
			this.setDead();
			return;
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == PolarAPI.CAPABILITY_TAPPABLE 
			? true 
			: super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == PolarAPI.CAPABILITY_TAPPABLE) 
			return PolarAPI.CAPABILITY_TAPPABLE.cast(tappingHandler);
		return super.getCapability(capability, facing);
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
