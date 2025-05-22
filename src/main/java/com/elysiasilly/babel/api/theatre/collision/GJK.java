package com.elysiasilly.babel.api.theatre.collision;

import org.joml.Vector3d;

/**
 * based on winter.dev's gjk algorithm
 * this is way above my pay grade
 */
public class GJK {

    public static Vector3d support(MeshCollider first, MeshCollider second, Vector3d direction) {
        return first.findFurthestPoint(direction).sub(second.findFurthestPoint(direction.mul(-1)));
    }

    public static Vector3d cross(Vector3d first, Vector3d second) {
        return new Vector3d(first).cross(second);
    }

    public static boolean sameDirection(Vector3d direction, Vector3d vector) {
        return direction.dot(vector) > 0;
    }

    ///

    public static MeshCollider minkowskiDifference(MeshCollider first, MeshCollider second) {

        Vector3d[] minkowskiDifference = new Vector3d[first.getCached().length * second.getCached().length];

        int index = 0;
        for(Vector3d vertexFirst : first.getCached()) {
            for(Vector3d vertexSecond : second.getCached()) {

                minkowskiDifference[index] = new Vector3d(vertexFirst).sub(vertexSecond);

                index++;
            }
        }

        return new MeshCollider(new Vector3d(), minkowskiDifference);
    }

    public static boolean gjk(MeshCollider first, MeshCollider second) {

        Vector3d support = support(first, second, new Vector3d(0, 1, 0));

        Simplex points = new Simplex();

        points.push(support);

        Vector3d direction = new Vector3d(support).mul(-1);

        while(true) {

            support = support(first, second, direction);

            if(support.dot(direction) <= 0) {
                return false;
            }

            points.push(support);

            if(nextSimplex(points, direction)) {
                return true;
            }
        }
    }

    public static boolean nextSimplex(Simplex points, Vector3d direction) {
        return switch(points.size()) {
            case 2  -> line(points, direction);
            case 3  -> triangle(points, direction);
            case 4  -> tetrahedron(points, direction);
            default -> false;
        };
    }

    public static boolean line(Simplex points, Vector3d direction) {

        Vector3d a = points.vertex(0), b = points.vertex(1);

        Vector3d ab = new Vector3d(b).sub(a);
        Vector3d ao = new Vector3d(a).mul(-1);

        if(sameDirection(ab, ao)) {
            direction.set(cross(cross(ab, ao), ab));
        } else {
            //points.update(a);
            direction.set(ao);
        }

        return false;
    }

    public static boolean triangle(Simplex points, Vector3d direction) {
        return false;
    }

    public static boolean tetrahedron(Simplex points, Vector3d direction) {
        return false;
    }
}
