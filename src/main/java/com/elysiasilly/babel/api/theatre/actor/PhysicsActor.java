package com.elysiasilly.babel.api.theatre.actor;

import com.elysiasilly.babel.util.UtilsSerialization;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.joml.Vector3d;

import java.util.UUID;

public abstract class PhysicsActor extends Actor {

    private Vector3d velocity = new Vector3d(), force = new Vector3d();

    public PhysicsActor(Vector3d pos, UUID uuid, CompoundTag tag) {
        super(pos, uuid, tag);
        velocity = new Vector3d(0, 0.05, 0.05);
    }

    public PhysicsActor(Vector3d pos, UUID uuid) {
        super(pos, uuid);
    }

    @Override
    public boolean canTick() {
        return true;
    }

    @Override
    public void onTick() {
        server().ifPresent(server -> {

            //float tickSpeed = 1;


            force(force().add(
                    gravity().mul(mass()) )
            );

            //System.out.println(UtilsFormatting.vector3d(force()));

            velocity(velocity().add(
                    force().div(mass()) )
            );

            offset(
                    velocity()
            );

            force(new Vector3d());
        });
    }

    public float mass() {
        return 0.0005f;
    }

    public Vector3d gravity() {
        return new Vector3d(0, -0.001, 0);
    }

    @Override
    public void serialize(CompoundTag tag, HolderLookup.Provider registries) {
        UtilsSerialization.vector3d("vel", velocity(), tag);
        UtilsSerialization.vector3d("force", force(), tag);
    }

    @Override
    public void deserialize(CompoundTag tag, HolderLookup.Provider registries) {
        velocity(UtilsSerialization.vector3d("vel", tag));
        force(UtilsSerialization.vector3d("force", tag));
    }

    public void velocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public void force(Vector3d force) {
        this.force = force;
    }

    public Vector3d velocity() {
        return new Vector3d(this.velocity);
    }

    public Vector3d force() {
        return new Vector3d(this.force);
    }
}
