package com.elysiasilly.babel.api.registry.entry.block.assets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;

import static net.neoforged.neoforge.client.model.generators.ModelProvider.ITEM_FOLDER;

public abstract class ItemModelBuilder {

    protected ItemModelBuilder() {}


    public abstract <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> block, Map<String, ModelBuilder> models);

    public static class Parented extends ItemModelBuilder {

        private final String model;

        public Parented(String model) {
            this.model = model;
        }

        @Override
        public <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> block, Map<String, ModelBuilder> models) {
            provider.itemModels().getBuilder(block.getId().getPath()).parent(models.get(this.model).model().orElseThrow(() -> new RuntimeException("Tried accessing a model that hasn't been built yet.")));
        }
    }

    public static class Sprite extends ItemModelBuilder {

        private final ResourceLocation location;

        public Sprite(String loc) {
            this(ResourceLocation.parse(loc));
        }

        public Sprite(ResourceLocation loc) {
            this.location = loc;
        }

        @Override
        public <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> block, Map<String, ModelBuilder> models) {
            provider.itemModels().getBuilder(block.getId().toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", parse(this.location));
        }

        private static ResourceLocation parse(ResourceLocation loc) {
            return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), String.format("%s/%s", ITEM_FOLDER, loc.getPath()));
        }
    }
}
