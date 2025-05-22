package com.elysiasilly.babel.api.client.screen.old.widget;

import net.minecraft.nbt.CompoundTag;

public interface ISavableWidget {

    void serialize(CompoundTag tag);

    void deserialize(CompoundTag tag);

    String ID();

}
