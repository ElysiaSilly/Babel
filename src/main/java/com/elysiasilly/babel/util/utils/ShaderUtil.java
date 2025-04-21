package com.elysiasilly.babel.util.utils;

import com.elysiasilly.babel.api.client.IDepthRenderTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class ShaderUtil {

    public static void setUniform(ShaderInstance instance, String id, double x) {
        setUniform(instance, id, (float) x);
    }

    public static void setUniform(ShaderInstance instance, String id, double x, double y) {
        setUniform(instance, id, (float) x, (float) y);
    }

    public static void setUniform(ShaderInstance instance, String id, double x, double y, double z, double w) {
        setUniform(instance, id, (float) x, (float) y, (float) z, (float) w);
    }

    public static void setUniform(ShaderInstance instance, String id, float x) {
        Uniform uniform = instance.getUniform(id);
        if(uniform != null) uniform.set(x);
    }

    public static void setUniform(ShaderInstance instance, String id, int x) {
        Uniform uniform = instance.getUniform(id);
        if(uniform != null) uniform.set(x);
    }

    public static void setUniform(ShaderInstance instance, String id, float x, float y) {
        Uniform uniform = instance.getUniform(id);
        if(uniform != null) uniform.set(x, y);
    }

    public static void setUniform(ShaderInstance instance, String id, float x, float y, float z, float w) {
        Uniform uniform = instance.getUniform(id);
        if(uniform != null) uniform.set(x, y, z, w);
    }

    ///

    public static boolean stage(RenderLevelStageEvent event, RenderLevelStageEvent.Stage stage) {
        return event.getStage() != stage;
    }

    ///

    public static RenderTarget depthBuffer() {
        return ((IDepthRenderTarget) Minecraft.getInstance().levelRenderer).babel$getDepthRenderTarget();
    }

    public static RenderTarget mainBuffer() {
        return Minecraft.getInstance().getMainRenderTarget();
    }
}
