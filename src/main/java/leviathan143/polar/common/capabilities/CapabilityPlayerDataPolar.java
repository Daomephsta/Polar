package leviathan143.polar.common.capabilities;

import leviathan143.polar.api.IPlayerDataPolar;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;

/**A capability used by Polar to store data associated with a player, such as rank and faction**/
public class CapabilityPlayerDataPolar
{
	public static void register()
	{
		CapabilityManager.INSTANCE.register(PlayerDataPolar.class,
				new Capability.IStorage<PlayerDataPolar>()
				{
					@Override
					public NBTBase writeNBT(
							Capability<PlayerDataPolar> capability,
							PlayerDataPolar instance, EnumFacing side)
					{
						return null;
					}

					@Override
					public void readNBT(Capability<PlayerDataPolar> capability,
							PlayerDataPolar instance, EnumFacing side,
							NBTBase nbt)
					{}
				}, () -> null);
	}
	
	public static class PlayerDataProvider implements ICapabilitySerializable<NBTTagCompound>
	{
		private PlayerDataPolar data = new PlayerDataPolar();

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
		public NBTTagCompound serializeNBT()
		{
			return data.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			data.deserializeNBT(nbt);
		}
	}

	/**Stores data associated with a player. Non-API class, other mods should access player data through {@link PolarAPI#getPlayerData(EntityPlayer)}**/
	public static class PlayerDataPolar implements IPlayerDataPolar,  INBTSerializable<NBTTagCompound>
	{
		// The faction the player is aligned with. Defaults to unaligned.
		private FactionAlignment faction = FactionAlignment.UNALIGNED;
		// The player's rank within their faction. Defaults to none.
		private FactionRank rank = FactionRank.NONE;
		
		public static IPlayerDataPolar get(EntityPlayer player)
		{
			return player.getCapability(PolarAPI.PLAYER_DATA_POLAR, null);
		}
		
		public FactionAlignment getFaction()
		{
			return faction;
		}

		public void setFaction(FactionAlignment faction)
		{
			this.faction = faction;
		}

		public FactionRank getRank()
		{
			return rank;
		}

		public void setRank(FactionRank rank)
		{
			this.rank = rank;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("faction", faction.name());
			nbt.setString("rank", rank.name());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.faction = FactionAlignment.valueOf(nbt.getString("faction"));
			this.rank = FactionRank.valueOf(nbt.getString("rank"));
		}
	}
}
