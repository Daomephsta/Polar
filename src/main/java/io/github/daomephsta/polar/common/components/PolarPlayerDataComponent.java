package io.github.daomephsta.polar.common.components;

import java.util.HashMap;
import java.util.Map;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarPlayerData;
import io.github.daomephsta.polar.api.factions.FactionAlignment;
import io.github.daomephsta.polar.api.factions.FactionRank;
import io.github.daomephsta.polar.client.PolarClientNetworking;
import io.github.daomephsta.polar.common.NBTExtensions;
import io.github.daomephsta.polar.common.PolarCommonNetworking;
import io.github.daomephsta.polar.common.PolarCommonNetworking.S2CResearchPacketAction;
import io.github.daomephsta.polar.common.research.Research;
import io.github.daomephsta.polar.common.research.Research.Progress;
import io.github.daomephsta.polar.common.research.ResearchManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

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
        private final Map<Identifier, Research.Progress> researchProgress = new HashMap<>();

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

        @Override
        public boolean hasStartedResearch(Identifier research)
        {
            Research.Progress progress = researchProgress.get(research);
            return progress != null && !progress.isComplete();
        }

        @Override
        public boolean hasCompletedResearch(Identifier research)
        {
            Research.Progress progress = researchProgress.get(research);
            return progress != null && progress.isComplete();
        }

        @Override
        public void startResearch(Identifier researchId)
        {
            if (player.world.isClient)
                PolarClientNetworking.sendStartResearchPacket(researchId);
            Research research = ResearchManager.INSTANCE.get(researchId);
            if (research == null)
                throw new IllegalArgumentException("Unknown research " + researchId);
            Progress tracker = research.createTracker(player);
            tracker.startTracking();
            researchProgress.put(researchId, tracker);
        }

        @Override
        public void completeResearch(Identifier researchId)
        {
            if (!researchProgress.containsKey(researchId))
                startResearch(researchId);
            researchProgress.get(researchId).complete();
        }

        @Override
        public void forgetResearch(Identifier researchId)
        {
            if (!player.world.isClient)
            {
                PolarCommonNetworking.sendResearchPacket((ServerPlayerEntity) player,
                    researchId, S2CResearchPacketAction.FORGET);
            }
            Progress research = researchProgress.remove(researchId);
            if (research != null)
                research.stopTracking();
        }

        public void sync()
        {
            if (!player.getEntityWorld().isClient())
                PolarCommonNetworking.sendResidualChargePacket((ServerPlayerEntity) player, residualPolarity);
        }

        @Override
        public void readFromNbt(NbtCompound nbt)
        {
            this.faction = NBTExtensions.getEnumConstant(nbt, FactionAlignment.class, "faction");
            this.rank = NBTExtensions.getEnumConstant(nbt, FactionRank.class, "rank");
            this.residualPolarity = NBTExtensions.getEnumConstant(nbt, Polarity.class, "residual_polarity");
            researchProgress.clear();
            var researchProgressNbt = nbt.getCompound("research_progress");
            for (String key : researchProgressNbt.getKeys())
            {
                Identifier researchId = new Identifier(key);
                Research research = ResearchManager.INSTANCE.get(researchId);
                Progress tracker = research.createTracker(player, researchProgressNbt.getInt(key));
                if (!tracker.isComplete()) tracker.startTracking();
                researchProgress.put(researchId, tracker);
            }
        }

        @Override
        public void writeToNbt(NbtCompound nbt)
        {
            NBTExtensions.putEnumConstant(nbt, "faction", this.faction);
            NBTExtensions.putEnumConstant(nbt, "rank", this.rank);
            NBTExtensions.putEnumConstant(nbt, "residual_polarity", this.residualPolarity);
            var researchProgressNbt = new NbtCompound();
            for (var progress : researchProgress.entrySet())
                researchProgressNbt.put(progress.getKey().toString(), progress.getValue().writeToNbt());
            nbt.put("research_progress", researchProgressNbt);
        }
    }
}
