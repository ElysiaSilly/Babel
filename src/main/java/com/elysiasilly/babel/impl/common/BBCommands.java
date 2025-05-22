package com.elysiasilly.babel.impl.common;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.task.TaskQueueCommands;
import com.elysiasilly.babel.api.theatre.TheatreCommands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class BBCommands {

    @SubscribeEvent
    private static void register(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext context = event.getBuildContext();
        Commands.CommandSelection selection = event.getCommandSelection();

        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.literal(Babel.MODID);

        ///
        argumentBuilder = TheatreCommands.create(dispatcher, context, selection, argumentBuilder);
        argumentBuilder = TaskQueueCommands.create(dispatcher, context, selection, argumentBuilder);
        ///

        LiteralCommandNode<CommandSourceStack> source = dispatcher.register(argumentBuilder);

        dispatcher.register(Commands.literal(Babel.MODID).redirect(source));
    }
}
