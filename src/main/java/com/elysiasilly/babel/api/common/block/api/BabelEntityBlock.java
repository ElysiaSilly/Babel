package com.elysiasilly.babel.api.common.block.api;

import com.elysiasilly.babel.api.common.be.BabelBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BabelEntityBlock<B extends BabelBE> extends EntityBlock {

    @Override
    @Nullable B newBlockEntity(BlockPos pos, BlockState state);

    boolean tick(Level level, BlockState state);

    @Override @Nullable
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return tick(level, state) ? (lvl, pos, st, be) -> { if(be instanceof BabelBE bbbe) bbbe.tick(); } : null;
    }
}
