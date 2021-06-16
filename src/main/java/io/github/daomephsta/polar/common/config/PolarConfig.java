package io.github.daomephsta.polar.common.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.FiberException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.SerializableType;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigType;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.StringConfigType;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import net.minecraft.util.Identifier;

public class PolarConfig
{
    private static final StringConfigType<Identifier> IDENTIFIER = ConfigTypes.STRING
        .withPattern("[a-z0-9_.-]*:[a-z0-9\\\\/._-]+")
        .derive(Identifier.class, Identifier::new, Identifier::toString);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Path PATH = Paths.get("config/polar.json5");
    public final Anomalies anomalies;
    public final Charge charge;
    
    public PolarConfig()
    {
        try
        {
            ConfigTreeBuilder builder = ConfigTree.builder();
            this.anomalies = new Anomalies(builder);
            this.charge = new Charge(builder);
            ConfigTree root = builder.build();
            JanksonValueSerializer serialiser = new JanksonValueSerializer(false);
            if (Files.exists(PATH))
                FiberSerialization.deserialize(root, Files.newInputStream(PATH), serialiser);
            else
                FiberSerialization.serialize(root, Files.newOutputStream(PATH), serialiser);
        } 
        catch (IOException | FiberException e)
        {
            throw new RuntimeException("Polar config initialisation failed", e);
        }
    }
    
    public static class Anomalies
    {
        private final PropertyMirror<Integer> minRadius, maxRadius, minSpawnY, maxSpawnY, 
                                              minLifetime, maxLifetime, maxAnomalyCount;
        private final PropertyMirror<Set<Identifier>> dimBlackList;
        
        private Anomalies(ConfigTreeBuilder builder)
        {
            ConfigTreeBuilder branch = builder.fork("anomalies");
            this.minRadius = bind(branch, "minRadius", ConfigTypes.INTEGER.withMinimum(0), 32, 
                "The minimum distance an anomaly can spawn from a player", 
                this::minRadiusListener);
            this.maxRadius = bind(branch, "maxRadius", ConfigTypes.INTEGER.withMinimum(0), 64, 
                "The maximum distance an anomaly can spawn from a player", 
                this::maxRadiusListener);
            this.minSpawnY = bind(branch, "minSpawnY", ConfigTypes.INTEGER.withMinimum(0), 0, 
                "The minimum height anomalies can spawn at");
            this.maxSpawnY = bind(branch, "maxSpawnY", ConfigTypes.INTEGER.withMinimum(0), 72, 
                "The maximum height anomalies can spawn at");
            this.minLifetime = bind(branch, "minLifetime", ConfigTypes.INTEGER.withMinimum(1), 3, 
                "The minimum number of days an anomaly will stay open for, if left untapped");
            this.maxLifetime = bind(branch, "maxLifetime", ConfigTypes.INTEGER.withMinimum(1), 8, 
                "The maximum height anomalies can spawn at");
            this.maxAnomalyCount = bind(branch, "maxAnomalyCount", ConfigTypes.INTEGER.withMinimum(1), 100, 
                "The maximum number of anomalies that can be loaded at once");
            this.dimBlackList = bind(branch, "dimBlackList", ConfigTypes.makeSet(IDENTIFIER), new HashSet<>(), 
                "Anomalies will not spawn in any dimension that has its identifier in this list");
            branch.build();
        }
        
        public int minRadius()
        {
            return minRadius.getValue();
        }
        
        private void minRadiusListener(Integer oldValue, Integer newValue)
        {
            // Null old value means initial value, always accept
            if (oldValue != null && newValue >= maxRadius())
            {
                this.minRadius.setValue(oldValue);
                LOGGER.warn("anomalies.minRadius must be less than anomalies.maxRadius. "
                    + "Value reset to {}", oldValue);
            }
        }
        
        private void maxRadiusListener(Integer oldValue, Integer newValue)
        {
            // Null old value means initial value, always accept
            if (oldValue != null && newValue <= minRadius())
            {
                this.maxRadius.setValue(oldValue);
                LOGGER.warn("anomalies.maxRadius must be greater than anomalies.minRadius. "
                    + "Value reset to {}", oldValue);
            }
        }

        public int maxRadius()
        {
            return maxRadius.getValue();
        }

        public int minSpawnY()
        {
            return minSpawnY.getValue();
        }

        public int maxSpawnY()
        {
            return maxSpawnY.getValue();
        }

        public int minLifetime()
        {
            return minLifetime.getValue();
        }

        public int maxLifetime()
        {
            return maxLifetime.getValue();
        }

        public int maxAnomalyCount()
        {
            return maxAnomalyCount.getValue();
        }
        
        public boolean isDimensionBlacklisted(Identifier identifier)
        {
            return dimBlackList.getValue().contains(identifier);
        }
    }
    
    public static class Charge
    {
        private final PropertyMirror<Integer> 
            fallingBlockStabiliserMaxCharge,
            fallingBlockStabiliserActivationCost,
            fallingBlockDestroyerMaxCharge,
            fallingBlockDestroyerActivationCost;
        
        public Charge(ConfigTreeBuilder builder)
        {
            ConfigTreeBuilder branch = builder.fork("charge");
            this.fallingBlockStabiliserMaxCharge = bind(branch, "fallingBlockStabiliserMaxCharge", 
                ConfigTypes.INTEGER.withMinimum(1), 128, "Maximum charge a Gravitic Stabiliser can hold");
            this.fallingBlockStabiliserActivationCost = bind(branch, "fallingBlockStabiliserActivationCost", 
                ConfigTypes.INTEGER.withMinimum(1), 2, "Charge cost to stabilise a block");
            this.fallingBlockDestroyerMaxCharge = bind(branch, "fallingBlockDestroyerMaxCharge", 
                ConfigTypes.INTEGER.withMinimum(1), 256, "Maximum charge a Percussive Disintegrator can hold");
            this.fallingBlockDestroyerActivationCost = bind(branch, "fallingBlockDestroyerActivationCost", 
                ConfigTypes.INTEGER.withMinimum(1), 2, "Charge cost to destroy a block");
            branch.build();
        }

        public int fallingBlockStabiliserMaxCharge()
        {
            return fallingBlockStabiliserMaxCharge.getValue();
        }
        
        public int fallingBlockStabiliserActivationCost()
        {
            return fallingBlockStabiliserActivationCost.getValue();
        }
        
        public int fallingBlockDestroyerMaxCharge()
        {
            return fallingBlockDestroyerMaxCharge.getValue();
        }
        
        public int fallingBlockDestroyerActivationCost()
        {
            return fallingBlockDestroyerActivationCost.getValue();
        }
    }
    
    static <R, S, T extends SerializableType<S>> PropertyMirror<R> bind(
        ConfigTreeBuilder branch, String name, ConfigType<R, S, T> type, R defaultValue, 
        String comment)
    {
        PropertyMirror<R> mirror = PropertyMirror.create(type);
        branch.beginValue(name, type, defaultValue)
            .withComment(comment)
            .finishValue(mirror::mirror);
        return mirror;
    }

    static <R, S, T extends SerializableType<S>> PropertyMirror<R> bind(
        ConfigTreeBuilder branch, String name, ConfigType<R, S, T> type, R defaultValue, 
        String comment, BiConsumer<R, R> listener)
    {
        PropertyMirror<R> mirror = PropertyMirror.create(type);
        branch.beginValue(name, type, defaultValue)
            .withListener(listener)
            .withComment(comment)
            .finishValue(mirror::mirror);
        return mirror;
    }
}
