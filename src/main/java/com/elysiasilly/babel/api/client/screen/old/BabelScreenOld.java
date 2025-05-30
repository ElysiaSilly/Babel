package com.elysiasilly.babel.api.client.screen.old;

import com.elysiasilly.babel.api.client.screen.old.widget.*;
import com.elysiasilly.babel.util.UtilsMath;
import com.elysiasilly.babel.util.misc.ImmutableTrio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * this will be nuked at some point
 */

@SuppressWarnings("rawtypes")
public abstract class BabelScreenOld extends Screen {

    public final List<BabelWidget<?, ?>> children = new ArrayList<>(), descendants = new ArrayList<>();

    public List<BabelWidget> getDescendants() {
        return List.copyOf(this.descendants);
    }

    public List<BabelWidget> getChildren() {
        return List.copyOf(this.children);
    }

    private Vec2 mousePosOld = Vec2.ZERO, mousePos = Vec2.ZERO, mouseDrag = Vec2.ZERO;

    public BabelWidget hoveredWidget = null, draggedWidget = null, focusedWidget = null;

    private boolean lock = true;

    private int tick = 0;

    private void incrementTick() {
        this.tick = this.tick == Integer.MAX_VALUE ? 0 : this.tick + 1;
    }

    public BabelScreenOld() {
        super(Component.empty());
    }

    @Override
    public void init() {
        initBefore();
        add(initWidgets());
        initAfter();
        this.lock = false;
    }

    public void rebuild() {
        this.lock = true;
        this.children.clear();
        this.descendants.clear();
        init();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        rebuild();
    }

    public void initBefore() {}

    public abstract List<BabelWidget<?, ?>> initWidgets();

    public void initAfter() {}


    public void updateHover() {
        BabelWidget temp = null;

        for(BabelWidget widget : getDescendants()) {
            if(widget instanceof IHoverableWidget hoverable) if(hoverable.canHover()) {
                if(UtilsMath.withinBounds(this.mousePos, widget.boundStart(), widget.boundEnd())) {
                    temp = temp == null ? isDragging(widget) ? null : widget : temp.bounds.depth < widget.bounds.depth ? widget : temp; // todo : clean this up wtf is going on
                    // hello future me here i dont feel like deciphering this
                }
            }
        }

        this.hoveredWidget = temp;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        updateHover();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        getDescendants().forEach(descendant -> {
            if(descendant instanceof IClickListenerWidget widget)
                widget.onClick(button);
        });

        if(this.hoveredWidget instanceof IFocusableWidget widget) {this.focusedWidget = widget.canFocus() ? this.hoveredWidget : null;}

        return super.mouseClicked(mouseX, mouseY, button); // do i need this stuff? :c
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        getDescendants().forEach(descendant -> {if(descendant instanceof IKeyListenerWidget widget) widget.onType();});
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        getDescendants().forEach(descendant -> {if(descendant instanceof IScrollListenerWidget widget) widget.onScroll();});
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private final List<ImmutableTrio<Integer, Integer, Integer>> pressedKeys = new ArrayList<>();

    public List<ImmutableTrio<Integer, Integer, Integer>> getPressedKeys() {
        return List.copyOf(this.pressedKeys);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(!pressedKeys.contains(new ImmutableTrio<>(keyCode, scanCode, modifiers))) pressedKeys.add(new ImmutableTrio<>(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        pressedKeys.remove(new ImmutableTrio<>(keyCode, scanCode, modifiers));
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public boolean isHovering(BabelWidget widget) {
        return this.hoveredWidget.equals(widget);
    }

    public BabelWidget getHoveredWidget() {
        return this.hoveredWidget;
    }

    public boolean isSomethingHovering() {
        return this.hoveredWidget != null;
    }

    public boolean isDragging(BabelWidget widget) {
        return isSomethingDragging() && this.draggedWidget.equals(widget);
    }

    public boolean isFocused(BabelWidget widget) {
        return this.focusedWidget.equals(widget);
    }

    public BabelWidget getDraggedWidget() {
        return this.draggedWidget;
    }

    public boolean isSomethingDragging() {
        return this.draggedWidget != null;
    }

    public boolean isSomethingFocused() {
        return this.focusedWidget != null;
    }

    public boolean isNothingInteracted() {
        return this.draggedWidget == null && this.hoveredWidget == null;
    }

    public Vec2 getMousePos() {
        return this.mousePos;
    }

    public Vec2 getMouseDrag() {
        return this.mouseDrag;
    }

    public void setDragged(BabelWidget widget) {
        this.draggedWidget = widget;
    }

    public void releaseDragged() {
        this.draggedWidget = null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, partialTick);

        if(this.lock) return;

        this.mousePos = new Vec2(mouseX, mouseY);
        this.mouseDrag = this.mousePos.add(new Vec2(-this.mousePosOld.x, -this.mousePosOld.y));

        getChildren().forEach(child -> child.processRendering(guiGraphics, partialTick));

        this.mousePosOld = this.mousePos;
    }

    public void add(BabelWidget widget) {
        if(widget.parent == null) {
            this.children.add(widget);
        } else {
            widget.parent.children.add(widget);
            widget.bounds.depth = widget.parent.bounds.depth + .1f;
        }
        this.descendants.add(widget);

    }

    public void add(List<BabelWidget<?, ?>> widgets) {
        this.children.addAll(widgets);
        this.descendants.addAll(widgets);
    }

    public void destroy(BabelWidget widget) {
        if(widget.isDragging()) this.draggedWidget = null;
        if(widget.isFocused()) this.focusedWidget = null;
        if(widget.isHovering()) this.hoveredWidget = null;
        this.children.remove(widget);
        this.descendants.remove(widget);
    }

    public void renderBackground(GuiGraphics guiGraphics, float partialTick) {};

    @Override
    public void tick() {
        if(lock) return;
        incrementTick();
        getChildren().forEach(BabelWidget::processTicking);
        if(getMouseDrag().equals(Vec2.ZERO)) if(this.tick % 5 == 0) updateHover();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean renderDebug() {
        return false;
    }

}
