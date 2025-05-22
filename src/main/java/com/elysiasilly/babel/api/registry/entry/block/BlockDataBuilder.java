package com.elysiasilly.babel.api.registry.entry.block;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockDataBuilder<B extends Block> {


    public static class Tag {
        private final List<TagKey<Block>> tags = new ArrayList<>();

        @SafeVarargs
        public final Tag add(TagKey<Block>... tags) {
            this.tags.addAll(List.of(tags)); return this;
        }

        public List<TagKey<Block>> tags() {
            return new ArrayList<>(this.tags);
        }
    }
}
