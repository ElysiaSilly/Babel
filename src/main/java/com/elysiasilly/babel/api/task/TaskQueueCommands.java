package com.elysiasilly.babel.api.task;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TaskQueueCommands {

    public static LiteralArgumentBuilder<CommandSourceStack> create(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection, LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {
        return argumentBuilder.then(Commands.literal("taskque"));
    }
}
