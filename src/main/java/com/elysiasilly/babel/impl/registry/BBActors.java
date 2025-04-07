package com.elysiasilly.babel.impl.registry;

import com.elysiasilly.babel.api.BabelRegistries;
import com.elysiasilly.babel.api.theatre.actor.ActorType;
import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.common.actor.PipeActor;
import com.elysiasilly.babel.impl.common.actor.TankActor;
import com.elysiasilly.babel.impl.common.actor.TankExtensionActor;
import com.elysiasilly.babel.impl.common.actor.TestActor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.nio.channels.Pipe;
import java.util.function.Supplier;

public class BBActors {
    public static final DeferredRegister<ActorType<?>> ACTORS = DeferredRegister.create(BabelRegistries.ACTOR_TYPE, Babel.MODID);

    public static final Supplier<ActorType<TestActor>> TEST_ACTOR =
            ACTORS.register("test_actor", () -> ActorType.Builder.of(TestActor::new).build());

    public static final Supplier<ActorType<TankActor>> TANK =
            ACTORS.register("tank", () -> ActorType.Builder.of(TankActor::new).build());

    public static final Supplier<ActorType<PipeActor>> PIPE =
            ACTORS.register("pipe", () -> ActorType.Builder.of(PipeActor::new).build());

    public static final Supplier<ActorType<TankExtensionActor>> TANK_EXTENSION =
            ACTORS.register("tank_extension", () -> ActorType.Builder.of(TankExtensionActor::new).build());
}
