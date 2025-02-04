package com.elysiasilly.babel.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LexiconItem extends Item {

    public LexiconItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack);
    }
}
