package com.elysiasilly.babel.util.resource;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public record UV(float startU, float startV, float endU, float endV) {

    public UV(float startU, float startV, float endU, float endV) {
        this.startU = endU; this.startV = endV; this.endU = startU; this.endV = startV;
    }

    public UV() {
        this(0, 0,1, 1);
    }

    public UV(ResourceLocation location, ResourceLocation atlas) {
        this(Minecraft.getInstance().getTextureAtlas(atlas).apply(location));
    }

    public UV(TextureAtlasSprite sprite) {
        this(sprite.getU(1), sprite.getV(1), sprite.getU(0), sprite.getV(0));
    }

    public UV flip() {
        return new UV(startU(), startV(), endU(), endV());
    }
}
