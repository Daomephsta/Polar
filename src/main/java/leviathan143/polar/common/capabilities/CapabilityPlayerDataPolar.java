package leviathan143.polar.common.capabilities;

import daomephsta.umbra.capabilities.CapabilityHelper;
import daomephsta.umbra.nbt.NBTExtensions;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPlayerDataPolar;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

/**A capability used by Polar to store data associated with a player, such as rank and faction**/
public class CapabilityPlayerDataPolar
{
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IPlayerDataPolar.class,
				CapabilityHelper.fromLambdas(
					(Capability<IPlayerDataPolar> capability, IPlayerDataPolar instance, EnumFacing side, NBTBase nbt) -> 
					{
						PlayerDataPolar internalImpl = (PlayerDataPolar) instance;
						NBTTagCompound compoundNBT = (NBTTagCompound) nbt;
						internalImpl.setFaction(NBTExtensions.getEnumConstant(compoundNBT, FactionAlignment.class, "faction", FactionAlignment.UNALIGNED));
						internalImpl.setRank(NBTExtensions.getEnumConstant(compoundNBT, FactionRank.class, "rank", FactionRank.NONE));
						internalImpl.setResidualPolarity(NBTExtensions.getEnumConstant(compoundNBT, Polarity.class, "residual_polarity", Polarity.NONE));
					},
					(Capability<IPlayerDataPolar> capability, IPlayerDataPolar instance, EnumFacing side) -> 
					{
						PlayerDataPolar internalImpl = (PlayerDataPolar) instance;
						NBTTagCompound nbt = new NBTTagCompound();
						NBTExtensions.setEnumConstant(nbt, "faction", internalImpl.getFaction());
						NBTExtensions.setEnumConstant(nbt, "rank", internalImpl.getRank());
						NBTExtensions.setEnumConstant(nbt, "residual_polarity", internalImpl.getResidualPolarity());
						return nbt;
					}), 
					PlayerDataPolar::new);
	}
	
	public static class PlayerDataProvider implements ICapabilitySerializable<NBTBase>
	{
		private IPlayerDataPolar data = new PlayerDataPolar();//PolarAPI.PLAYER_DATA_POLAR.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == PolarAPI.PLAYER_DATA_POLAR;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			if(capability == PolarAPI.PLAYER_DATA_POLAR) return PolarAPI.PLAYER_DATA_POLAR.cast(data);
			return null;
		}

		@Override
		public NBTBase serializeNBT()
		{
			return PolarAPI.PLAYER_DATA_POLAR.getStorage().writeNBT(PolarAPI.PLAYER_DATA_POLAR, data, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			PolarAPI.PLAYER_DATA_POLAR.getStorage().readNBT(PolarAPI.PLAYER_DATA_POLAR, data, null, nbt);
		}
	}

	/**Stores data associated with a player. Non-API class, other mods should access player data through {@link PolarAPI#getPlayerData(EntityPlayer)}**/
	public static class PlayerDataPolar implements IPlayerDataPolar
	{
		// The faction the player is aligned with. Defaults to unaligned.
		private FactionAlignment faction = FactionAlignment.UNALIGNED;
		// The player's rank within their faction. Defaults to none.
		private FactionRank rank = FactionRank.NONE;
		// The player's residual charge. Defaults to none;
		private Polarity residualPolarity = Polarity.NONE;
		
		public static PlayerDataPolar get(EntityPlayer player)
		{
			return (PlayerDataPolar) player.getCapability(PolarAPI.PLAYER_DATA_POLAR, null);
		}
		
		@Override
		public FactionAlignment getFaction()
		{
			return faction;
		}

		@Override
		public void setFaction(FactionAlignment faction)
		{
			this.faction = faction;
		}

		@Override
		public FactionRank getRank()
		{
			return rank;
		}

		@Override
		public void setRank(FactionRank rank)
		{
			this.rank = rank;
		}

		public Polarity getResidualPolarity()
		{
			return residualPolarity;
		}

		public void setResidualPolarity(Polarity residualPolarity)
		{
			this.residualPolarity = residualPolarity;
		}
	}
}
