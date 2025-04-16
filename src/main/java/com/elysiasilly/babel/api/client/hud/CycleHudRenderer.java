package com.elysiasilly.babel.api.client.hud;

import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.item.Item;

public class CycleHudRenderer implements LayeredDraw.Layer {

    public static final LayeredDraw.Layer LAYER = new CycleHudRenderer();

    private Item item;

    private CycleHudElement element;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

        Minecraft mc = Minecraft.getInstance();

        if(mc.player != null) {
            if(mc.player.getMainHandItem().getItem() instanceof CycleBlockItem item) {
                if(item != this.item) {
                    this.item = item;
                    this.element = new CycleHudElement(item);
                }
                element.render(guiGraphics);
            } else if(this.item != null) {
                this.item = null;
                this.element = null;
            }
        }
    }
}
