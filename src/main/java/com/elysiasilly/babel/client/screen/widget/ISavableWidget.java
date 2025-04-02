package com.elysiasilly.babel.client.screen.widget;

import net.minecraft.nbt.CompoundTag;

public interface ISavableWidget {

    void serialize(CompoundTag tag);

    void deserialize(CompoundTag tag);

    String ID();

}
