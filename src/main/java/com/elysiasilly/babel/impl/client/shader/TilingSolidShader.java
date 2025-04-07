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
import static net.minecraft.client.renderer.RenderStateShard.LEQUAL_DEPTH_TEST;

public class TilingSolidShader extends CoreShader {

    @Override
    public RenderType.CompositeState createState() {
        return RenderType.CompositeState.builder()
                .setLightmapState(LIGHTMAP)
                .setShaderState(getShard())
                .setTextureState(BLOCK_SHEET_MIPPED)
                .createCompositeState(true);
    }

    @Override
    public RenderType createType() {
        return RenderType.create(
                path().getPath(),
                DefaultVertexFormat.BLOCK,
                VertexFormat.Mode.QUADS,
                4194304,
                true,
                false,
                getState()
        );
    }

    @Override
    public ResourceLocation path() {
        return Babel.location("tiling_solid");
    }

    @Override
    public ShaderInstance createInstance(ResourceProvider provider) throws IOException {
        return new ShaderInstance(provider, path(), DefaultVertexFormat.BLOCK);
    }
}
