package com.elysiasilly.babel.api.dbi;

import net.minecraft.util.StringRepresentable;

public enum CycleMode implements StringRepresentable {
    RANDOM_ONLY("random"), CYCLE_ONLY("cycle"), RANDOM_AND_CYCLE("both");

    private final String string;

    CycleMode(String string) {
        this.string = string;
    }

    @Override
    public String getSerializedName() {
        return string;
    }
}
