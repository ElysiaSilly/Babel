package com.elysiasilly.babel.impl.client;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.client.shader.EntityShadowShader;
import com.elysiasilly.babel.impl.client.shader.TilingCutoutShader;
import com.elysiasilly.babel.impl.client.shader.TilingSolidShader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BBShaders {

    public static final TilingCutoutShader TILING_CUTOUT = new TilingCutoutShader();
    public static final TilingSolidShader TILING_SOLID = new TilingSolidShader();

    public static final EntityShadowShader ENTITY_SHADOW = new EntityShadowShader();

    @SubscribeEvent
    private static void register(final RegisterShadersEvent event) throws IOException {
        //TILING_CUTOUT.create(event);
        TILING_SOLID.create(event);
        ENTITY_SHADOW.create(event);
    }
}
