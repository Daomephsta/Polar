package io.github.daomephsta.polar.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import io.github.daomephsta.polar.api.PolarPlugin;
import io.github.daomephsta.polar.common.advancements.triggers.PolarCriteria;
import io.github.daomephsta.polar.common.blockentities.PolarBlockEntityTypes;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent;
import io.github.daomephsta.polar.common.config.PolarConfig;
import io.github.daomephsta.polar.common.core.PolarApiImplementation;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalySpawningHandler;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalyEntity;
import io.github.daomephsta.polar.common.handlers.JawbladeHandler;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.handlers.wearables.WearablesHandler;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import io.github.daomephsta.polar.common.recipes.PolarRecipes;
import io.github.daomephsta.polar.common.research.ResearchManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;


public class Polar implements ModInitializer
{
    public static final String MOD_ID = "polar";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final PolarConfig CONFIG = new PolarConfig();

    @Override
    public void onInitialize()
    {
        BlockRegistry.initialize();
        ItemRegistry.initialize();
        EntityRegistry.initialise();
        PolarTags.initialise();
        CompatibilityTags.initialise();
        PolarCriteria.initialise();
        PolarBlockEntityTypes.initialize();
        AnomalySpawningHandler.registerEventCallbacks();
        JawbladeHandler.registerEventCallbacks();
        ResidualPolarityHandler.registerEventCallbacks();
        WearablesHandler.initialise();
        PolarRecipes.initialise();
        PolarCommonNetworking.initialise();
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            PolarCommand.register(server.getCommandManager().getDispatcher());
        });
        ResearchManager.INSTANCE.initialise();
        initialiseApi();
    }

    public void initialiseApi()
    {
        PolarApiImplementation polarApi = new PolarApiImplementation();
        for (PolarPlugin plugin : FabricLoader.getInstance().getEntrypoints("polar_api", PolarPlugin.class))
        {
            plugin.onPolarInitialised(polarApi);
        }
    }

    // Static entrypoint for cardinal-components-entity. See fabric.mod.json
    public static void registerEntityComponents(EntityComponentFactoryRegistry registry)
    {
        PolarPlayerDataComponent.register(registry);
        AnomalyEntity.registerComponents(registry);
    }

    //  Static entrypoint for cardinal-components-item. See fabric.mod.json
    public static void registerItemComponents(ItemComponentFactoryRegistry registry)
    {
        PolarChargeStorageComponent.register(registry);
    }

    public static Identifier id(String name)
    {
        return new Identifier(MOD_ID, name);
    }

    public static TranslatableText translation(String suffix, Object... arguments)
    {
        return new TranslatableText(MOD_ID + '.' + suffix, arguments);
    }
}
