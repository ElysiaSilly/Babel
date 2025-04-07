package com.elysiasilly.babel.api.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BabelEvents {

    public static class BlockBurn extends BlockEvent {

        public BlockBurn(LevelAccessor level, BlockPos pos, BlockState state) {
            super(level, pos, state);
        }
    }
}
