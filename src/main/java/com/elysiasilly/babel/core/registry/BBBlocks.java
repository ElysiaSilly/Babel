package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.common.block.TrophyBlock;
import com.elysiasilly.babel.core.Babel;
import com.elysiasilly.babel.util.MCUtil;
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
        if(MCUtil.Dev.isModPresent("calvariae")) {
            CAVERS_TROPHY = regWithItem("cavers_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }
        if(MCUtil.Dev.isModPresent("factory_expansion")) {
            BUILDERS_TROPHY = regWithItem("builders_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }
        if(MCUtil.Dev.isModPresent("musica_universalis")) {
            INQUISITIVES_TROPHY = regWithItem("inquisitives_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of()));
        }

        if(MCUtil.Dev.isModsPresent("calvariae", "factory_expansions", "musica_universalis")) {
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
