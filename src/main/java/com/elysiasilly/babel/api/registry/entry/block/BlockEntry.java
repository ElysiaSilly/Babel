package com.elysiasilly.babel.api.registry.entry.block;

import com.elysiasilly.babel.api.registry.Registrar;
import com.elysiasilly.babel.core.datagen.BBProviders;
import com.elysiasilly.babel.core.datagen.data.BBModels;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntry<B extends Block> implements ItemLike {

    private final DeferredBlock<B> block;

    public BlockEntry(DeferredBlock<B> block) {
        this.block = block;
    }

    public B block() {
        return this.block.get();
    }

    @Override
    public @NotNull Item asItem() {
        return this.block.asItem();
    }

    public static class Builder<B extends Block> {

        private final Registrar registrar;

        private final DeferredBlock<B> block;

        private BlockAssetBuilder<B> assetBuilder = null;
        private BlockDataBuilder<B> dataBuilder = null;

        private boolean item = true;

        private Builder(String id, Supplier<B> block, Registrar registrar) {
            this.registrar = registrar;
            this.block = registrar.block(id, block);
        }

        public Builder<B> data(Function<DeferredBlock<B>, BlockDataBuilder<B>> builder) {
            this.dataBuilder = builder.apply(this.block); return this;
        }

        public Builder<B> assets(Function<DeferredBlock<B>, BlockAssetBuilder<B>> builder) {
            this.assetBuilder = builder.apply(this.block); return this;
        }

        public Builder<B> assets(BiFunction<DeferredBlock<B>, BlockAssetBuilder<B>, BlockAssetBuilder<B>> builder) {
            builder.apply(this.block, this.assetBuilder); return this;
        }

        public BlockAssetBuilder<B> assets() {
            return this.assetBuilder;
        }

        public BlockDataBuilder<B> data() {
            return this.dataBuilder;
        }

        public Builder<B> withoutItem() {
            this.item = false; return this;
        }

        public static <B extends Block> Builder<B> of(String id, Supplier<B> block, Registrar registrar) {
            return new Builder<>(id, block, registrar);
        }

        private DeferredBlock<B> bb;

        public BlockEntry<B> build() {


            if(this.item) this.registrar.blockItems(this.block);

            if(this.assetBuilder != null) {

                if(!this.assetBuilder.localizations().isEmpty()) {
                    BBProviders.localizations.put(this.block, this.assetBuilder.localizations());
                }

                if(this.assetBuilder != null) {
                    BBProviders.assets.add(this.assetBuilder);
                }

                BBModels.assets.add(this.assetBuilder);
            }
            return new BlockEntry<>(this.block);
        }
    }
}
