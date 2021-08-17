package io.github.daomephsta.polar.common;

import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.command.argument.EntityArgumentType.player;
import static net.minecraft.command.argument.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.argument.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import io.github.daomephsta.polar.common.research.ResearchManager;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class PolarCommand
{
    private static final DynamicCommandExceptionType RESEARCH_ID_INVALID_EXCEPTION =
        new DynamicCommandExceptionType(context ->
        new TranslatableText(Polar.MOD_ID + ".command.invalid_research_id", context));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(literal("polar")
            .then(research()));
    }

    private static ArgumentBuilder<ServerCommandSource, ?> research()
    {
        return literal("research")
            .then(argument("target", player())
                .then(literal("learn").then(learnResearch()))
                .then(literal("forget").then(forgetResearch())));
    }

    private static ArgumentBuilder<ServerCommandSource, ?> learnResearch()
    {
        return argument("research_id", identifier())
            .suggests(PolarCommand::researchIdSuggestions)
            .executes(context ->
            {
                Identifier researchId = getResearchId(context);
                getPlayerData(context).completeResearch(researchId);
                return Command.SINGLE_SUCCESS;
            });
    }

    public static CompletableFuture<Suggestions> researchIdSuggestions(
        CommandContext<ServerCommandSource> context, SuggestionsBuilder builder)
    {
        return CommandSource.suggestIdentifiers(ResearchManager.INSTANCE.getIds(), builder);
    }

    private static ArgumentBuilder<ServerCommandSource, ?> forgetResearch()
    {
        return argument("research_id", identifier())
            .suggests(PolarCommand::researchIdSuggestions)
            .executes(context ->
            {
                Identifier researchId = getResearchId(context);
                getPlayerData(context).forgetResearch(researchId);
                return Command.SINGLE_SUCCESS;
            });
    }

    private static PolarPlayerData getPlayerData(CommandContext<ServerCommandSource> context)
        throws CommandSyntaxException
    {
        return PolarPlayerData.get(getPlayer(context, "target"));
    }

    private static Identifier getResearchId(CommandContext<ServerCommandSource> context)
        throws CommandSyntaxException
    {
        Identifier researchId = getIdentifier(context, "research_id");
        if (!ResearchManager.INSTANCE.exists(researchId))
            throw RESEARCH_ID_INVALID_EXCEPTION.create(researchId);
        return researchId;
    }
}
