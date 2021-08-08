
package io.github.daomephsta.polar.common.advancements.triggers;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.util.Identifier;

public abstract class AbstractCriterion<T extends CriterionConditions> implements Criterion<T>
{
    private final Identifier id;
    private final Multimap<PlayerAdvancementTracker, ConditionsContainer<T>> conditions =
        MultimapBuilder.hashKeys().hashSetValues().build();

    protected AbstractCriterion(Identifier id)
    {
        this.id = id;
    }

    @Override
    public void beginTrackingCondition(PlayerAdvancementTracker tracker, ConditionsContainer<T> container)
    {
        conditions.get(tracker).add(container);
    }
    
    @Override
    public void endTrackingCondition(PlayerAdvancementTracker tracker, ConditionsContainer<T> container)
    {
        Collection<ConditionsContainer<T>> handler = conditions.get(tracker);
        handler.remove(container);
        if (handler.isEmpty())
            conditions.removeAll(tracker);
    }
    
    @Override
    public void endTracking(PlayerAdvancementTracker tracker)
    {
        conditions.removeAll(tracker);
    }

    @Override
    public Identifier getId()
    {
        return id;
    }
    
    public boolean isTrackedBy(PlayerAdvancementTracker tracker)
    {
        return conditions.containsKey(tracker);
    }
    
    public Collection<ConditionsContainer<T>> getContainers(PlayerAdvancementTracker tracker)
    {
        return conditions.get(tracker);
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
