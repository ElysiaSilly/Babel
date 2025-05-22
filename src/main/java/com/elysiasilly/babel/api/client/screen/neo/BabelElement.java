package com.elysiasilly.babel.api.client.screen.neo;

import com.elysiasilly.babel.api.theatre.collision.MeshCollider;
import com.elysiasilly.babel.util.UtilsRender;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class BabelElement {

    private final MeshCollider collider;

    private final BabelScreen screen;

    private ColliderHandler colliderHandler;
    private ClickHandler clickHandler;
    private HoverHandler hoverHandler;
    private DragHandler dragHandler;
    private KeyPressHandler keyPressHandler;

    private final Vector3d position;
    private final Quaterniond orientation = new Quaterniond();

    public BabelElement(Vector3d position, BabelScreen screen, MeshCollider collider) {
        this.screen = screen;
        this.collider = collider;
        this.position = position;
    }

    public Vector3d position() {
        return new Vector3d(this.position);
    }

    public void position(Vector3d vec) {
        this.position.set(vec);
    }

    public void offset(Vector3d vec) {
        this.position.add(vec);
    }

    public void orientation(Quaterniond orientation) {
        this.orientation.set(orientation);
    }

    public Quaterniond orientation() {
        return new Quaterniond(this.orientation);
    }

    public BabelScreen screen() {
        return this.screen;
    }

    public Vector2d screenSize() {
        return screen().screenSize();
    }

    public Vector2d screenCentre() {
        return screen().screenCentre();
    }

    public MeshCollider collider() {
        return this.collider;
    }

    public void tick() {
        collider().offset(position());
        collider().orientation(orientation());
    }

    public void render(GuiGraphics guiGraphics, MultiBufferSource bufferSource, PoseStack poseStack) {

    }

    public void renderDebug(GuiGraphics guiGraphics, MultiBufferSource bufferSource, PoseStack poseStack) {
        Vector3d[] vertices = collider().getCached();

        int index = 0;
        for(Vector3d vertex : vertices) {

            index = index >= (vertices.length - 1) ? 0 : index + 1;

            drawLine(bufferSource.getBuffer(RenderType.gui()), poseStack.last().pose(), vertex, vertices[index], .5f, RGBA.WHITE);

            poseStack.pushPose();

            poseStack.translate(vertex.x - 1, vertex.y - 1, 0);

            UtilsRender.drawCube(bufferSource.getBuffer(RenderType.gui()), poseStack.last().pose(), LightTexture.FULL_BRIGHT, RGBA.WHITE, UtilsRender.Cube.cube(), 2);

            poseStack.popPose();
        }
    }

    public static void drawLine(VertexConsumer consumer, Matrix4f matrix4f, Vector3d start, Vector3d end, float girth, RGBA rgba) {

        Vector2d difference = new Vector2d(start.x - end.x, start.y - end.y), perpendicular = new Vector2d((difference.y), -difference.x);

        double length = Math.sqrt(perpendicular.x * perpendicular.x + perpendicular.y * perpendicular.y);

        Vector2d normalize = new Vector2d(perpendicular.x / length, perpendicular.y / length);

        UtilsRender.drawPlane(
                consumer, matrix4f, 100, rgba,
                new Vec3(start.x + normalize.x * girth / 2, start.y + normalize.y * girth / 2, 0),
                new Vec3(end.x - normalize.x * girth / 2, end.y - normalize.y * girth / 2, 0),
                new Vec3(start.x - normalize.x * girth / 2, start.y - normalize.y * girth / 2, 0),
                new Vec3(end.x + normalize.x * girth / 2, end.y + normalize.y * girth / 2, 0)
        );
    }

    public boolean canKeyPress() {
        return this.keyPressHandler != null;
    }
    
    public void keyPress(int keyCode, int scanCode, int modifiers) {
        if(canKeyPress()) this.keyPressHandler.onKeyPress(this, keyCode, scanCode, modifiers);
    }

    public boolean canClick() {
        return this.clickHandler != null;
    }

    public void click(int mouseButton) {
        if(canClick()) this.clickHandler.onClick(this, mouseButton);
    }

    public boolean canHover() {
        return this.hoverHandler != null;
    }

    public void hover() {
        if(canHover()) this.hoverHandler.onHover(this);
    }

    public boolean canDrag() {
        return this.dragHandler != null;
    }

    public void drag() {
        if(canDrag()) this.dragHandler.onDrag(this);
    }

    public boolean canCollide() {
        return this.colliderHandler != null;
    }

    public void collide(BabelElement element) {
        if(canCollide()) this.colliderHandler.onCollide(this, element);
    }

    public boolean focused() {
        return screen().focused(this);
    }

    public boolean hovering() {
        return screen().hovering(this);
    }

    public boolean dragging() {
        return screen().dragging(this);
    }

    public <E extends BabelElement> void click(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public <E extends BabelElement> void drag(DragHandler dragHandler) {
        this.dragHandler = dragHandler;
    }

    public <E extends BabelElement> void hover(HoverHandler hoverHandler) {
        this.hoverHandler = hoverHandler;
    }

    public <E extends BabelElement> void collide(ColliderHandler colliderHandler) {
        this.colliderHandler = colliderHandler;
    }

    public <E extends BabelElement> void keyPress(KeyPressHandler keyPressHandler) {
        this.keyPressHandler = keyPressHandler;
    }
    
    @FunctionalInterface
    public interface ClickHandler {
        void onClick(BabelElement ctx, int mouseButton);
    }

    @FunctionalInterface
    public interface DragHandler {
        void onDrag(BabelElement ctx);
    }

    @FunctionalInterface
    public interface HoverHandler {
        void onHover(BabelElement ctx);
    }

    @FunctionalInterface
    public interface ColliderHandler {
        void onCollide(BabelElement ctx, BabelElement collidedElement);
    }

    @FunctionalInterface
    public interface KeyPressHandler {
        void onKeyPress(BabelElement ctx, int keyCode, int scanCode, int modifiers);
    }
}
