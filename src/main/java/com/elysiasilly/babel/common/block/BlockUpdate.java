package com.elysiasilly.babel.common.block;

import net.minecraft.world.level.block.Block;

public enum BlockUpdate {
    NEIGHBOURS      (Block.UPDATE_NEIGHBORS),
    CLIENTS         (Block.UPDATE_CLIENTS),
    INVISIBLE       (Block.UPDATE_INVISIBLE),
    IMMEDIATE       (Block.UPDATE_IMMEDIATE),
    KNOWN_SHAPE     (Block.UPDATE_KNOWN_SHAPE),
    SUPPRESS_DROPS  (Block.UPDATE_SUPPRESS_DROPS),
    MOVE_BY_PISTON  (Block.UPDATE_MOVE_BY_PISTON),
    NONE            (Block.UPDATE_NONE),
    ALL             (Block.UPDATE_ALL),
    ALL_IMMEDIATE   (Block.UPDATE_IMMEDIATE),
    UPDATE_LIMIT    (Block.UPDATE_LIMIT);

    final int value;

    BlockUpdate(int value) {
        this.value = value;
    }
}
