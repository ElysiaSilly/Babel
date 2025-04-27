package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.attachment.ChunkData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BBAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Babel.MODID);

    public static final Supplier<AttachmentType<ChunkData>> SCENE_DATA = ATTACHMENTS.register(
            "scene_data", () -> AttachmentType.builder(() -> ChunkData.EMPTY).serialize(ChunkData.CODEC).build()
    );

    /*
    public static final Supplier<AttachmentType<List<Actor.Data>>> TEST = ATTACHMENTS.register(
            "test", () -> AttachmentType.builder(() -> List.of()).serialize(Codec.list(Actor.ACTOR_DATA_CODEC))
    )

     */
}
