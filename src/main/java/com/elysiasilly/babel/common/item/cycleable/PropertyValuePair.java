package com.elysiasilly.babel.common.item.cycleable;

import net.minecraft.world.level.block.state.properties.Property;

public class PropertyValuePair<P extends Comparable<P>, V extends P> {

    final Property<P> property;
    final V value;

    public PropertyValuePair(Property<P> property, V value) {
        this.property = property;
        this.value = value;
    }
}
