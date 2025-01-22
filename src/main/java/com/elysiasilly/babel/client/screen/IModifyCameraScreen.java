package com.elysiasilly.babel.client.screen;

import net.minecraft.client.Camera;

public interface IModifyCameraScreen {

    void camera(Camera camera);

    boolean hideElements();
}
