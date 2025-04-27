package com.elysiasilly.babel.api.theatre.actor.command;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ActorArgument implements ArgumentType<ActorSelector> {

    private static final Collection<String> EXAMPLES = Arrays.asList("@a", "@a[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    private static final SimpleCommandExceptionType NO_ACTORS_FOUND = new SimpleCommandExceptionType(Component.translatable("argument.babel.actor.notfound.actor"));

    private final boolean single;

    protected ActorArgument(boolean single) {
        this.single = single;
    }

    public static ActorArgument actors() {
        return new ActorArgument(false);
    }



    public static Collection<Actor> getActors(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        Collection<Actor> collection = getOptionalActors(context, name);

        if(collection.isEmpty()) {
            throw NO_ACTORS_FOUND.create();
        } else {
            return collection;
        }
    }

    public static Collection<Actor> getOptionalActors(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, ActorSelector.class).findActors(context.getSource());
    }


    @Override
    public ActorSelector parse(StringReader reader) throws CommandSyntaxException {
        ActorSelectorParser parser = new ActorSelectorParser(reader);
        return parser.parse();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if(context.getSource() instanceof SharedSuggestionProvider sharedSuggestionProvider) {
            StringReader reader = new StringReader(builder.getInput());

            reader.setCursor(builder.getStart());

            ActorSelectorParser parser = new ActorSelectorParser(reader);

            try {
                parser.parse();
            } catch (CommandSyntaxException ignored) {
            }

            return parser.fillSuggestions(builder, consumer -> SharedSuggestionProvider.suggest(sharedSuggestionProvider.getOnlinePlayerNames(), consumer));
        } else {
            return Suggestions.empty();
        }
    }

    /*
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider sharedsuggestionprovider) {
            StringReader stringreader = new StringReader(builder.getInput());
            stringreader.setCursor(builder.getStart());
            EntitySelectorParser entityselectorparser = new EntitySelectorParser(stringreader, net.neoforged.neoforge.common.CommonHooks.canUseEntitySelectors(sharedsuggestionprovider));

            try {
                entityselectorparser.parse();
            } catch (CommandSyntaxException commandsyntaxexception) {
            }

            return entityselectorparser.fillSuggestions(
                builder,
                p_91457_ -> {
                    Collection<String> collection = sharedsuggestionprovider.getOnlinePlayerNames();
                    Iterable<String> iterable = (Iterable<String>)(this.playersOnly
                        ? collection
                        : Iterables.concat(collection, sharedsuggestionprovider.getSelectedEntities()));
                    SharedSuggestionProvider.suggest(iterable, p_91457_);
                }
            );
        } else {
            return Suggestions.empty();
        }
    }

     */
}
