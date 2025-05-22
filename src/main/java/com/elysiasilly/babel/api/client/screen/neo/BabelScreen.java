package com.elysiasilly.babel.api.client.screen.neo;

import com.elysiasilly.babel.util.UtilsDev;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class BabelScreen extends Screen {

    private long ticks = 0;

    private final List<BabelElement> elements = new ArrayList<>();

    private final Vector2d mousePos = new Vector2d(), previousMousePos = new Vector2d(), mouseVelocity = new Vector2d();

    private BabelElement focusedElement = null, draggedElement = null, hoveredElement = null;

    protected BabelScreen() {
        super(Component.empty());

        List<BabelElement> elements = new ArrayList<>();

        buildElements(elements);

        UtilsDev.postGameEvent(new BabelScreenEvents.BuiltElements(this, elements));

        this.elements.addAll(elements);
    }

    public abstract void buildElements(List<BabelElement> builder);

    public List<BabelElement> elements() {
        return new ArrayList<>(this.elements);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
            onClose();
        } else {
            for(BabelElement element : elements()) {
                element.keyPress(keyCode, scanCode, modifiers);
            }
        }
        return true;
    }

    @Override
    public void tick() {
        //this.ticks++;

        for(BabelElement element : elements()) {
            element.tick();
        }
    }

    public boolean hovering(BabelElement element) {
        return hovering() && this.hoveredElement == element;
    }

    public boolean hovering() {
        return hoveredElement != null;
    }

    public void releaseHovered() {
        this.hoveredElement = null;
    }

    public boolean dragging(BabelElement element) {
        return dragging() && this.draggedElement == element;
    }

    public boolean dragging() {
        return draggedElement != null;
    }

    public void releaseDragged() {
        this.draggedElement = null;
    }

    public boolean focused(BabelElement element) {
        return focused() && this.focusedElement == element;
    }

    public boolean focused() {
        return focusedElement != null;
    }

    public void releaseFocused() {
        this.focusedElement = null;
    }

    public long ticks() {
        return this.ticks;
    }

    @Override
    public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.ticks++;

        updateMousePosition(new Vector2d(mouseX, mouseY));

        PoseStack poseStack = guiGraphics.pose();
        MultiBufferSource bufferSource = guiGraphics.bufferSource();

        poseStack.pushPose();
        renderBefore(guiGraphics, poseStack, bufferSource, partialTick);
        poseStack.popPose();

        for(BabelElement element : elements()) {
            poseStack.pushPose();
            element.render(guiGraphics, bufferSource, poseStack);
            poseStack.popPose();

            if(debugRendering()) {
                poseStack.pushPose();
                element.renderDebug(guiGraphics, bufferSource, poseStack);
                poseStack.popPose();
            }
        }

        poseStack.pushPose();
        renderAfter(guiGraphics, poseStack, bufferSource, partialTick);
        poseStack.popPose();
    }

    public void renderBefore(GuiGraphics guiGraphics, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {

    }

    public void renderAfter(GuiGraphics guiGraphics, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {

    }

    public Vector2d mousePos() {
        return new Vector2d(this.mousePos);
    }

    public Vector2d mouseVelocity() {
        return new Vector2d(this.mouseVelocity);
    }

    public Vector2d screenCentre() {
        return new Vector2d(width / 2f, height / 2f);
    }

    public Vector2d screenSize() {
        return new Vector2d(width, height);
    }

    public void updateMousePosition(Vector2d mousePosition) {
        this.mousePos.set(mousePosition);
        this.mouseVelocity.set(mousePos().sub(this.previousMousePos));
        this.previousMousePos.set(mousePosition);
    }

    public boolean debugRendering() {
        return true;
    }

    public void updateHovering() {
        BabelElement temp = null;

        for(BabelElement element : elements()) {
            if(element.canHover()) {
                //if(GJK.gjk(element.collider(), new Collider(new Vector3d(), new Quaterniond(), new Vector3d(mousePos().x, mousePos().y, 0)))) temp = element;
            }
        }

        this.hoveredElement = temp;
    }
}
