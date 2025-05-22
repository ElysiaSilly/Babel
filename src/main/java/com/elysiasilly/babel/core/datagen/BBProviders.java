package com.elysiasilly.babel.core.datagen;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.registry.entry.block.BlockAssetBuilder;
import com.elysiasilly.babel.core.datagen.data.BBLocals;
import com.elysiasilly.babel.core.datagen.data.BBModels;
import com.elysiasilly.babel.util.misc.ImmutablePair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BBProviders {

    public static final Map<DeferredBlock<?>, Map<String, String>> localizations = new HashMap<>();

    public static final List<BlockAssetBuilder<?>> assets = new ArrayList<>();


    @SubscribeEvent
    private static void register(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        /// MODELS

        generator.addProvider(event.includeClient(), new BBModels(packOutput, fileHelper));

        /// LOCALIZATION

        // lang, <block, name>
        Map<String, List<ImmutablePair<DeferredBlock<?>, String>>> localizations = new HashMap<>();


        for(Map.Entry<DeferredBlock<?>, Map<String, String>> entry : BBProviders.localizations.entrySet()) {

            DeferredBlock<?> block = entry.getKey();

            for(Map.Entry<String, String> localization : entry.getValue().entrySet()) {

                String lang = localization.getKey();
                String name = localization.getValue();

                localizations.computeIfAbsent(lang, i -> new ArrayList<>()).add(new ImmutablePair<>(block, name));
            }
        }

        for(Map.Entry<String, List<ImmutablePair<DeferredBlock<?>, String>>> entry : localizations.entrySet()) {
            generator.addProvider(event.includeClient(), new BBLocals(packOutput, entry.getKey(), entry.getValue()));
        }

        ///
    }
}
