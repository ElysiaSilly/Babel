package com.elysiasilly.babel.api.registry.entry.block.assets;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class StateBuilder {

    private StateBuilder() {}

    public abstract <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> block, Map<String, ModelBuilder> models);

    ///

    public static class Variant extends StateBuilder {

        private final Map<Function<BlockState, Boolean>, Function<BlockState, Entry>> entries = new HashMap<>();

        @Override
        public <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> deferredBlock, Map<String, ModelBuilder> models) {
            B block = deferredBlock.get();

            outer : provider.getVariantBuilder(block).forAllStates(state -> {
                ConfiguredModel[] configuredModels = new ConfiguredModel[]{};

                for(Map.Entry<Function<BlockState, Boolean>, Function<BlockState, Entry>> entry : this.entries.entrySet()) {
                    if(entry.getKey().apply(state)) {
                        configuredModels = ArrayUtils.addAll(configuredModels, entry.getValue().apply(state).build(models));
                    } else {
                        //continue outer;
                    }
                }

                return configuredModels;
            });
        }

        public Variant add(Function<BlockState, Boolean> function, Function<BlockState, Entry> model) {
            this.entries.put(function, model); return this;
        }

        public static class Entry {

            private final String key;
            private Integer rotX, rotY, weight;
            private boolean uvLock = false;

            public Entry(String key) {
                this.key = key;
            }

            public Entry uvLock(boolean bool) {
                this.uvLock = bool; return this;
            }

            public Entry rotX(int rot) {
                this.rotX = rot; return this;
            }

            public Entry rotY(int rot) {
                this.rotY = rot; return this;
            }

            public Entry weight(int weight) {
                this.weight = weight; return this;
            }

            public ConfiguredModel[] build(Map<String, ModelBuilder> models) {

                ConfiguredModel.Builder<?> model = ConfiguredModel.builder().modelFile(models.get(this.key).model().orElseThrow(() -> new RuntimeException("Tried accessing a model that hasn't been built yet.")));

                if(this.uvLock) model.uvLock(true);

                if(this.rotX != null) model.rotationX(this.rotX);
                if(this.rotY != null) model.rotationY(this.rotY);

                if(this.weight != null) model.weight(this.weight);

                return model.build();
            }
        }
    }

    ///

    public static class MultiPart extends StateBuilder {

        @Override
        public <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> block, Map<String, ModelBuilder> models) {

        }
    }
}
