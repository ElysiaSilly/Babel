package com.elysiasilly.babel.common.item.cycleable;

import net.minecraft.world.level.block.state.properties.Property;

public record PropertyValuePair<P extends Comparable<P>, V extends P>(Property<P> property, V value) {}
