package com.elysiasilly.babel.impl.common.actor;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.scene.SceneType;
import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.core.registry.BBScenes;
import com.elysiasilly.babel.util.utils.SerializationUtil;
import com.elysiasilly.babel.util.utils.VectorUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.UUID;

public class TestActor extends Actor {

    public int rotation = 0;

    public Vec3 startPos = Vec3.ZERO;
    public Vec3 endPos = Vec3.ZERO;

    public Block block;
    public FluidStack fluid;

    public TestActor(UUID uuid) {
        super(uuid);
    }

    public void init(Vec3 start, Vec3 end) {
        this.startPos = start;
        this.endPos = end;
    }

    @Override
    public void serialize(CompoundTag tag, HolderLookup.Provider registries) {
        super.serialize(tag, registries);

        SerializationUtil.vec3("sPos", this.startPos, tag);
        SerializationUtil.vec3("ePos", this.endPos, tag);
    }

    @Override
    public void deserialize(CompoundTag tag, HolderLookup.Provider registries) {
        super.deserialize(tag, registries);

        this.startPos = SerializationUtil.vec3("sPos", tag);
        this.endPos = SerializationUtil.vec3("ePos", tag);
    }

    public int getRot() {
        return this.rotation;
    }

    public void setRot(int rot) {
        this.rotation = rot;
    }

    @Override
    public boolean canTick() {
        return true;
    }

    @Override
    public void onTick() {
    }

    @Override
    public InteractionResult onPlayerInteraction(Player player, InteractionHand hand, ItemStack stack) {

        if(stack.getItem() instanceof BucketItem bucket && !stack.is(Items.BUCKET)) {
            if(this.fluid == null) {
                this.fluid = new FluidStack(bucket.content, 1000);
            } else {
                if(this.fluid.getAmount() <= getVolume() - 1000) this.fluid.grow(1000);
            }
        }

        if(stack.is(Items.BUCKET)) {
            if(this.fluid != null) {
                this.fluid.shrink(1000);
            }
        }

        if(stack.isEmpty()) {
            String string = this.fluid == null || this.fluid.isEmpty() ? "No Contents" : this.fluid.getAmount() + " mb of " + Component.translatable(this.fluid.getFluid().getFluidType().toString()).getString();
            player.displayClientMessage(Component.literal("Tank Volume is " + getVolume() + " mb (" + string + ")"), true);
        }

        if(stack.is(Tags.Items.MINING_TOOL_TOOLS)) {
            destroy();
        }

        return InteractionResult.SUCCESS;
    }

    public int getVolume() {
        return (int) VectorUtil.volume(VectorUtil.abs(this.startPos.subtract(this.endPos))) * 1000;
    }

    @Override
    public ActorType<?> actorType() {
        return BBActors.TEST_ACTOR.get();
    }

    @Override
    public SceneType<?, ?> sceneType() {
        return BBScenes.BUILTIN.get();
    }

    @Override
    public VoxelShape getCollisionBox() {
        Vec3 end = this.startPos.subtract(this.endPos);

        return Block.box(0, 0, 0, Math.abs(end.x) * 16, Math.abs(end.y) * 16, Math.abs(end.z) * 16);
    }

    public void test() {

    }
}
