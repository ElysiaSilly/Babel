package com.elysiasilly.babel.api.client.shader;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

public abstract class CoreShader {

    public final void init() {
        this.shard = createShard();
        this.state = createState();
        this.type = createType();
    }

    RenderStateShard.ShaderStateShard shard;

    public final RenderStateShard.ShaderStateShard createShard() {
        return new RenderStateShard.ShaderStateShard(this::getInstance);
    }

    public final RenderStateShard.ShaderStateShard getShard() {
        return this.shard;
    }

    ///

    RenderType.CompositeState state;

    public abstract RenderType.CompositeState createState();

    public final RenderType.CompositeState getState() {
        return this.state;
    }

    ///

    RenderType type;

    public abstract RenderType createType();

    public final RenderType getType() {
        return this.type;
    }

    ///

    public abstract ResourceLocation path();

    ///

    ShaderInstance instance;

    public abstract ShaderInstance createInstance(ResourceProvider provider) throws IOException;

    public final ShaderInstance getInstance() {
        return this.instance;
    }

    ///

    public final void create(RegisterShadersEvent event) throws IOException {
        init();

        event.registerShader(
                createInstance(event.getResourceProvider()),
                i -> instance = i
        );
    }
}
