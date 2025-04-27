package com.elysiasilly.babel.api.client;

import com.mojang.blaze3d.pipeline.RenderTarget;

public interface IRenderTargets {
    RenderTarget babel$getDepthRenderTarget();

    RenderTarget babel$getMainRenderTarget();
}
