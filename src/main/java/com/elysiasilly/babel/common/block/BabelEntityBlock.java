package com.elysiasilly.babel.common.block;

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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public abstract @NotNull BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);

    public abstract Tick shouldTick(Level level, BlockState state);

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        Tick tick = shouldTick(level, state);

        return tick.equals(Tick.NONE) ? null : (lvl, pos, st, blockEntity) -> {
            if(blockEntity instanceof BabelBE be) {
                if(level.isClientSide) {
                    if(tick.equals(Tick.BOTH) || tick.equals(Tick.ONLY_CLIENT)) be.tickClient();
                } else {
                    if(tick.equals(Tick.BOTH) || tick.equals(Tick.ONLY_SERVER)) be.tickServer();
                }
            }
        };
    }
}
