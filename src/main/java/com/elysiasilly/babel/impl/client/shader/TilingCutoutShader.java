package com.elysiasilly.babel.impl.client.shader;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.shader.CoreShader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

import static net.minecraft.client.renderer.RenderStateShard.*;

public class TilingCutoutShader extends CoreShader {

    @Override
    public RenderType.CompositeState createState() {
        return RenderType.CompositeState.builder()
                .setShaderState(getShard())
                .setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .createCompositeState(true);
    }

    @Override
    public RenderType createType() {
        return RenderType.create(path().getPath(), DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, getState());
    }

    @Override
    public ResourceLocation path() {
        return Babel.location("tiling_cutout");
    }

    @Override
    public ShaderInstance createInstance(ResourceProvider provider) throws IOException {
        return new ShaderInstance(provider, path(), DefaultVertexFormat.POSITION_COLOR);
    }
}
