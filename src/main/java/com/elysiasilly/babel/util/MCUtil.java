package com.elysiasilly.babel.util;

import com.elysiasilly.babel.util.conversions.VectorConversions;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MCUtil {



    public static class Raycast {

        public static final float GOOD_ENOUGH = .005f;

        public static List<Vec3> shittyRayCast(Player player, float precision) {
            return shittyRayCast(player, (float) player.blockInteractionRange(), precision);
        }

        public static List<Vec3> shittyRayCast(Entity entity, float range, float precision) {
            return shittyRayCast(entity.getEyePosition(), entity.getLookAngle(), range, precision, entity.level());
        }

        @SuppressWarnings("deprecation")
        public static List<Vec3> shittyRayCast(Vec3 start, Vec3 direction, float distance, float precision, Level level) {
            List<Vec3> points = new ArrayList<>();

            for(float i = 0; i <= distance; i+= precision) {
                Vec3 point = start.lerp(MathUtil.Vec.offset(start, direction, distance), i);
                if(level.getBlockState(VectorConversions.toBlockPos(point)).isSolid()) break; // todo
                points.add(point);
            }

            //Minecraft.getInstance().hitResult

            return points;
        }
    }

    public static class Particle {

        public static void add(Level level, ParticleOptions particle, Vec3 position, Vec3 velocity) {
            level.addParticle(particle, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
        }

        public static void add(Level level, ParticleOptions particle, Vec3 position) {
            add(level, particle, position, Vec3.ZERO);
        }
    }

    public static class BlockPos {

        public static boolean isNeighbour(net.minecraft.core.BlockPos pos, net.minecraft.core.BlockPos potentialNeighbourPos) {
            for(Direction dir : Direction.values()) if(pos.relative(dir).equals(potentialNeighbourPos)) return true;
            return false;
        }
    }
}
