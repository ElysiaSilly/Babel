package com.elysiasilly.babel.api.theatre.collision;

import org.joml.Vector3d;

public class Simplex {
    private Vector3d[] vertices = new Vector3d[4];
    private int size = 0;

    public Simplex() {}

    public void push(Vector3d vertex) {
        this.vertices = new Vector3d[]{new Vector3d(vertex), this.vertices[0], this.vertices[1], this.vertices[2]};
        this.size = Math.min(this.size + 1, 4);
    }

    public Simplex update(Vector3d...vertices) {
        if(size() >= 1) this.vertices[0] = vertices[0];
        if(size() >= 2) this.vertices[1] = vertices[1];
        if(size() >= 3) this.vertices[2] = vertices[2];
        if(size() >= 4) this.vertices[3] = vertices[3];
        return this;
    }

    public int size() {
        return this.size;
    }

    public Vector3d vertex(int index) {
        return this.vertices[index];
    }
}
