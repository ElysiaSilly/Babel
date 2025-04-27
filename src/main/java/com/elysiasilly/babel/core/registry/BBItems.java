package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.common.item.FlamethrowerItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Babel.MODID);

    public static final DeferredItem<FlamethrowerItem> FLAMETHROWER = ITEMS.register("flamethrower", () ->
            new FlamethrowerItem(new Item.Properties()));
}
