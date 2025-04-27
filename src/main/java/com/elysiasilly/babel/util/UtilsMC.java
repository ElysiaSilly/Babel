package com.elysiasilly.babel.util;

import com.elysiasilly.babel.util.conversions.ConversionsVector;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class UtilsMC {

    public static void addParticle(Level level, ParticleOptions particle, Vec3 position, Vec3 velocity) {
        level.addParticle(particle, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
    }

    public static void addParticle(Level level, ParticleOptions particle, Vec3 position) {
        addParticle(level, particle, position, Vec3.ZERO);
    }

    // horrible lmao
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
                Vec3 point = start.lerp(UtilsMath.offset(start, direction, distance), i);
                if(level.getBlockState(ConversionsVector.toBlockPos(point)).isSolid()) break; // todo
                points.add(point);
            }

            //Minecraft.getInstance().hitResult

            return points;
        }
    }
}
