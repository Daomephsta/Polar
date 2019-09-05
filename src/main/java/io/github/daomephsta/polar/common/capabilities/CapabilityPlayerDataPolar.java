package io.github.daomephsta.polar.common.capabilities;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.capabilities.IPlayerDataPolar;
import io.github.daomephsta.polar.api.factions.FactionAlignment;
import io.github.daomephsta.polar.api.factions.FactionRank;
import net.minecraft.entity.player.PlayerEntity;

/**A capability used by Polar to store data associated with a player, such as rank and faction**/
public class CapabilityPlayerDataPolar
{
	/**Stores data associated with a player. Non-API class, other mods should access player data through {@link PolarAPI#getPlayerData(PlayerEntity)}**/
	public static class PlayerDataPolar implements IPlayerDataPolar
	{
		private final PlayerEntity player;
		// The faction the player is aligned with. Defaults to unaligned.
		private FactionAlignment faction = FactionAlignment.UNALIGNED;
		// The player's rank within their faction. Defaults to none.
		private FactionRank rank = FactionRank.NONE;
		// The player's residual charge. Defaults to none;
		private Polarity residualPolarity = Polarity.NONE;
		
		public PlayerDataPolar(PlayerEntity player)
		{
			this.player = player;
		}

		public static PlayerDataPolar get(PlayerEntity player)
		{
			//TODO Player data
			return null;
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
			if (!player.getEntityWorld().isClient)
				sync();
			setResidualPolarityInternal(residualPolarity);
		}
		
		private void setResidualPolarityInternal(Polarity residualPolarity)
		{
			this.residualPolarity = residualPolarity;
		}

		@Override
		public void sync()
		{
			//TODO PacketHandler.CHANNEL.sendTo(new PacketSetResidualCharge(residualPolarity), (PlayerEntityMP) player);
		}
	}
}
