package com.elysiasilly.babel.client.screen;

import com.elysiasilly.babel.util.Conversions;
import com.elysiasilly.babel.util.RGBA;
import com.elysiasilly.babel.util.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("rawtypes")
public abstract class BabelWidget<W extends BabelWidget, S extends BabelScreen> {

    public W parent;
    public final S screen;

    public final List<BabelWidget<?, ?>> children = new ArrayList<>();

    public List<BabelWidget> getChildren() {
        return List.copyOf(this.children);
    }

    public final UUID ID;

    public final WidgetBounds bounds;

    boolean lock = true;

    public BabelWidget(@Nullable W parent, @NotNull S screen, @NotNull WidgetBounds bounds) {
        this.screen = screen;
        this.parent = parent;
        this.bounds = bounds;
        this.ID = UUID.randomUUID();
        initBefore();
        add(initWidgets());
        initAfter();
        this.lock = false;
    }

    public BabelWidget(@NotNull S screen, @NotNull WidgetBounds bounds) {
        this(null, screen, bounds);
    }

    public void add(List<BabelWidget<?, ?>> widgets) {
        this.children.addAll(widgets);
        this.screen.descendants.addAll(widgets);
    }

    public Vec2 mouseDrag() {
        return this.screen.getMouseDrag();
    }

    public Vec2 mousePos() {
        return this.screen.getMousePos();
    }

    public boolean isHovering() {
        return this.screen.isHovering(this);
    }

    public boolean isDragging() {
        return this.screen.isDragging(this);
    }

    public boolean isFocused() {
        return this.screen.isFocused(this);
    }

    public void initBefore() {}

    public List<BabelWidget<?, ?>> initWidgets() {
        return List.of();
    }

    public void initAfter() {}

    public void processRendering(GuiGraphics guiGraphics, float partialTick) {
        if(lock) return;
        this.bounds.calculateGlobals();
        getChildren().forEach(child -> child.processRendering(guiGraphics, partialTick));
        if(this.screen.renderDebug()) renderDebug(guiGraphics, partialTick);
        render(guiGraphics, partialTick);
    }

    public void processTicking() {
        if(lock) return;
        tick();
        getChildren().forEach(BabelWidget::processTicking);
    }

    public void tick() {}

    public void render(GuiGraphics guiGraphics, float partialTick) {}

    private RGBA current;

    private void renderDebug(GuiGraphics guiGraphics, float partialTick) {
        RGBA to = isHovering() || isDragging() || isFocused() ? RGBA.colours.WHITE : new RGBA(this.bounds.depth / 2);
        if(this.current == null) this.current = to;

        this.current = this.current.lerp(to, .5);

        RenderUtil.drawOutlineRectangle(guiGraphics.bufferSource().getBuffer(RenderType.gui()), guiGraphics.pose().last().pose(), Conversions.vector.vec3(this.boundStart()),  Conversions.vector.vec3(this.boundEnd()), 1, this.current);
    }

    public void destroy() {
        this.screen.destroy(this);
        if(this.parent != null) this.parent.children.remove(this);
    }

    public void move(Vec2 vec2) {
        this.bounds.move(vec2);
    }

    public void moveWithChildren(Vec2 vec2) {
        move(vec2);
        getChildren().forEach(child -> child.moveWithChildren(vec2));
    }

    public void moveChildren(Vec2 vec2) {
        getChildren().forEach(child -> child.moveWithChildren(vec2));
    }

    public Vec2 boundStart() {
        return this.bounds.globalStart;
    }

    public Vec2 boundEnd() {
        return this.bounds.globalEnd;
    }

    public Vec2 pos() {
        return this.bounds.position;
    }

    public float depth() {
        return this.bounds.depth;
    }

    public void pos(Vec2 pos) {
        this.bounds.position = pos;
    }

    public void unParent() {
        this.parent.children.remove(this);
        this.parent = null;
        if(!this.screen.children.contains(this)) this.screen.children.add(this);
        if(!this.screen.descendants.contains(this)) this.screen.descendants.add(this);
    }

    public void rebindParent() {
        if(parent != null) {
            this.screen.children.remove(this);
            if(!this.parent.children.contains(this)) this.parent.children.add(this);
            this.bounds.depth = this.parent.bounds.depth + .1f;
        }
    }


}
