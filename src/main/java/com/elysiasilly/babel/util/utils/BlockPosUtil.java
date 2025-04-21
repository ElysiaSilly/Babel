package com.elysiasilly.babel.util.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BlockPosUtil {

    public static boolean isNeighbour(BlockPos pos, BlockPos potentialNeighbourPos) {
        for(Direction dir : Direction.values()) if(pos.relative(dir).equals(potentialNeighbourPos)) return true;
        return false;
    }
}
