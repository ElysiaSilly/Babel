package com.elysiasilly.babel.core.registry;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.dbi.DynamicBlockItemComponent;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;
import java.util.function.Supplier;

public class BBComponents {

    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Babel.MODID);

    public static final Supplier<DataComponentType<DynamicBlockItemComponent>> DYNAMIC_BLOCK_ITEM = COMPONENTS.registerComponentType("dbi",
            builder -> builder.persistent(DynamicBlockItemComponent.CODEC).networkSynchronized(DynamicBlockItemComponent.STREAM_CODEC));

    /// GENERIC

    public static final Supplier<DataComponentType<BlockPos>> BLOCKPOS = COMPONENTS.registerComponentType("blockpos",
            builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC));

    public static final Supplier<DataComponentType<UUID>> UUID = COMPONENTS.registerComponentType("uuid",
            builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));

    public static final Supplier<DataComponentType<Boolean>> BOOL = COMPONENTS.registerComponentType("bool",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final Supplier<DataComponentType<Integer>> INT = COMPONENTS.registerComponentType("int",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final Supplier<DataComponentType<Float>> FLOAT = COMPONENTS.registerComponentType("float",
            builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
}
