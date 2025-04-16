package com.elysiasilly.babel.api.common.item.cycleable;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.HashMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PredefinedBlockState {

    private final Block block;
    private boolean context = false;
    private int cost = 1;

    private final HashMap<Property<?>, PropertyValuePair<?, ?>> properties =  new HashMap<>();

    private BlockState computedState;

    public PredefinedBlockState(Block block) {
        this.block = block;
    }

    public <P extends Comparable<P>, V extends P> PredefinedBlockState set(Property<P> property, V value) {
        this.properties.putIfAbsent(property, new PropertyValuePair<>(property, value)); return this;
    }

    public PredefinedBlockState placementContext() {
        this.context = true; return this;
    }

    public PredefinedBlockState cost(int cost) {
        this.cost = cost; return this;
    }

    public Block block() {
        return this.block;
    }

    public int cost() {
        return this.cost;
    }

    public BlockState get() {
        if(computedState == null) {
            BlockState state = block().defaultBlockState();

            for(PropertyValuePair entry : this.properties.values()) {
                state = state.setValue(entry.property(), entry.value());
            }

            this.computedState = state;
            return state;
        } else {
            return this.computedState;
        }
    }

    public BlockState get(BlockPlaceContext context) {
        BlockState state = this.context ? block().getStateForPlacement(context) : block().defaultBlockState();

        if(state != null) {
            for(PropertyValuePair entry : this.properties.values()) {
                state = state.setValue(entry.property(), entry.value());
            }
        }

        return state;
    }
}
