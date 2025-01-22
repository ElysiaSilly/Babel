package com.elysiasilly.babel.util;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class ShaderUtil {
    public static void setUniform(ShaderInstance instance, String name, Object value) {
        Uniform uniform = instance.getUniform(name);
        if(uniform == null) return;

        switch(value) {
            case Float f -> uniform.set(f);
            case Integer i -> uniform.set(i);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    public static boolean stage(RenderLevelStageEvent event, RenderLevelStageEvent.Stage stage) {
        return event.getStage() != stage;
    }
}
