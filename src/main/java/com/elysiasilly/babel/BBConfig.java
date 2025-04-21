package com.elysiasilly.babel;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BBConfig {

    public static ModConfigSpec CLIENT_CONFIG;

    public enum CycleableHudStyle { DEFAULT, CIRCULAR }
    public static ModConfigSpec.EnumValue<CycleableHudStyle> CYCLEABLE_HUD_STYLE;

    public enum CycleableHudPosition { TOP, LEFT, RIGHT, CENTRE }
    public static ModConfigSpec.EnumValue<CycleableHudPosition> CYCLEABLE_HUD_POSITION;

    public static ModConfigSpec.BooleanValue CYCLEABLE_STYLIZED_ICONS;

    static {
        ModConfigSpec.Builder CLIENT = new ModConfigSpec.Builder();

        CLIENT.push("cycleable");

        CYCLEABLE_HUD_STYLE = CLIENT.defineEnum("cycleableHudStyle", CycleableHudStyle.DEFAULT);
        CYCLEABLE_HUD_POSITION = CLIENT.defineEnum("cycleableHudPosition", CycleableHudPosition.TOP);
        CYCLEABLE_STYLIZED_ICONS = CLIENT.define("cycleableStylizedIcons", true);

        CLIENT.pop();

        CLIENT_CONFIG = CLIENT.build();
    }

    public static ModConfigSpec COMMON_CONFIG;

    static {
        ModConfigSpec.Builder COMMON = new ModConfigSpec.Builder();
        COMMON_CONFIG = COMMON.build();
    }
}
