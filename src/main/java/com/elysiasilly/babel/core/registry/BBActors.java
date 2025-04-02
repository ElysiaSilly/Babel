package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.theatre.actor.ActorType;
import com.elysiasilly.babel.common.actor.TestActor;
import com.elysiasilly.babel.core.Babel;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BBActors {
    public static final DeferredRegister<ActorType<?>> ACTORS = DeferredRegister.create(BabelRegistries.ACTOR_TYPE, Babel.MODID);

    public static final Supplier<ActorType<TestActor>> TEST_ACTOR =
            ACTORS.register("test_actor", () -> ActorType.Builder.of(TestActor::new).build());
}
