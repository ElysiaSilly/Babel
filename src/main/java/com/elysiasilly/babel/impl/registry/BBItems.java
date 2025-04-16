package com.elysiasilly.babel.impl.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.api.common.item.cycleable.DefinedBlockState;
import com.elysiasilly.babel.impl.common.item.TankItem;
import com.elysiasilly.babel.impl.common.item.TestItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Babel.MODID);

    public static final DeferredItem<TestItem> TEST = ITEMS.register("test_item", () ->
            new TestItem(new Item.Properties()));

    public static final DeferredItem<TankItem> TANK = ITEMS.register("tank", () ->
            new TankItem(new Item.Properties()));

    public static final DeferredItem<CycleBlockItem> CYCLE = ITEMS.register("cycle", () ->
            new CycleBlockItem(new Item.Properties(), CycleBlockItem.Mode.RANDOM_AND_CYCLE,
                    new DefinedBlockState(Blocks.PINK_WOOL), new DefinedBlockState(Blocks.LIGHT_BLUE_WOOL), new DefinedBlockState(Blocks.WHITE_WOOL))
            );
}
