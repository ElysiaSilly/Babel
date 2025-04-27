package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.api.theatre.actor.registry.DeferredActor;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.impl.common.actor.FlamethrowerActor;
import com.elysiasilly.babel.impl.common.actor.TestActor;

public class BBActors {
    public static final BBRegistries.Actors ACTORS = BBRegistries.createActors(Babel.MODID);

    public static final DeferredActor<TestActor> TEST_ACTOR =
            ACTORS.registerActor("test_actor", () -> ActorType.Builder.of(TestActor::new, BBScenes.BUILTIN).build());

    public static final DeferredActor<FlamethrowerActor> FLAMETHROWER =
            ACTORS.registerActor("flamethrower", () -> ActorType.Builder.of(FlamethrowerActor::new, BBScenes.BUILTIN).build());
}
