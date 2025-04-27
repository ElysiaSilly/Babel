package com.elysiasilly.babel.api.theatre.actor.command;


import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.core.BBRegistries;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActorSelectorParser {


    private final StringReader reader;

    private ActorType<?> actorType;
    private ActorSelector.Type type;
    private int maxResults;

    public ActorSelectorParser(StringReader reader) {
        this.reader = reader;
    }

    private StringReader reader() {
        return this.reader;
    }

    public ActorSelector parse() throws CommandSyntaxException {

        if(reader().canRead() && reader().peek() == '@') {
            parseSelector();
        }

        return new ActorSelector(null, null, this.type);
    }

    private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions = SUGGEST_NOTHING;
    public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING = (p_121363_, p_121364_) -> p_121363_.buildFuture();


    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        return this.suggestions.apply(builder.createOffset(this.reader.getCursor()), consumer);
    }

    private void parseSelector() throws CommandSyntaxException {

        char c = reader().peek();

        switch(c){
            case 'a' -> {
                this.maxResults = Integer.MAX_VALUE;
                this.type = ActorSelector.Type.ALL;
                //all
            }
            case 'r' -> {
                this.maxResults = 1;
                this.type = ActorSelector.Type.RANDOM;
                //random
            }
            case 'n' -> {
                this.maxResults = 1;
                this.type = ActorSelector.Type.NEAREST;
                //nearest
            }
            case 'h' -> {
                this.maxResults = 1;
                this.type = ActorSelector.Type.HIGHLIGHTED;
                //highlighted
            }
        }

        this.suggestions = this::suggestOpenOptions;
        if (this.reader.canRead() && this.reader.peek() == '[') {
            this.reader.skip();
            this.suggestions = this::suggestOptionsKeyOrClose;
            this.parseOptions();
        }
    }

    private CompletableFuture<Suggestions> suggestOptionsNextOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        builder.suggest(String.valueOf(','));
        builder.suggest(String.valueOf(']'));
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestOptionsKey(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        suggestNames(this, builder);
        return builder.buildFuture();
    }

    public void parseOptions() throws CommandSyntaxException {
        this.suggestions = this::suggestOptionsKey;
        this.reader.skipWhitespace();

        while (this.reader.canRead() && this.reader.peek() != ']') {
            this.reader.skipWhitespace();
            int i = this.reader.getCursor();
            String s = this.reader.readString();
            Modifier entityselectoroptions$modifier = get(this, s, i);
            this.reader.skipWhitespace();
            if (!this.reader.canRead() || this.reader.peek() != '=') {
                this.reader.setCursor(i);
                //throw ERROR_EXPECTED_OPTION_VALUE.createWithContext(this.reader, s);
            }

            this.reader.skip();
            this.reader.skipWhitespace();
            this.suggestions = SUGGEST_NOTHING;
            entityselectoroptions$modifier.handle(this);
            this.reader.skipWhitespace();
            this.suggestions = this::suggestOptionsNextOrClose;
            if (this.reader.canRead()) {
                if (this.reader.peek() != ',') {
                    if (this.reader.peek() != ']') {
                        //throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
                    }
                    break;
                }

                this.reader.skip();
                this.suggestions = this::suggestOptionsKey;
            }
        }

        if (this.reader.canRead()) {
            this.reader.skip();
            this.suggestions = SUGGEST_NOTHING;
        } else {
            //throw ERROR_EXPECTED_END_OF_OPTIONS.createWithContext(this.reader);
        }
    }

    public static Modifier get(ActorSelectorParser parser, String id, int cursor) throws CommandSyntaxException {
        Option entityselectoroptions$option = OPTIONS.get(id);
        if (entityselectoroptions$option != null) {
            if (entityselectoroptions$option.canUse.test(parser)) {
                return entityselectoroptions$option.modifier;
            } else {
                //throw ERROR_INAPPLICABLE_OPTION.createWithContext(parser.getReader(), id);
            }
        } else {
            parser.reader().setCursor(cursor);
            //throw ERROR_UNKNOWN_OPTION.createWithContext(parser.getReader(), id);
        }

        return null;
    }

    public boolean isTypeLimited() {
        return type != null;
    }

    private CompletableFuture<Suggestions> suggestOpenOptions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        builder.suggest(String.valueOf('['));
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        builder.suggest(String.valueOf(']'));
        suggestNames(this, builder);
        return builder.buildFuture();
    }

    public void setSuggestions(BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestionHandler) {
        this.suggestions = suggestionHandler;
    }



    ///

    static record Option(Modifier modifier, Predicate<ActorSelectorParser> canUse, Component description) { }

    public interface Modifier {
        void handle(ActorSelectorParser parser) throws CommandSyntaxException;
    }

    private static final Map<String, Option> OPTIONS = Maps.newHashMap();

    public static void register(String id, Modifier handler, Predicate<ActorSelectorParser> predicate, Component tooltip) {
        OPTIONS.put(id, new Option(handler, predicate, tooltip));
    }

    public static final DynamicCommandExceptionType ERROR_ENTITY_TYPE_INVALID = new DynamicCommandExceptionType(
            p_304137_ -> Component.translatableEscape("argument.entity.options.type.invalid", p_304137_)
    );

    public static void bootstrap() {
        if(OPTIONS.isEmpty()) {
            register("type", i -> i.setSuggestions(
                    (builder, suggestion) -> {
                        SharedSuggestionProvider.suggestResource(BBRegistries.ACTOR_TYPE.get().keySet(), builder, String.valueOf('!'));
                        return builder.buildFuture();
                    }),
                    i -> !i.isTypeLimited(),
                    Component.translatable("argument.entity.options.type.description")
            );

            register(
                    "type",
                    i -> {
                        i.setSuggestions(
                                (builder, suggestion) -> {
                                    SharedSuggestionProvider.suggestResource(BBRegistries.ACTOR_TYPE.get().keySet(), builder, String.valueOf('!'));
                                    return builder.buildFuture();
                                }
                        );

                        ResourceLocation resourcelocation = ResourceLocation.read(i.reader());


                        ActorType<?> type = BBRegistries.ACTOR_TYPE.get().getOptional(resourcelocation).orElseThrow(() -> {
                            i.reader().setCursor(i.reader().getCursor());
                            return ERROR_ENTITY_TYPE_INVALID.createWithContext(i.reader(), resourcelocation.toString());
                        });

                        i.actorType = type;
                    },
                    i -> !i.isTypeLimited(),
                    Component.translatable("argument.entity.options.type.description")
            );
        }
    }


    public static void suggestNames(ActorSelectorParser parser, SuggestionsBuilder builder) {
        String s = builder.getRemaining().toLowerCase(Locale.ROOT);

        for (Map.Entry<String, Option> entry : OPTIONS.entrySet()) {
            if (entry.getValue().canUse.test(parser) && entry.getKey().toLowerCase(Locale.ROOT).startsWith(s)) {
                builder.suggest(entry.getKey() + "=", entry.getValue().description);
            }
        }
    }
}
