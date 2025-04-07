package com.elysiasilly.babel.api.common.block.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ITransformOnBurnBlock {

    /// called when a block is burnt
    void onBurn(Level level, BlockPos pos, Direction face, BlockState state);
}
