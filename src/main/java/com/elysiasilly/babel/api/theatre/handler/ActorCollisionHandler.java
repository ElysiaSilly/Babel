package com.elysiasilly.babel.api.theatre.handler;

import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorPredicates;
import com.elysiasilly.babel.api.theatre.collision.MeshCollider;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActorCollisionHandler {

    private final Scene<?> scene;

    public ActorCollisionHandler(Scene<?> scene) {
        this.scene = scene;
    }

    private Scene<?> scene() {
        return this.scene;
    }

    // limit actor voxelshape to 48x48x48 (blocks) probably
    private Collection<Actor> actors3by3ChunkSection(SectionPos pos) {

        Collection<Actor> actors = new ArrayList<>();

        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -1; z <= 1; z++) {
                    actors.addAll(scene().actorsInSection(pos.offset(x, y, z)));
                }
            }
        }

        actors.removeIf(actor -> actor != null && (!ActorPredicates.CAN_BE_COLLIDED_WITH.test(actor) || !actor.collisionShapeWorldSpace().bounds().intersects(collision(pos))));

        return actors;
    }

    public void tick() {
        Collection<Candidate> candidates = new ArrayList<>();

        for(SectionPos pos : scene().populatedSections()) {
            candidates.addAll(simple(actors3by3ChunkSection(pos), pos));
        }

        for(Candidate candidate : candidates) {
            complex(candidate.entity(), candidate.actor());
        }
    }

    private Collection<Candidate> simple(Collection<Actor> actors, SectionPos pos) {
        List<Entity> entities = scene().level().getEntities(null, collision(pos));

        Collection<Candidate> candidates = new ArrayList<>();

        for(Entity entity : entities) {
            if(!entity.noPhysics) {
                for (Actor actor : actors) {
                    if (entity.getBoundingBox().intersects(actor.collisionShapeWorldSpace().bounds())) {
                        candidates.add(new Candidate(actor, entity));
                    }
                }
            }
        }

        return candidates;
    }

    private void complex(Entity entity, Actor actor) {

        AABB entityAABB = entity.getBoundingBox();

        for(AABB actorAABB : actor.collisionShapeWorldSpace().toAabbs()) {
            if(entityAABB.intersects(actorAABB)) {

                MeshCollider entityMesh = new MeshCollider(null, entityAABB);

                MeshCollider actorMesh = new MeshCollider(null, actorAABB);

                /*
                Vec3 entityCentre = entityAABB.getCenter();
                Vec3 actorCentre = actorAABB.getCenter();


                Vec3 push = entityCentre.subtract(actorCentre).normalize();

                //System.out.println(push);

                entity.setDeltaMovement(push.scale(.1));

                //entity.setPos(push);

                 */
            }
        }


    }

    private AABB collision(SectionPos pos) {
        return new AABB(pos.minBlockX(), pos.minBlockY(), pos.minBlockZ(), pos.maxBlockX(), pos.maxBlockY(), pos.maxBlockZ());
    }

    public record Candidate(Actor actor, Entity entity) {}
}
