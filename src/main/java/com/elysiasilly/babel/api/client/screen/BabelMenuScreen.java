package com.elysiasilly.babel.api.client.screen;

import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public abstract class BabelMenuScreen<M extends AbstractContainerMenu> extends BabelScreen implements MenuAccess<M> {

    final M MENU;

    public BabelMenuScreen(M menu) {
        this.MENU = menu;
    }

    public @NotNull M getMenu() {
        return this.MENU;
    }

}
