package com.elysiasilly.babel.api.client.hud;

import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.item.Item;

public class CycleHudRenderer implements LayeredDraw.Layer {

    public static final CycleHudRenderer LAYER = new CycleHudRenderer();

    private Item item;

    private CycleHudElement element;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

        Minecraft mc = Minecraft.getInstance();

        if(mc.player != null) {
            if(mc.player.getMainHandItem().getItem() instanceof CycleBlockItem item) {
                if(item != item()) {
                    item(item);
                    element(new CycleHudElement(item));
                    tick(200);
                }
                element.render(guiGraphics);
            } else if(item() != null) {
                item(null);
                element(null);
            }
        }
    }

    public void element(CycleHudElement element) {
        this.element = element;
    }

    public void item(Item item) {
        this.item = item;
    }

    public CycleHudElement element() {
        return this.element;
    }

    public Item item() {
        return this.item;
    }

    public boolean resetIdle() {
        tick(0); return true;
    }

    public boolean idle() {
        return element().idle();
    }

    public void tick(int tick) {
        element().tick(tick);
    }
}
