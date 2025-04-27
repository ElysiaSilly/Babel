package com.elysiasilly.babel.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class Registrar<T> {

    private final ResourceKey<Registry<T>> key;
    private final Registry<T> registry;

    public Registrar(ResourceLocation id, boolean sync) {
        this.key = ResourceKey.createRegistryKey(id);
        this.registry =  new RegistryBuilder<>(key()).sync(sync).create();
    }

    public ResourceKey<Registry<T>> key() {
        return this.key;
    }

    public Registry<T> get() {
        return this.registry;
    }
}
