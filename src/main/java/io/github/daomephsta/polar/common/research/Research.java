package io.github.daomephsta.polar.common.research;

import java.util.Arrays;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.PolarCommonNetworking;
import io.github.daomephsta.polar.common.PolarCommonNetworking.S2CResearchPacketAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.SystemToast.Type;
import net.minecraft.client.toast.Toast;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Research
{
    private final Identifier id;
    private final ResearchObjective.Factory[] objectiveFactories;
    private final Text displayName;

    public Research(Identifier id, ResearchObjective.Factory... objectiveFactories)
    {
        this.id = id;
        this.displayName = new TranslatableText(id.getNamespace() + ".research." + id.getPath());
        this.objectiveFactories = objectiveFactories;
    }

    public Progress createTracker(PlayerEntity player)
    {
        return new Progress(this, player);
    }

    public Progress createTracker(PlayerEntity player, int progress)
    {
        Progress tracker = new Progress(this, player);
        tracker.progress = progress;
        return tracker;
    }

    public Identifier getId()
    {
        return id;
    }

    public Text getDisplayName()
    {
        return displayName;
    }

    public static class Progress
    {
        private final PlayerEntity researcher;
        private final Research research;
        private final ResearchObjective[] objectives;
        private int progress = 0;

        private Progress(Research research, PlayerEntity researcher)
        {
            this.research = research;
            this.researcher = researcher;
            this.objectives = Arrays.stream(research.objectiveFactories)
                .map(factory -> factory.create(researcher, this::increase))
                .toArray(ResearchObjective[]::new);
        }

        public void startTracking()
        {
            for (var objective : objectives)
                objective.startTracking();
        }

        public void stopTracking()
        {
            for (var objective : objectives)
                objective.stopTracking();
        }

        private void increase()
        {
            this.progress += 1;
            if (isComplete())
                complete();
        }

        public boolean isComplete()
        {
            return progress >= objectives.length;
        }

        public void complete()
        {
            this.progress = objectives.length;
            stopTracking();
            if (researcher.world.isClient())
            {
                Toast toast = new SystemToast(Type.TUTORIAL_HINT, Polar.translation("research.complete"),
                    research.getDisplayName());
                MinecraftClient.getInstance().getToastManager().add(toast);
            }
            else
            {
                PolarCommonNetworking.sendResearchPacket((ServerPlayerEntity) researcher,
                    research.getId(), S2CResearchPacketAction.COMPLETE);
            }
        }

        public NbtElement writeToNbt()
        {
            return NbtInt.of(progress);
        }
    }
}
