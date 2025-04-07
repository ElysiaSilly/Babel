package com.elysiasilly.babel.impl.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.common.item.TankItem;
import com.elysiasilly.babel.impl.common.item.TestItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Babel.MODID);

    public static final DeferredItem<TestItem> TEST = ITEMS.register("test_item", () ->
            new TestItem(new Item.Properties()));

    public static final DeferredItem<TankItem> TANK = ITEMS.register("tank", () ->
            new TankItem(new Item.Properties()));
}
