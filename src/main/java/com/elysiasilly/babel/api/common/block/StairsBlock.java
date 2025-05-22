package com.elysiasilly.babel.api.common.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;

public class StairsBlock extends StairBlock {

    public StairsBlock(Properties properties) {
        super(Blocks.AIR.defaultBlockState(), properties);
    }

    @Override
    public float getExplosionResistance() {
        return this.explosionResistance;
    }
}
