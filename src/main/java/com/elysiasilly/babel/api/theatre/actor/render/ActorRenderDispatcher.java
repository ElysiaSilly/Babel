package com.elysiasilly.babel.api.theatre.actor.render;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ActorRenderDispatcher implements ResourceManagerReloadListener {

    // todo : frustum cull

    public static final ActorRenderDispatcher INSTANCE = new ActorRenderDispatcher();

    private Map<ActorType<?>, ActorRenderer<?>> renderers;

    @Nullable
    public <A extends Actor> ActorRenderer<?> getRenderer(A actor) {
        return this.renderers.get(actor.actorType());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.renderers = ActorRenderers.createRenderers();
    }

    @SubscribeEvent
    private static void reloadListener(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }

    @SubscribeEvent
    private static void render(RenderLevelStageEvent event) {
        if(event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_ENTITIES)) {

            Level level = Minecraft.getInstance().level;

            List<Scene<?>> scenes = Theatre.get(level);

            for(Scene<?> scene : scenes) {
                for(Actor actor : scene.getActorsInStorage()) {
                    PoseStack poseStack = event.getPoseStack();

                    poseStack.pushPose();

                    Vec3 pos = actor.getPos();
                    Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
                    poseStack.translate(pos.x - cam.getPosition().x, pos.y - cam.getPosition().y, pos.z - cam.getPosition().z);

                    ActorRenderer<?> renderer = INSTANCE.getRenderer(actor);
                    renderer.render(actor.self(), event.getRenderTick(), poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), LightTexture.FULL_SKY);

                    poseStack.popPose();
                }
            }
        }
    }
}
