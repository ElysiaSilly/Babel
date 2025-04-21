package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.common.block.TrophyBlock;
import com.elysiasilly.babel.util.utils.DevUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Babel.MODID);
    public static final DeferredRegister.Items BLOCKITEMS = DeferredRegister.createItems(Babel.MODID);

    public enum Mod { FNE, CALVARIAE, MUSICA_UNIVERSALIS, BABEL }

    public static final Map<Mod, DeferredBlock<TrophyBlock>> TROPHIES = new HashMap<>();

    public static void reg() {
        int mods = Mod.values().length, index = 0;

        for(Mod mod : Mod.values()) {
            String modid = mod.toString().toLowerCase();
            if(DevUtil.isModPresent(modid) && !mod.equals(Mod.BABEL)) {
               TROPHIES.put(mod, regWithItem(modid + "_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of())));
               index++;
            }
        }

        if(index == mods) {
            TROPHIES.put(Mod.BABEL, regWithItem("babel_trophy", () -> new TrophyBlock(BlockBehaviour.Properties.of())));
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
