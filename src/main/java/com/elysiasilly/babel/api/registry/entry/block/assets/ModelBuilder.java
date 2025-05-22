package com.elysiasilly.babel.api.registry.entry.block.assets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.neoforged.neoforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

public class ModelBuilder {

    private ModelFile model = null;

    private final Map<String, ResourceLocation> textures = new HashMap<>();

    private ResourceLocation output, parent, renderType;

    private boolean ao = true;

    public ModelBuilder texture(String loc, String key) {
        this.textures.put(key, parse(loc)); return this;
    }

    public ModelBuilder texture(ResourceLocation loc, String key) {
        this.textures.put(key, loc); return this;
    }

    public ModelBuilder textures(String loc, String...keys) {
        return textures(parse(loc), keys);
    }

    public ModelBuilder textures(ResourceLocation loc, String...keys) {
        for(String key : keys) {
            this.textures.put(key, loc);
        }
        return this;
    }

    public ModelBuilder output(ResourceLocation loc) {
        this.output = loc; return this;
    }

    public ModelBuilder output(String loc) {
        this.output = parse(loc); return this;
    }

    public ModelBuilder parent(ResourceLocation loc) {
        this.parent = loc; return this;
    }

    public ModelBuilder parent(String loc) {
        this.parent = parse(loc); return this;
    }

    public ModelBuilder renderType(ResourceLocation loc) {
        this.renderType = loc; return this;
    }

    public ModelBuilder renderType(String loc) {
        this.renderType = parse(loc); return this;
    }

    public ModelBuilder noAo() {
        this.ao = false; return this;
    }

    public Optional<ModelFile> model() {
        return Optional.ofNullable(this.model);
    }

    public <B extends Block> void build(BlockStateProvider provider, DeferredBlock<B> block) {

        BlockModelBuilder builder = provider.models().withExistingParent(
                this.output == null ? block.getId().toString() : loc(this.output).toString(),
                this.parent == null ? parse("cube_all") : loc(this.parent)
        );

        if(this.renderType != null) builder.renderType(this.renderType);
        if(!this.ao) builder.ao(false);

        for(Map.Entry<String, ResourceLocation> entry : this.textures.entrySet()) {
            builder.texture(entry.getKey(), loc(entry.getValue()));
        }

        this.model = builder;
    }

    private static ResourceLocation loc(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, String.format("%s/%s", BLOCK_FOLDER, path));
    }

    private static ResourceLocation loc(ResourceLocation loc) {
        return loc(loc.getNamespace(), loc.getPath());
    }

    private static ResourceLocation parse(String loc) {
        return ResourceLocation.parse(loc);
    }
}
