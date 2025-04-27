package com.elysiasilly.babel.api.theatre.util;

import org.joml.Vector3d;

/**
 * based on winter.dev's gjk algorithm
 * this is way above my pay grade
 */
public class GJK {

    // will not be used but keeping it around for reference
    public static Element minkowskiDifference(Element first, Element second) {

        Vector3d[] minkowskiDifference = new Vector3d[first.get().length * second.get().length];

        int index = 0;
        for(Vector3d vert1 : first.get()) {
            for(Vector3d vert2 : second.get()) {

                minkowskiDifference[index] = new Vector3d(vert1).sub(vert2);

                index++;
            }
        }

        return new Element(null, null, minkowskiDifference);
    }

    public static Vector3d support(Element first, Element second, Vector3d direction) {
        return first.findFurthestPoint(direction).sub(second.findFurthestPoint(direction.mul(-1)));
    }

    public static boolean gjk(Element first, Element second) {

        Vector3d support = support(first, second, new Vector3d(0, 0, 1));

        Element.Simplex simplex = new Element.Simplex();

        simplex.push(support);

        Vector3d direction = new Vector3d(support).mul(-1);

        while(true) {

            support = support(first, second, direction);

            if(support.dot(direction) <= 0) {
                return false;
            }

            simplex.push(support);

            if(nextSimplex(simplex, direction)) {
                return true;
            }
        }
    }

    public static boolean nextSimplex(Element.Simplex simplex, Vector3d direction) {

        switch(simplex.size()) {
            case 2 -> {
                return line(simplex, direction);
            }
            case 3 -> {
                return triangle(simplex, direction);
            }
            case 4 -> {
                return tetrahedron(simplex, direction);
            }
        }

        return false;
    }

    public static boolean line(Element.Simplex simplex, Vector3d direction) {
        return true;
    }

    public static boolean triangle(Element.Simplex simplex, Vector3d direction) {
        return true;
    }

    public static boolean tetrahedron(Element.Simplex simplex, Vector3d direction) {
        return true;
    }
}
