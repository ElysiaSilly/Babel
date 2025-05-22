package com.elysiasilly.babel.core.datagen.data;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.util.misc.ImmutablePair;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BBLocals extends LanguageProvider {

    private final List<ImmutablePair<DeferredBlock<?>, String>> entries;

    public BBLocals(PackOutput output, String locale, List<ImmutablePair<DeferredBlock<?>, String>> entries) {
        super(output, Babel.MODID, locale);
        this.entries = entries;
    }

    @Override
    protected void addTranslations() {
        for(ImmutablePair<DeferredBlock<?>, String> entry : entries) {
            addBlock(entry.first(), entry.second() == null ? auto(entry.first()) : entry.second());
        }
    }


    private static String auto(DeferredBlock<?> block) {
        String name = block.getId().getPath().replaceAll("_", " ");

        // stolen from the interwebs i have no clue what this means
        return Arrays.stream(name.split("\\s+"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
