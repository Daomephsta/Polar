package io.github.daomephsta.polar.api.components;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.factions.FactionAlignment;
import io.github.daomephsta.polar.api.factions.FactionRank;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.entity.player.PlayerEntity;

/**An interface that provides access to Polar player data. Designed to be used by other mods.**/
public interface IPolarPlayerData extends Component
{
	public static IPolarPlayerData get(PlayerEntity player)
	{
		return PolarAPI.PLAYER_DATA.get(ComponentProvider.fromEntity(player));
	}
	
	public FactionAlignment getFaction();
	
	public void setFaction(FactionAlignment faction);

	public FactionRank getRank();

	public void setRank(FactionRank rank);
}
