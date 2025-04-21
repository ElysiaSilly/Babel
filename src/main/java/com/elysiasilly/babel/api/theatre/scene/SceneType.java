package com.elysiasilly.babel.api.theatre.scene;

import com.elysiasilly.babel.core.BBRegistries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class SceneType<C extends ClientScene, S extends ServerScene> {

    private final ClientSceneFactory<? extends C> clientSceneFactory;
    private final ServerSceneFactory<? extends S> serverSceneFactory;

    private final Tick tick;

    public SceneType(ClientSceneFactory<? extends C> clientSceneFactory, ServerSceneFactory<? extends S> serverSceneFactory, Tick tick) {
        this.clientSceneFactory = clientSceneFactory;
        this.serverSceneFactory = serverSceneFactory;
        this.tick = tick;
    }

    public ResourceLocation getKey() {
        return getKey(this);
    }

    public static ResourceLocation getKey(SceneType<?, ?> sceneType) {
        return BBRegistries.SCENE_TYPE.getKey(sceneType);
    }

    public C createClient(ClientLevel client) {
        return this.clientSceneFactory.create(client);
    }

    public S createServer(ServerLevel server) {
        return this.serverSceneFactory.create(server);
    }

    public Tick tick() {
        return this.tick;
    }

    public boolean tickPre() {
        return this.tick.equals(Tick.PRE);
    }

    public boolean tickPost() {
        return this.tick.equals(Tick.POST);
    }

    public enum Tick { PRE, POST, NONE }

    public static class Builder<C extends ClientScene, S extends ServerScene> {

        private final ClientSceneFactory<C> clientSceneFactory;
        private final ServerSceneFactory<S> serverSceneFactory;

        private Tick tick = Tick.NONE;

        private Builder(ClientSceneFactory<C> clientSceneFactory, ServerSceneFactory<S> serverSceneFactory) {
            this.clientSceneFactory = clientSceneFactory;
            this.serverSceneFactory = serverSceneFactory;
        }

        public static <C extends ClientScene, S extends ServerScene> Builder<C,S> of(ClientSceneFactory<C> clientSceneFactory, ServerSceneFactory<S> serverSceneFactory) {
            return new Builder<>(clientSceneFactory, serverSceneFactory);
        }

        public SceneType<C, S> build() {
            return new SceneType<>(this.clientSceneFactory, this.serverSceneFactory, this.tick);
        }

        public Builder<C, S> tick(Tick tick) {
            this.tick = tick; return this;
        }
    }

    @FunctionalInterface
    public interface ClientSceneFactory<C extends ClientScene> {
        C create(ClientLevel client);
    }

    @FunctionalInterface
    public interface ServerSceneFactory<S extends ServerScene> {
        S create(ServerLevel server);
    }
}
