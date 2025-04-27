package com.elysiasilly.babel.util;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import org.joml.Vector3d;

public class UtilsFormatting {

    public static String vector3d(Vector3d value) {
        return String.format("[%s, %s, %s]", value.x, value.y, value.z);
    }

    public static String chunkPos(ChunkPos value) {
        return String.format("[%s, %s]", value.x, value.z);
    }

    public static String sectionPos(SectionPos value) {
        return String.format("[%s, %s, %s]", value.x(), value.y(), value.z());
    }
}
