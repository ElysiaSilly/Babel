package com.elysiasilly.babel.api.dbi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicBlockItem {

    private static final Map<Item, Item> MERGE = new HashMap<>();

    public static boolean mergeItem(Item item) {
        return MERGE.containsKey(item);
    }

    public static Item itemToMergeInto(Item item) {
        return MERGE.get(item);
    }

    private final Item item;
    private final List<Item> merge = new ArrayList<>();
    private final CycleMode mode;
    private final List<DynamicBlockState> entries = new ArrayList<>();

    public static final Codec<DynamicBlockItem> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(DynamicBlockItem::item),
            Codec.list(BuiltInRegistries.ITEM.byNameCodec()).fieldOf("merge").forGetter(DynamicBlockItem::merge),
            StringRepresentable.fromEnum(CycleMode::values).fieldOf("mode").forGetter(DynamicBlockItem::mode),
            Codec.list(DynamicBlockState.CODEC, 1, 32).fieldOf("entries").forGetter(DynamicBlockItem::entries)
    ).apply(builder, DynamicBlockItem::new));

    public DynamicBlockItem(Item item, List<Item> merge, CycleMode mode, List<DynamicBlockState> entries) {
        this.item = item;
        this.mode = mode;

        for(DynamicBlockState entry : entries) {
            if(entry.cost() > item.getDefaultMaxStackSize())
                throw new IllegalStateException(String.format("Cost (%s) cannot be higher than the max stack size (%s)", entry.cost(), item.getDefaultMaxStackSize()));
            if(entry.cost() <= 0)
                throw new IllegalStateException(String.format("Cost (%s) cannot be equal or lower than 0", entry.cost()));
        }

        for(Item itemToMerge : merge) {
            MERGE.put(itemToMerge, item);
        }

        this.entries.addAll(entries);
    }

    public Item item() {
        return this.item;
    }

    public CycleMode mode() {
        return this.mode;
    }

    public List<DynamicBlockState> entries() {
        return this.entries;
    }

    public List<Item> merge() {
        return this.merge;
    }
}
