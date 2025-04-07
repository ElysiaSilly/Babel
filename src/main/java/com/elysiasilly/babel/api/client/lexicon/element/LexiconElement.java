package com.elysiasilly.babel.api.client.lexicon.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class LexiconElement {

    private final Vec3 position;
    private final float rotation;
    private final Vec2 size;

    public LexiconElement(Vec3 position, float rotation, Vec2 size) {
        this.position = position;
        this.rotation = rotation;
        this.size = size;
    }

    public Vec3 pos() {
        return new Vec3(this.position.x, this.position.y, this.position.z);
    }

    public Vec2 size() {
        return new Vec2(this.size.x, this.size.y);
    }

    public float rot() {
        return this.rotation;
    }

    public abstract void render(PoseStack pose, MultiBufferSource source, float partialTicks);
}
