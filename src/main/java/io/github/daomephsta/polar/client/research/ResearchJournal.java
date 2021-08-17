package io.github.daomephsta.polar.client.research;

import io.github.daomephsta.inscribe.api.GuideConfigurator;
import io.github.daomephsta.inscribe.api.GuideFlags;
import io.github.daomephsta.inscribe.api.IGuide;
import io.github.daomephsta.inscribe.client.guide.template.Arguments;
import io.github.daomephsta.inscribe.common.util.Identifiers;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import io.github.daomephsta.polar.common.research.ResearchManager;
import net.minecraft.client.MinecraftClient;

public class ResearchJournal implements GuideConfigurator
{
    public static final ResearchJournal INSTANCE = new ResearchJournal();

    @Override
    public void configure(IGuide researchJournal)
    {
        GuideFlags flags = researchJournal.getFlags();
        for (var researchId : ResearchManager.INSTANCE.getIds())
        {
            var working = Identifiers.working(researchId).addPathSegment(0, "research");
            flags.register(working.toIdentifier(),
                () -> PolarPlayerData.get(MinecraftClient.getInstance().player).hasCompletedResearch(researchId));
            flags.register(working.addPathSegment("in_progress").toIdentifier(),
                () -> PolarPlayerData.get(MinecraftClient.getInstance().player).hasStartedResearch(researchId));
        }
    }

    public static void processTemplate(Arguments arguments)
    {
        var inProgressFlag = new StringBuilder(arguments.getString("research"));
        inProgressFlag.insert(inProgressFlag.indexOf(":") + 1, "research/").append('/').append("in_progress");
        arguments.putString("in_progress_flag", inProgressFlag.toString());

        var completedFlag = new StringBuilder(arguments.getString("research"));
        completedFlag.insert(completedFlag.indexOf(":") + 1, "research/");
        arguments.putString("completed_flag", completedFlag.toString());
    }
}
