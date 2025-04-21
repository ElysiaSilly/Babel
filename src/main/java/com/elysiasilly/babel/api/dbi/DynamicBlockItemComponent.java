package com.elysiasilly.babel.api.dbi;

import com.elysiasilly.babel.core.registry.BBComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public record DynamicBlockItemComponent(int index, List<DynamicBlockState> entries) {

    public static final Supplier<DataComponentType<DynamicBlockItemComponent>> COMPONENT = BBComponents.DYNAMIC_BLOCK_ITEM;

    public static final Codec<DynamicBlockItemComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("index").forGetter(DynamicBlockItemComponent::index),
            Codec.list(DynamicBlockState.CODEC, 1, 32).fieldOf("entries").forGetter(DynamicBlockItemComponent::entries)
    ).apply(builder, DynamicBlockItemComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DynamicBlockItemComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, DynamicBlockItemComponent::index,
            DynamicBlockState.STREAM_CODEC.apply(ByteBufCodecs.list()), DynamicBlockItemComponent::entries,
            DynamicBlockItemComponent::new
    );

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DynamicBlockItemComponent;
    }

    public Block block() {
        return block(index());
    }

    public Block block(int index) {
        return entry(index).block();
    }

    public BlockState state(int index, @Nullable BlockPlaceContext context) {
        return entry(index).state(context);
    }

    public BlockState state(int index) {
        return entry(index).state();
    }

    public BlockState state() {
        return state(index());
    }

    public BlockState state(@Nullable BlockPlaceContext context) {
        return entry().state(context);
    }

    public static void cycle(ItemStack stack, int index) {
        if(stack.has(COMPONENT)) {
            DynamicBlockItemComponent component = stack.get(COMPONENT);
            stack.set(COMPONENT, new DynamicBlockItemComponent(index, component.entries()));
        }
    }

    public static boolean cycle(ItemStack stack) {
        if(stack.has(COMPONENT)) {
            DynamicBlockItemComponent component = stack.get(COMPONENT);
            stack.set(COMPONENT, new DynamicBlockItemComponent(component.nextIndex(component.index()), component.entries()));
        }

        return true;
    }

    public int nextIndex(int index) {
        return index >= size() - 1 ? 0 : index + 1;
    }

    public int index() {
        return this.index;
    }

    public List<DynamicBlockState> entries() {
        return this.entries;
    }

    public DynamicBlockState entry() {
        return entry(index());
    }

    public DynamicBlockState entry(int index) {
        return entries().get(index);
    }

    public int size() {
        return entries().size();
    }
}
