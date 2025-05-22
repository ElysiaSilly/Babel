package com.elysiasilly.babel.api.theatre.collision;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.util.UtilsFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;

/*
 * generate an aabb from this to perform a simpler check before using GJK
 */
public class MeshCollider implements Collider {

    private final Vector3d[] vertices, rotatedVertices;
    private final Quaterniond orientation = new Quaterniond();
    private final Vector3d origin, offset = new Vector3d();

    private AABB aabb;

    public MeshCollider(Vector3d origin, Vector3d...vertices) {
        this.vertices = vertices;
        this.rotatedVertices = new Vector3d[vertices.length];
        this.origin = origin;

        cache();
    }

    public MeshCollider(@Nullable Vector3d origin, AABB aabb) {
        Vector3d[] vertices = new Vector3d[8];

        vertices[0] = new Vector3d(aabb.minX, aabb.minY, aabb.minZ);
        vertices[1] = new Vector3d(aabb.minX, aabb.minY, aabb.maxZ);
        vertices[2] = new Vector3d(aabb.minX, aabb.maxZ, aabb.maxZ);
        vertices[3] = new Vector3d(aabb.maxX, aabb.minY, aabb.minZ);
        vertices[4] = new Vector3d(aabb.maxX, aabb.minY, aabb.minZ);
        vertices[5] = new Vector3d(aabb.minX, aabb.maxY, aabb.minZ);
        vertices[6] = new Vector3d(aabb.maxX, aabb.minY, aabb.maxY);
        vertices[7] = new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ);

        this.vertices = vertices;
        this.rotatedVertices = new Vector3d[vertices.length];

        this.origin = origin == null ? centre(aabb) : origin;

        cache();
    }

    private static Vector3d centre(AABB aabb) {
        return new Vector3d(Mth.lerp(.5, aabb.minX, aabb.maxX), Mth.lerp(.5, aabb.minY, aabb.maxY), Mth.lerp(.5, aabb.minZ, aabb.maxZ));
    }

    @Override
    public AABB aabb() {
        return this.aabb;
    }

    public Vector3d offset() {
        return new Vector3d(this.offset);
    }

    public void offset(Vector3d offset) {
        if(offset().equals(offset)) return;
        this.offset.set(offset);
    }

    public void orientation(Quaterniond orientation) {
        if(this.orientation.equals(orientation)) return;
        this.orientation.set(orientation);
        cache();
    }

    public Quaterniond orientation() {
        return new Quaterniond(this.orientation);
    }

    public void origin(Vector3d origin) {
        if(this.origin.equals(origin)) return;
        this.origin.set(origin);
        cache();
    }

    public Vector3d origin() {
        return new Vector3d(this.origin);
    }

    private void cache() {
        int index = 0;

        for(Vector3d vertex : get()) {
            this.rotatedVertices[index] = orientation().transform(vertex, origin());
            index++;
        }
    }

    public Vector3d[] get() {
        Vector3d[] vertices = new Vector3d[this.vertices.length];

        int index  = 0;
        for(Vector3d vertex : this.vertices) {
            vertices[index] = new Vector3d(vertex);
            index++;
        }

        return vertices;
    }

    public Vector3d[] getCached() {
        Vector3d[] vertices = new Vector3d[this.rotatedVertices.length];

        int index = 0;
        for(Vector3d vertex : this.rotatedVertices) {
            vertices[index] = new Vector3d(vertex).add(offset());
            index++;
        }

        return vertices;
    }

    @Override
    public Vector3d findFurthestPoint(Vector3d direction) {
        Vector3d maxVertex = null;

        double maxDistance = Double.MIN_VALUE;

        for(Vector3d vertex : this.rotatedVertices) {
            double distance = vertex.dot(direction);

            if(distance > maxDistance) {
                maxDistance = distance;
                maxVertex = vertex;
            }
        }

        if(maxVertex == null) {
            Babel.LOGGER.warn("Somehow failed to find a Vertex for Direction {}", UtilsFormatting.vector3d(direction));
            maxVertex = new Vector3d();
        }

        return new Vector3d(maxVertex).add(offset());
    }
}
