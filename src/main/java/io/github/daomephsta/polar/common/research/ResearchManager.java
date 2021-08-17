package io.github.daomephsta.polar.common.research;

import java.util.HashMap;
import java.util.Map;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.util.Identifier;

public class ResearchManager
{
    public static final ResearchManager INSTANCE = new ResearchManager();
    private final Map<Identifier, Research> registry = new HashMap<>();

    public void initialise()
    {
        registerResearch(Polar.id("falling_blocks"),
            (player, onCompletion) -> new ObserveFallingBlockObjective(player, onCompletion, 32));
    }

    private void registerResearch(Identifier id, ResearchObjective.Factory... objectives)
    {
        registry.put(id, new Research(id, objectives));
    }

    public boolean exists(Identifier researchId)
    {
        return registry.containsKey(researchId);
    }

    public Research get(Identifier researchId)
    {
        return registry.get(researchId);
    }

    public Iterable<Identifier> getIds()
    {
        return registry.keySet();
    }
}
