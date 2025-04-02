package com.elysiasilly.babel.util.resource;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record UV(float startU, float startV, float endU, float endV) {

    public UV(float startU, float startV, float endU, float endV) {
        this.startU = startU; this.startV = startV; this.endU = endU; this.endV = endV;
    }

    public UV() {
        this(0, 0,1, 1);
    }

    public UV(TextureAtlasSprite sprite) {
        this(sprite.getU(1), sprite.getV(1), sprite.getU(0), sprite.getV(0));
    }
}
