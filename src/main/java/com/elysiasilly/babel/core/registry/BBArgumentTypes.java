package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.actor.command.ActorArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENTS = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Babel.MODID);

    public static final Supplier<ArgumentTypeInfo<ActorArgument, ?>> ACTOR = ARGUMENTS.register(
            "actor", () -> ArgumentTypeInfos.registerByClass(ActorArgument.class, SingletonArgumentInfo.contextFree(ActorArgument::actors))
    );
}
