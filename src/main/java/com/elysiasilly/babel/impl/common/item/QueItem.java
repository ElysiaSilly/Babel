package com.elysiasilly.babel.impl.common.item;

import com.elysiasilly.babel.api.task.TaskQueue;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.UUID;

public class QueItem extends Item {

    public QueItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        BlockPos pos = player.blockPosition();

        UUID uuid1 = TaskQueue.scheduleTicks(level, 20, () -> level.setBlock(pos, Blocks.YELLOW_WOOL.defaultBlockState(), 3));
        UUID uuid2 = TaskQueue.scheduleMinutes(level, 1, () -> player.displayClientMessage(Component.literal("It has been one (1) minute"), false));

        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
