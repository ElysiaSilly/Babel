package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.common.item.TestItem;
import com.elysiasilly.babel.core.Babel;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Babel.MODID);

    public static final DeferredItem<TestItem> TEST =
            ITEMS.register("test_item", () ->
                    new TestItem(new Item.Properties())
            );
}
