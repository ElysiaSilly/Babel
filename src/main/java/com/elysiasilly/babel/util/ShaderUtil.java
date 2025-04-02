package com.elysiasilly.babel.util;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class ShaderUtil {
    public static void setUniform(ShaderInstance instance, String name, Object value) {
        Uniform uniform = instance.getUniform(name);
        if(uniform == null) return;

        switch(value) {
            case Float f -> uniform.set(f);
            case Integer i -> uniform.set(i);
            case Vec2 v -> uniform.set(v.x, v.y);
            case Vec3 v -> uniform.set((float) v.x, (float) v.y, (float) v.z);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    public static boolean stage(RenderLevelStageEvent event, RenderLevelStageEvent.Stage stage) {
        return event.getStage() != stage;
    }
}
