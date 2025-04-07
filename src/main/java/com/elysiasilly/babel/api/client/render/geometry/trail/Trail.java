package com.elysiasilly.babel.api.client.render.geometry.trail;

import java.util.ArrayList;
import java.util.List;

public class Trail {

    final List<TrailPoint<?>> points = new ArrayList<>();

    public List<TrailPoint<?>> getPoints() {
        return List.copyOf(this.points);
    }

    float maxLength;
    float segmentLength;
    int maxAge;


    public void tick() {
        getPoints().forEach(TrailPoint::tick);
    }

    public void destroy(TrailPoint<?> point) {
        this.points.remove(point);
    }
}
