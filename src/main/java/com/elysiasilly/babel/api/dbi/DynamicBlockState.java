package com.elysiasilly.babel.api.dbi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DynamicBlockState {

    private final Block block;

    private final float cost;

    private final boolean context;

    private final Map<Property<?>, Object> propertyValueMap = new HashMap<>();

    private final Map<String, String> properties = new HashMap<>();

    public static final Codec<DynamicBlockState> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(DynamicBlockState::block),
            Codec.FLOAT.fieldOf("cost").forGetter(i -> i.cost),
            Codec.BOOL.fieldOf("context").forGetter(i -> i.context),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("properties").forGetter(DynamicBlockState::properties)
            ).apply(builder, DynamicBlockState::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DynamicBlockState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(BuiltInRegistries.BLOCK.key()), DynamicBlockState::block,
            ByteBufCodecs.FLOAT, DynamicBlockState::cost,
            ByteBufCodecs.BOOL, DynamicBlockState::context,
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.STRING_UTF8), DynamicBlockState::properties,
            DynamicBlockState::new
    );


    /// Map<Property<?>, Value<?>>
    public DynamicBlockState(Block block, float cost, boolean context, Map<String, String> propertyValueMap) {

        this.block = block;
        this.cost = cost;
        this.context = context;

        Map<String, Property<?>> propertiesByName = new HashMap<>();

        for(Property<?> property: block.defaultBlockState().getProperties()) {
            propertiesByName.put(property.getName(), property);
        }

        for(Map.Entry<String, String> propertyValue : propertyValueMap.entrySet()) {
            if(propertiesByName.containsKey(propertyValue.getKey())) {
                Property<?> property = propertiesByName.get(propertyValue.getKey());

                if(this.propertyValueMap.containsKey(property)) {
                    throw new IllegalStateException(String.format("Property '%s' already defined for Block '%s'", property, blockId()));
                } else {
                     Stream<? extends Property.Value<?>> values = property.getAllValues();

                     Map<String, Property.Value<?>> valuesByName = new HashMap<>();

                     for(Property.Value<?> value : values.toList()) {
                         valuesByName.put(value.toString(), value);
                     }

                     String key = propertyValue.getKey() + "=" + propertyValue.getValue();

                     if(valuesByName.containsKey(key)) {
                         this.propertyValueMap.put(property, valuesByName.get(key).value());
                     } else {
                         throw new IllegalStateException(String.format("Could not find Value '%s' for Property '%s'", propertyValue.getValue(), propertyValue.getValue()));
                     }
                }
            } else {
                throw new IllegalStateException(String.format("Could not find Property '%s' for Block '%s'", propertyValue.getKey(), blockId()));
            }
        }
    }

    public String blockId() {
        return BuiltInRegistries.BLOCK.wrapAsHolder(this.block).getRegisteredName();
    }

    public float cost() {
        return this.cost;
    }

    public boolean context() {
        return this.context;
    }

    public Block block() {
        return this.block;
    }

    public Map<String, String> properties() {
        return this.properties;
    }

    @Nullable
    public BlockState state() {
        return state(null);
    }

    @SuppressWarnings({"unchecked"}) @Nullable
    public <P extends Comparable<P>, V extends P> BlockState state(@Nullable BlockPlaceContext context) {
        BlockState state = context() && context != null ? block().getStateForPlacement(context) : block().defaultBlockState();

        if(state == null) return null;

        for(Map.Entry<Property<?>, Object> property : propertyValueMap.entrySet()) {
            state = state.setValue((Property<P>) property.getKey(), (V) property.getValue());
        }

        return state;
    }
}
