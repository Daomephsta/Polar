package io.github.daomephsta.polar.common.components;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarPlayerData;
import io.github.daomephsta.polar.api.factions.FactionAlignment;
import io.github.daomephsta.polar.api.factions.FactionRank;
import io.github.daomephsta.polar.common.NBTExtensions;
import io.github.daomephsta.polar.common.PolarCommonNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

/**A component used by Polar to store data associated with a player, such as rank and faction**/
public class PolarPlayerDataComponent
{
	public static void register(EntityComponentFactoryRegistry registry)
	{
	    registry.registerFor(PlayerEntity.class, PolarApi.PLAYER_DATA, PolarPlayerData::new);
	}
	
	/**Stores data associated with a player. Non-API class, other mods should access player data through {@link PolarApi#getPlayerData(PlayerEntity)}**/
	public static class PolarPlayerData implements IPolarPlayerData
	{
		private final PlayerEntity player;
		// The faction the player is aligned with. Defaults to unaligned.
		private FactionAlignment faction = FactionAlignment.UNALIGNED;
		// The player's rank within their faction. Defaults to none.
		private FactionRank rank = FactionRank.NONE;
		// The player's residual charge. Defaults to none;
		private Polarity residualPolarity = Polarity.NONE;
		
		public PolarPlayerData(PlayerEntity player)
		{
			this.player = player;
		}

		public static PolarPlayerData get(PlayerEntity player)
		{
			return (PolarPlayerData) IPolarPlayerData.get(player);
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
			setResidualPolarityInternal(residualPolarity);
            sync();
		}
		
		private void setResidualPolarityInternal(Polarity residualPolarity)
		{
			this.residualPolarity = residualPolarity;
		}

		public void sync()
		{
		    if (!player.getEntityWorld().isClient())
		        PolarCommonNetworking.sendResidualChargePacket((ServerPlayerEntity) player, residualPolarity);
		}

		@Override
		public void readFromNbt(NbtCompound tag)
		{
			this.faction = NBTExtensions.getEnumConstant(tag, FactionAlignment.class, "faction");
			this.rank = NBTExtensions.getEnumConstant(tag, FactionRank.class, "rank");
			this.residualPolarity = NBTExtensions.getEnumConstant(tag, Polarity.class, "residual_polarity");
		}

		@Override
		public void writeToNbt(NbtCompound tag)
		{
			NBTExtensions.putEnumConstant(tag, "faction", this.faction);
			NBTExtensions.putEnumConstant(tag, "rank", this.rank);
			NBTExtensions.putEnumConstant(tag, "residual_polarity", this.residualPolarity);
		}
	}
}
