package io.github.daomephsta.polar.common.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import blue.endless.jankson.Comment;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.AnnotatedSettings;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Listener;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting.Constrain;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.FiberException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.StringConfigType;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import net.minecraft.util.Identifier;

public class PolarConfig
{
    @Setting(ignore = true)
    public static final PolarConfig POLAR_CONFIG = new PolarConfig();
    @Setting(ignore = true)
    private static final Logger LOGGER = LogManager.getLogger();
    @Setting(ignore = true)
    private static final Path PATH = Paths.get("config/polar.json5");
    @Setting.Group
    private Anomalies anomalies = new Anomalies();
    @Setting.Group
    private Charge charge = new Charge();
    
    public Anomalies anomalies()
    {
        return anomalies;
    }
    
    public Charge charge()
    {
        return charge;
    }
    
    public static class Anomalies
    {
        @Constrain.Range(min = 0)
        @Comment("The minimum distance an anomaly can spawn from the player")
        private int minRadius = 32;
        @Constrain.Range(min = 0)
        @Comment("The maximum distance an anomaly can spawn from the player")
        private int maxRadius = 64;
        @Constrain.Range(min = 0)
        @Comment("The minimum height anomalies can spawn at")
        private int minSpawnY = 0;
        @Constrain.Range(min = 0)
        @Comment("The maximum height anomalies can spawn at")
        private int maxSpawnY = 72;
        @Constrain.Range(min = 1)
        @Comment("The minimum number of days an anomaly will stay open for, if left untapped")
        private int minLifetime = 3;
        @Constrain.Range(min = 1)
        @Comment("The maximum number of days an anomaly will stay open for, if left untapped")
        private int maxLifetime = 8;
        @Constrain.Range(min = 1)
        @Comment("The maximum number of anomalies that can be loaded at once")
        private int maxAnomalyCount = 100;
        @Comment("Anomalies will not spawn in any dimension that has its identifier in this list")
        private Set<Identifier> dimBlackList = new HashSet<>();
        
        public int minRadius()
        {
            return minRadius;
        }
        
        @Listener("minRadius")
        private void minRadiusValidator(Integer oldValue, Integer newValue)
        {
            if (minRadius >= maxRadius)
            {
                this.minRadius = oldValue;
                LOGGER.warn("anomalies.minRadius must be less than anomalies.maxRadius. "
                    + "Value reset to {}", oldValue);
            }
        }

        public int maxRadius()
        {
            return maxRadius;
        }

        public int minSpawnY()
        {
            return minSpawnY;
        }

        public int maxSpawnY()
        {
            return maxSpawnY;
        }

        public int minLifetime()
        {
            return minLifetime;
        }

        public int maxLifetime()
        {
            return maxLifetime;
        }

        public int maxAnomalyCount()
        {
            return maxAnomalyCount;
        }
        
        public boolean isDimensionBlacklisted(Identifier identifier)
        {
            return dimBlackList.contains(identifier);
        }
    }
    
    public static class Charge
    {
        @Constrain.Range(min = 1)
        @Comment("The maximum charge a Gravitic Stabiliser can hold")
        private int fallingBlockStabiliserMaxCharge = 128;
        @Constrain.Range(min = 0)
        @Comment("How much charge it costs to stabilise a block")
        private int fallingBlockStabiliserActivationCost = 2;
        
        @Constrain.Range(min = 1)
        @Comment("The maximum charge a Percussive Disintegrator can hold")
        private int fallingBlockDestroyerMaxCharge = 256;
        @Constrain.Range(min = 0)
        @Comment("How much charge it costs to destroy a block")
        private int fallingBlockDestroyerActivationCost = 2;
        
        public int fallingBlockStabiliserMaxCharge()
        {
            return fallingBlockStabiliserMaxCharge;
        }
        
        public int fallingBlockStabiliserActivationCost()
        {
            return fallingBlockStabiliserActivationCost;
        }
        
        public int fallingBlockDestroyerMaxCharge()
        {
            return fallingBlockDestroyerMaxCharge;
        }
        
        public int fallingBlockDestroyerActivationCost()
        {
            return fallingBlockDestroyerActivationCost;
        }
    }
    
    public static void initialise()
    {
        try
        {
            StringConfigType<Identifier> identifier = ConfigTypes.STRING
                .withPattern("[a-z0-9_.-]*:[a-z0-9\\/._-]+")
                .derive(Identifier.class, Identifier::new, Identifier::toString);
            AnnotatedSettings serialisationSettings = AnnotatedSettings.builder()
                .registerTypeMapping(Identifier.class, identifier)
                .build();
            ConfigTree root = ConfigTree.builder().applyFromPojo(POLAR_CONFIG, serialisationSettings).build();
            JanksonValueSerializer serialiser = new JanksonValueSerializer(false);
            if (Files.exists(PATH))
                FiberSerialization.deserialize(root, Files.newInputStream(PATH), serialiser);
            else
                FiberSerialization.serialize(root, Files.newOutputStream(PATH), serialiser);
        } 
        catch (IOException | FiberException e)
        {
            e.printStackTrace();
        }
    }
}
