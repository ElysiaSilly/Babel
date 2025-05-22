package com.elysiasilly.babel.api.registry;

import com.elysiasilly.babel.api.registry.entry.block.BlockEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked"})
public class Registrar {

    private final DeferredRegister.Blocks blocks;
    private final DeferredRegister.Items blockItem;

    public Registrar(String modId) {
        this.blocks = DeferredRegister.createBlocks(modId);
        this.blockItem = DeferredRegister.createItems(modId);
    }
    
    public void register(IEventBus bus) {
        blockRegistry().register(bus);
        blockItemRegistry().register(bus);
    }
    
    public DeferredRegister.Blocks blockRegistry() {
        return this.blocks;
    }
    
    public DeferredRegister.Items blockItemRegistry() {
        return this.blockItem;
    }


    public <B extends Block> BlockEntry.Builder<B> blockEntry(String id, Supplier<B> block) {
        return BlockEntry.Builder.of(id, block, this);
    }

    /// ITEM STUFF

    public void blockItems(DeferredBlock<?> ...blocks) {
        for(DeferredBlock<?> block : blocks) {
            blockItemRegistry().registerSimpleBlockItem(block);
        }
    }

    /// BLOCK STUFF

    // register block
    public <T extends Block> DeferredBlock<T> block(String id, Supplier<? extends Block> blockType) {
        return (DeferredBlock<T>) blockRegistry().register(id, blockType);
    }


    // register block with item
    public <T extends Block> DeferredBlock<T> blockWithItem(String id, Supplier<? extends Block> blockType) {
        var tempBlock = blockRegistry().register(id, blockType);
        blockItemRegistry().registerSimpleBlockItem(tempBlock);
        return (DeferredBlock<T>) tempBlock;
    }

    // register blocks
    public <T extends Block> List<DeferredBlock<T>> blocks(Supplier<? extends Block> blockType, String...ids) {
        List<DeferredBlock<T>> list = new ArrayList<>();

        for(String id : ids) {
            list.add((DeferredBlock<T>) blockRegistry().register(id, blockType));
        }

        return list;
    }

    // register blocks with item
    public <T extends Block> List<DeferredBlock<T>> blocksWithItem(Supplier<? extends Block> blockType, String...ids) {
        List<DeferredBlock<T>> list = new ArrayList<>();

        for(String id : ids) {
            list.add(blockWithItem(id, blockType));
        }

        return list;
    }

    /// BLOCKSET STUFF

    /*
    public SimpleBlockSet.Entries simpleBlockSet(SimpleBlockSet set, BlockBehaviour.Properties properties) {

        DeferredBlock<Block>
                block = set.block() ? blockWithItem(String.format("%s_%s", set.name(), "block"), () -> new Block(properties)) : null;

        DeferredBlock<StairsBlock>
                stairs = set.stairs() ? blockWithItem(String.format("%s_%s", set.name(), "stairs"), () -> new StairsBlock(properties)) : null;

        DeferredBlock<SlabBlock>
                slab = set.slab() ? blockWithItem(String.format("%s_%s", set.name(), "slab"), () -> new SlabBlock(properties)) : null;

        DeferredBlock<WallBlock>
                wall = set.wall() ? blockWithItem(String.format("%s_%s", set.name(), "wall"), () -> new WallBlock(properties)) : null;

        return new SimpleBlockSet.Entries(set.name(), block, stairs, slab, wall);
    }

     */

    /// register colour set of block and item
    public <T extends Block> Map<DyeColor, DeferredBlock<T>> dyeSet(String id, Supplier<? extends Block> blockType) {
        Map<DyeColor, DeferredBlock<T>> map = new Hashtable<>();

        for(DyeColor colour : DyeColor.values()) {
            map.put(colour, blockWithItem(String.format(id, colour.getName()), blockType));
        }

        return map;
    }

    /// HELPERS

    public static BlockBehaviour.Properties property(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    public static BlockBehaviour.Properties property(DeferredBlock<Block> block) {
        return BlockBehaviour.Properties.ofFullCopy(block.get());
    }

    public static BlockBehaviour.Properties property() {
        return BlockBehaviour.Properties.of();
    }

    public static BlockState state(DeferredBlock<Block> block) {
        return block.get().defaultBlockState();
    }

    public static BlockState state(Block block) {
        return block.defaultBlockState();
    }
}
