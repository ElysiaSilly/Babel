package com.elysiasilly.babel.api.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BabelEntityBlock extends EntityBlock {

    enum Tick { ONLY_CLIENT, ONLY_SERVER, BOTH, NONE }

    static boolean tickClient(Tick tick) {
        return tick.equals(Tick.ONLY_CLIENT) || tick.equals(Tick.BOTH);
    }

    static boolean tickServer(Tick tick) {
        return tick.equals(Tick.ONLY_SERVER) || tick.equals(Tick.BOTH);
    }

    Tick tick(Level level, BlockPos pos, BlockState state, BabelBE be);

    @Override @Nullable
    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, st, blockEntity) -> {
            if(blockEntity instanceof BabelBE be) {
                Tick tick = tick(lvl, pos, st, be);

                if(lvl.isClientSide()) {
                    if(tickClient(tick)) be.tickClient();
                } else {
                    if(tickServer(tick)) be.tickServer();
                }
            }
        };
    }
}
