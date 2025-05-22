package com.elysiasilly.babel.api.theatre.collision.screen;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.screen.neo.BabelElement;
import com.elysiasilly.babel.api.client.screen.neo.BabelScreen;
import com.elysiasilly.babel.api.client.screen.old.BabelScreenUtil;
import com.elysiasilly.babel.api.theatre.collision.MeshCollider;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class PhysicsScreen extends BabelScreen {

    @Override
    public void buildElements(List<BabelElement> widgets) {

        PhysicsElement a = new PhysicsElement(new Vector3d(20, 20, 0), this,
                new MeshCollider(new Vector3d(), new Vector3d(-10, 10, 0), new Vector3d(10, 10, 0), new Vector3d(10, -10, 0), new Vector3d(-10, -10, 0)
        ));

        a.keyPress((ctx, keyCode, scanCode, modifiers) -> {

            switch(keyCode) {
                case GLFW.GLFW_KEY_W -> ctx.offset(new Vector3d(0, -1, 0));
                case GLFW.GLFW_KEY_S -> ctx.offset(new Vector3d(0, 1, 0));
                case GLFW.GLFW_KEY_A -> ctx.offset(new Vector3d(-1, 0, 0));
                case GLFW.GLFW_KEY_D -> ctx.offset(new Vector3d(1, 0, 0));
                case GLFW.GLFW_KEY_R -> ctx.orientation(ctx.orientation().rotateZ(.01f));
            }

            //System.out.println(ctx.collider().get()[0] + " : " + ctx.collider().getCached()[0]);

        });

        PhysicsElement b = new PhysicsElement(new Vector3d(60, 60, 0), this,
                new MeshCollider(new Vector3d(), new Vector3d(-10, 10, 0), new Vector3d(10, 10, 0), new Vector3d(10, -10, 0)
        ));

        MinkowskiDifferenceElement c = new MinkowskiDifferenceElement(this, new MeshCollider(new Vector3d()), a, b);

        widgets.add(a);
        widgets.add(b);
        widgets.add(c);
    }

    @Override
    public void renderBefore(GuiGraphics guiGraphics, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {

        Matrix4f matrix4f = guiGraphics.pose().last().pose();

        BabelScreenUtil.fill(bufferSource.getBuffer(RenderType.gui()), matrix4f, new Vector2d(), screenSize(), -100, RGBA.BLACK);

        BabelElement.drawLine(bufferSource.getBuffer(RenderType.gui()), matrix4f, new Vector3d(width / 2f, 0, -99), new Vector3d(width / 2f, height, -99), 1, new RGBA(.2f, .2f, .2f));
        BabelElement.drawLine(bufferSource.getBuffer(RenderType.gui()), matrix4f, new Vector3d(0, height / 2f, -99), new Vector3d(width, height / 2f, -99), 1, new RGBA(.2f, .2f, .2f));
    }

    @SubscribeEvent
    public static void keyPress(ScreenEvent.KeyPressed.Post event) {
        if(event.getKeyCode() == GLFW.GLFW_KEY_N) {
            Minecraft.getInstance().setScreen(new PhysicsScreen());
        }
    }
}
