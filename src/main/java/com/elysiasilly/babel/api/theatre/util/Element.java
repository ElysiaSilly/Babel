package com.elysiasilly.babel.api.theatre.util;

import com.elysiasilly.babel.util.UtilsMath;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Element {

    private final Vector3d[] vertices, cachedVertices;
    private Quaterniond orientation;
    private Vector3d origin;

    public Element(@Nullable Vector3d origin, Quaterniond orientation, AABB aabb) {
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
        this.cachedVertices = vertices;

        this.origin = origin == null ? centre(aabb) : origin;
        this.orientation = orientation;

        cache();
    }

    private static Vector3d centre(AABB aabb) {
        return new Vector3d(Mth.lerp(.5, aabb.minX, aabb.maxX), Mth.lerp(.5, aabb.minY, aabb.maxY), Mth.lerp(.5, aabb.minZ, aabb.maxZ));
    }

    public Element(Vector3d origin, Quaterniond orientation, Vector3d...vertices) {
        this.vertices = vertices;
        this.cachedVertices = vertices;
        this.orientation = orientation;
        this.origin = origin;

        cache();
    }

    public void orientation(Quaterniond orientation) {
        if(!this.orientation.equals(orientation)) {
            this.orientation = new Quaterniond(orientation);
            cache();
        }
    }

    public Quaterniond orientation() {
        return new Quaterniond(this.orientation);
    }

    public void origin(Vector3d origin) {
        if(!this.origin.equals(origin)) {
            this.origin = new Vector3d(origin);
            cache();
        }
    }

    public Vector3d origin() {
        return new Vector3d(this.origin);
    }

    private void cache() {
        int index = 0;
        for(Vector3d vertex : this.vertices) {
            //orientation.transform(origin, vertex); ?
            cachedVertices[index] = UtilsMath.rotateAroundPoint(origin, vertex, orientation());
            index++;
        }
    }

    public Vector3d[] get() {
        Vector3d[] vertices = new Vector3d[this.cachedVertices.length];

        int index = 0;
        for(Vector3d vertex : this.cachedVertices) {
            vertices[index] = new Vector3d(vertex);
            index++;
        }

        return vertices;
    }

    // TODO this is in local space
    public Vector3d findFurthestPoint(Vector3d direction) {
        Vector3d maxVertex = null;

        double maxDistance = Double.MIN_VALUE;

        for(Vector3d vertex : this.cachedVertices) {
            double distance = vertex.dot(direction);
            if(distance > maxDistance) {
                maxDistance = distance;
                maxVertex = vertex;
            }
        }

        if(maxVertex == null)
            throw new RuntimeException("Failed to find a Vertex Somehow");

        return maxVertex;
    }

    public static class Simplex {
        private Vector3d[] vertices = new Vector3d[4];
        private int size = 0;

        public Simplex() {}

        public void push(Vector3d vertex) {
            this.vertices = new Vector3d[]{vertex, this.vertices[0], this.vertices[1], this.vertices[2]};
            this.size = Math.min(this.size + 1, 4);
        }

        public int size() {
            return this.size;
        }

        public Vector3d vertex(int index) {
            return this.vertices[index];
        }
    }
}
