package com.elysiasilly.babel.common.item.cycleable;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.List;


public class DefinedBlockState {

    private final Block block;
    private boolean context = false;
    private final int cost;

    private final List<PropertyValuePair<?, ?>> properties =  new ArrayList<>();

    public DefinedBlockState(Block block, int cost) {
        this.block = block;
        this.cost = cost;
    }

    public <P extends Comparable<P>, V extends P> DefinedBlockState setProperty(Property<P> property, V value) {
        PropertyValuePair<?, ?> pair = new PropertyValuePair<>(property, value);
        if(!this.properties.contains(pair)) this.properties.add(pair);
        return this;
    }

    public DefinedBlockState placementContext() {
        this.context = true; return this;
    }

    public Block getBlock() {
        return this.block;
    }

    public int getCost() {
        return this.cost;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public BlockState get(BlockPlaceContext context) {

        BlockState state = this.context ? getBlock().getStateForPlacement(context) : getBlock().defaultBlockState();

        if(state != null) for(PropertyValuePair entry : this.properties) state = state.setValue(entry.property(), entry.value());

        return state;
    }
}
