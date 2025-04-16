package com.elysiasilly.babel.impl.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.storage.ChunkData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BBAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Babel.MODID);

    public static final Supplier<AttachmentType<ChunkData>> SCENE_DATA = ATTACHMENTS.register(
            "scene_data", () -> AttachmentType.builder(() -> ChunkData.EMPTY).serialize(ChunkData.CODEC).build()
    );
}
