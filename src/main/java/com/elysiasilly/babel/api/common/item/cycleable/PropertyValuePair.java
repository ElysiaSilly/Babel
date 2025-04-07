package com.elysiasilly.babel.api.common.item.cycleable;

import net.minecraft.world.level.block.state.properties.Property;

import java.util.Objects;

public record PropertyValuePair<P extends Comparable<P>, V extends P>(Property<P> property, V value) {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PropertyValuePair<?,?> other && this.property.equals(other.property) && this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, value);
    }
}
