package io.github.daomephsta.polar.api.components;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.factions.FactionAlignment;
import io.github.daomephsta.polar.api.factions.FactionRank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

/**An interface that provides access to Polar player data. Designed to be used by other mods.**/
public interface IPolarPlayerData extends AutoSyncedComponent
{
    public static IPolarPlayerData get(PlayerEntity player)
    {
        return PolarApi.PLAYER_DATA.get(ComponentProvider.fromEntity(player));
    }

    public FactionAlignment getFaction();

    public void setFaction(FactionAlignment faction);

    public FactionRank getRank();

    public void setRank(FactionRank rank);

    public boolean hasStartedResearch(Identifier researchId);

    public boolean hasCompletedResearch(Identifier researchId);

    public void startResearch(Identifier researchId);

    public void completeResearch(Identifier researchId);

    public void forgetResearch(Identifier researchId);
}
