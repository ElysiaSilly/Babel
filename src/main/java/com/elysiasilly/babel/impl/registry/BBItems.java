package com.elysiasilly.babel.impl.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.common.item.cycleable.CycleBlockItem;
import com.elysiasilly.babel.api.common.item.cycleable.PredefinedBlockState;
import com.elysiasilly.babel.impl.common.item.TankItem;
import com.elysiasilly.babel.impl.common.item.TestItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BBItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Babel.MODID);

    public static final DeferredItem<TestItem> TEST = ITEMS.register("test_item", () ->
            new TestItem(new Item.Properties()));

    public static final DeferredItem<TankItem> TANK = ITEMS.register("tank", () ->
            new TankItem(new Item.Properties()));

    ///

    public static final DeferredItem<CycleBlockItem> TRANS = ITEMS.register("transgender", () ->
            new CycleBlockItem(new Item.Properties(), CycleBlockItem.Mode.RANDOM_AND_CYCLE,
                    new PredefinedBlockState(Blocks.PINK_WOOL),
                    new PredefinedBlockState(Blocks.LIGHT_BLUE_WOOL),
                    new PredefinedBlockState(Blocks.WHITE_WOOL)
            ));

    public static final DeferredItem<CycleBlockItem> ABC = ITEMS.register("test", () ->
            new CycleBlockItem(new Item.Properties(), CycleBlockItem.Mode.RANDOM_AND_CYCLE,
                    new PredefinedBlockState(Blocks.REDSTONE_LAMP).set(RedstoneLampBlock.LIT, true),
                    new PredefinedBlockState(Blocks.REDSTONE_LAMP),
                    new PredefinedBlockState(Blocks.SCULK),
                    new PredefinedBlockState(Blocks.CALIBRATED_SCULK_SENSOR),
                    new PredefinedBlockState(Blocks.PISTON).set(PistonBaseBlock.FACING, Direction.UP)
            ));

    public static final DeferredItem<CycleBlockItem> STAIRS = ITEMS.register("stairs", () ->
            new CycleBlockItem(new Item.Properties(), CycleBlockItem.Mode.RANDOM_AND_CYCLE,
                    new PredefinedBlockState(Blocks.BRICK_STAIRS).set(StairBlock.FACING, Direction.NORTH),
                    new PredefinedBlockState(Blocks.BRICK_STAIRS).set(StairBlock.FACING, Direction.EAST),
                    new PredefinedBlockState(Blocks.BRICK_STAIRS).set(StairBlock.FACING, Direction.SOUTH),
                    new PredefinedBlockState(Blocks.BRICK_STAIRS).set(StairBlock.FACING, Direction.WEST),
                    new PredefinedBlockState(Blocks.BRICK_STAIRS).set(StairBlock.WATERLOGGED, true).noContext()
            ));
}
