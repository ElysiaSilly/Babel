package com.elysiasilly.babel.impl.client.shader;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.shader.CoreShader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class EntityShadowShader extends CoreShader {

    @Override
    public RenderType.CompositeState createState() {
        return RenderType.CompositeState.builder()
                .setShaderState(getShard())
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(false);
    }

    @Override
    public RenderType createType() {
        return RenderType.create(path().toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, false, false, getState());
    }

    @Override
    public ResourceLocation path() {
        return Babel.location("entity_shadow");
    }

    @Override
    public ShaderInstance createInstance(ResourceProvider provider) throws IOException {
        return new ShaderInstance(provider, path(), DefaultVertexFormat.NEW_ENTITY);
    }
}
