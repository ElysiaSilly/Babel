package com.elysiasilly.babel.impl.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.common.block.TrophyBlock;
import com.elysiasilly.babel.util.utils.DevUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Babel.MODID);
    public static final DeferredRegister.Items BLOCKITEMS = DeferredRegister.createItems(Babel.MODID);

    public static DeferredBlock<Block> CAVERS_TROPHY;
    public static DeferredBlock<Block> BUILDERS_TROPHY;
    public static DeferredBlock<Block> INQUISITIVES_TROPHY;
    public static DeferredBlock<Block> BABEL_TROPHY;

    public static void reg() {
        if(DevUtil.isModPresent("calvariae")) {
            CAVERS_TROPHY = regWithItem("cavers_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }
        if(DevUtil.isModPresent("factory_expansion")) {
            BUILDERS_TROPHY = regWithItem("builders_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }
        if(DevUtil.isModPresent("musica_universalis")) {
            INQUISITIVES_TROPHY = regWithItem("inquisitives_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }

        if(DevUtil.isModsPresent("calvariae", "factory_expansions", "musica_universalis")) {
            BABEL_TROPHY = regWithItem("babel_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }
    }

    /// register block and item
    @SuppressWarnings({"unchecked"})
    private static <T extends Block> DeferredBlock<T> regWithItem(String id, Supplier<? extends Block> blockType) {
        var tempBlock = BLOCKS.register(id, blockType);
        BLOCKITEMS.registerSimpleBlockItem(tempBlock);
        return (DeferredBlock<T>) tempBlock;
    }

}
