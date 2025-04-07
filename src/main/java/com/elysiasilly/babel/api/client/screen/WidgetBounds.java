package com.elysiasilly.babel.api.client.screen;

import net.minecraft.world.phys.Vec2;

public class WidgetBounds {

    //final BabelWidget<?, ?> widget;

    public Vec2 localStart, localEnd;

    public Vec2 globalStart = Vec2.ZERO, globalEnd = Vec2.ZERO;

    public Vec2 position = Vec2.ZERO, centre;

    public float depth = 0;

    public WidgetBounds(Vec2 start, Vec2 end) {
        this.localStart = start;
        this.localEnd = end;
    }

    public WidgetBounds(float start, float end) {
        this.localStart = new Vec2(start, start);
        this.localEnd = new Vec2(end, end);
    }

    public WidgetBounds(float startX, float startY, float endX, float endY) {
        this.localStart = new Vec2(startX, startY);
        this.localEnd = new Vec2(endX, endY);
    }

    public void calculateGlobals() {
        this.globalStart = this.position.add(localStart);
        this.globalEnd = this.position.add(localEnd);
    }

    public void calculateCentre() {

    }

    public void move(Vec2 vec2) {
        this.position = this.position.add(vec2);
    }

    public WidgetBounds copy() {
        return null;
    }
}
