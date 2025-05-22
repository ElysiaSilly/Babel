package com.elysiasilly.babel.api.registry.entry.block;

import com.elysiasilly.babel.api.registry.entry.block.assets.ItemModelBuilder;
import com.elysiasilly.babel.api.registry.entry.block.assets.ModelBuilder;
import com.elysiasilly.babel.api.registry.entry.block.assets.StateBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BlockAssetBuilder<B extends Block> {

    public final DeferredBlock<B> block;

    public BlockAssetBuilder(DeferredBlock<B> block) {
        this.block = block;
    }

    /// LOCALIZATION

    private final Map<String, String> localizations = new HashMap<>();

    // will auto generate a localization from the block id if null is passed in
    public BlockAssetBuilder<B> localization(String lang, @Nullable String entry) {
        this.localizations.put(lang, entry); return this;
    }

    public BlockAssetBuilder<B> localization(String lang) {
        return localization(lang, null);
    }

    public Map<String, String> localizations() {
        return new HashMap<>(this.localizations);
    }

    /// MODELS

    private final Map<String, ModelBuilder> models = new HashMap<>();
    private StateBuilder state;
    private ItemModelBuilder itemModel;


    public BlockAssetBuilder<B> item(ItemModelBuilder builder) {
        this.itemModel = builder; return this;
    }

    public BlockAssetBuilder<B> model(String key, ModelBuilder builder) {
        this.models.put(key, builder); return this;
    }

    public BlockAssetBuilder<B> state(StateBuilder state) {
        this.state = state; return this;
    }

    ///

    public static ModelBuilder model() {
        return new ModelBuilder();
    }

    public static ItemModelBuilder.Parented parentedItem(String model) {
        return new ItemModelBuilder.Parented(model);
    }

    public static ItemModelBuilder.Sprite spriteItem(ResourceLocation loc) {
        return new ItemModelBuilder.Sprite(loc);
    }

    public static ItemModelBuilder.Sprite spriteItem(String loc) {
        return new ItemModelBuilder.Sprite(loc);
    }

    public static StateBuilder.Variant variantState() {
        return new StateBuilder.Variant();
    }

    public static StateBuilder.Variant.Entry variantStateEntry(String model) {
        return new StateBuilder.Variant.Entry(model);
    }

    ///

    public void build(BlockStateProvider provider) {
        this.models.forEach((key, model) -> model.build(provider, this.block));
        this.state.build(provider, this.block, this.models);
        this.itemModel.build(provider, this.block, this.models);
    }
}
