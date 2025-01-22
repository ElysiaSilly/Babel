package com.elysiasilly.babel.common.interactible;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class Interactable<M extends InteractableManager> {

    public M manager;

    public abstract void render(RenderLevelStageEvent event);

    public abstract void tick(Level level);

    public void destroy() {
        manager.destroy(this);
    }

    public VoxelShape getShape() {
        return Shapes.block();
    }

    public void setDirty() {
        this.manager.setDirty();
    }
}
