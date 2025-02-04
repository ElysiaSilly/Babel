package com.elysiasilly.babel.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class MCUtil {

    public static class item {

        public static boolean isEmpty(ItemStack...stacks) {
            for(ItemStack stack : stacks) {
                if(stack == null) return true;
                if(stack.isEmpty()) return true;
            }
            return false;
        }

        public static boolean hasComponent(ItemStack stack, Supplier<? extends DataComponentType<?>> component) {
            return stack.has(component) ? stack.get(component) != null : false;
        }

        public static ItemStack splitWithCheck(ItemStack stack, int amount, Player player) {
            if(isEmpty(stack)) return ItemStack.EMPTY;
            if(player.hasInfiniteMaterials()) return stack.copy();
            return stack.split(amount);
        }

        public static void serialize(String id, ItemStack stack, CompoundTag compoundTag, HolderLookup.Provider registries) {
            if(isEmpty(stack)) return;
            compoundTag.put(id, stack.save(registries, compoundTag));
        }

        public static ItemStack deserialize(String id, CompoundTag compoundTag, HolderLookup.Provider registries) {
            Tag tag = compoundTag.get(id);
            if(tag == null) return ItemStack.EMPTY;
            Optional<ItemStack> stack = ItemStack.parse(registries, tag);
            return stack.orElse(ItemStack.EMPTY);
        }

        public static void increment(ItemStack stack, int increment) {
            int count = stack.getCount() + increment;
            if(count > stack.getMaxStackSize() || count < 1) return;
            stack.grow(increment);
        }
    }

    public static class raycast {

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
                Vec3 point = start.lerp(MathUtil.vectors.offset(start, direction, distance), i);
                if(level.getBlockState(Conversions.vector.blockPos(point)).isSolid()) break; // todo
                points.add(point);
            }

            return points;
        }
    }

    public static class particle {

        public static void add(Level level, ParticleOptions particle, Vec3 position, Vec3 velocity) {
            level.addParticle(particle, position.x, position.y, position.z, velocity.x, velocity.y, velocity.z);
        }

        public static void add(Level level, ParticleOptions particle, Vec3 position) {
            add(level, particle, position, Vec3.ZERO);
        }
    }

    public static class blockPos {

        public static boolean isNeighbour(BlockPos pos, BlockPos potentialNeighbourPos) {
            for(Direction dir : Direction.values()) if(pos.relative(dir).equals(potentialNeighbourPos)) return true;
            return false;
        }
    }
}
