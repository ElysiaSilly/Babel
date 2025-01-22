package com.elysiasilly.babel.client.screen.widget;

import net.minecraft.nbt.CompoundTag;

public interface ISavableWidget {

    CompoundTag serialize();

    void deserialize(CompoundTag tag);

    String ID();

}
