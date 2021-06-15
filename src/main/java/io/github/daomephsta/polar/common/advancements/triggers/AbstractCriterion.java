
package io.github.daomephsta.polar.common.advancements.triggers;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.github.daomephsta.polar.common.advancements.triggers.AbstractCriterion.AbstractHandler;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.util.Identifier;

public abstract class AbstractCriterion<T extends CriterionConditions, H extends AbstractHandler<T>> implements Criterion<T>
{
    private final Identifier id;
    private final Function<PlayerAdvancementTracker, H> handlerFactory;
    private final Map<PlayerAdvancementTracker, H> handlers = Maps.newHashMap();

    protected AbstractCriterion(Identifier id, Function<PlayerAdvancementTracker, H> handlerFactory)
    {
        this.id = id;
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void beginTrackingCondition(PlayerAdvancementTracker tracker, ConditionsContainer<T> container)
    {
        handlers.computeIfAbsent(tracker, handlerFactory).addContainer(container);
    }
    
    @Override
    public void endTrackingCondition(PlayerAdvancementTracker tracker, ConditionsContainer<T> container)
    {
        H handler = handlers.get(tracker);
        if (handler != null)
        {
            handler.removeContainer(container);
            if (handler.isEmpty())
                handlers.remove(tracker);
        }
    }
    
    @Override
    public void endTracking(PlayerAdvancementTracker tracker)
    {
        handlers.remove(tracker);
    }
    @Override
    public Identifier getId()
    {
        return id;
    }
    
    public boolean hasHandler(PlayerAdvancementTracker tracker)
    {
        return handlers.containsKey(tracker);
    }
    
    public H getHandler(PlayerAdvancementTracker tracker)
    {
        return handlers.get(tracker);
    }
    
    protected static abstract class AbstractHandler<T extends CriterionConditions> 
    {
        private final Set<Criterion.ConditionsContainer<T>> containers = Sets.newHashSet();
        
        public Iterable<Criterion.ConditionsContainer<T>> getContainers()
        {
            return containers;
        }

        boolean isEmpty()
        {
            return containers.isEmpty();
        }

        boolean addContainer(ConditionsContainer<T> container)
        {
            return containers.add(container);
        }

        boolean removeContainer(ConditionsContainer<T> container)
        {
            return containers.remove(container);
        }
    }
}
