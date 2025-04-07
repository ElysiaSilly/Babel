package com.elysiasilly.babel.api.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BabelEntityBlock extends Block implements EntityBlock {

    public enum Tick { ONLY_CLIENT, ONLY_SERVER, BOTH, NONE }

    public BabelEntityBlock(Properties properties) {
        super(properties);
    }

    private boolean tickClient(Tick tick) {
        return tick.equals(Tick.ONLY_CLIENT) || tick.equals(Tick.BOTH);
    }

    private boolean tickServer(Tick tick) {
        return tick.equals(Tick.ONLY_SERVER) || tick.equals(Tick.BOTH);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public abstract @NotNull BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    public abstract Tick tick(Level level, BlockPos pos, BlockState state, BabelBE be);

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
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
