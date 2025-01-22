package com.elysiasilly.babel.client.render.geometry;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.StandaloneGeometryBakingContext;
import net.neoforged.neoforge.client.model.obj.ObjLoader;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.client.model.renderable.CompositeRenderable;
import org.jetbrains.annotations.Nullable;

public class ObjRenderer {
    private final CompositeRenderable MODEL;

    public ObjRenderer(ResourceLocation modelLocation, boolean automaticCulling, boolean shadeQuads, boolean flipV, boolean emissiveAmbient, @Nullable String mtlOverride) {
        modelLocation = ResourceLocation.parse(String.format("%s:models/%s.obj", modelLocation.getNamespace(), modelLocation.getPath()));

        ObjModel model = ObjLoader.INSTANCE.loadModel(new ObjModel.ModelSettings(modelLocation, automaticCulling, shadeQuads, flipV, emissiveAmbient, mtlOverride));
        this.MODEL = model.bakeRenderable(StandaloneGeometryBakingContext.create(modelLocation));
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, int packedLight) {
        MODEL.render(poseStack, bufferSource, t -> renderType, packedLight, OverlayTexture.NO_OVERLAY, 0, CompositeRenderable.Transforms.EMPTY);
    }
}
