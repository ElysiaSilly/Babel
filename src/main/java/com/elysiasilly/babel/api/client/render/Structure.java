package com.elysiasilly.babel.api.client.render;

import com.elysiasilly.babel.util.misc.MutablePair;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class Structure {

    public static class Data extends MutablePair<BlockState, Boolean> {
        public Data(BlockState state, Boolean render) {
            super(state, render);
        }

        public static class codec{
            public static final Codec<Data> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BlockState.CODEC.fieldOf("state").forGetter(i -> i.first),
                    Codec.BOOL.fieldOf("render").forGetter(i -> i.second)
            ).apply(instance, Data::new));
        }
    }

    final Map<BlockPos, Data> structure = new HashMap<>();

    public static class codec {

        // ):
        public static final Codec<Map<BlockPos, Data>> MAP = Codec.unboundedMap(
                Codec.STRING.flatXmap(s -> {
                            String[] parts = s.split(",");
                            if (parts.length == 3) {
                                try {
                                    return DataResult.success(new BlockPos(
                                            Integer.parseInt(parts[0]),
                                            Integer.parseInt(parts[1]),
                                            Integer.parseInt(parts[2])
                                    ));
                                } catch (NumberFormatException e) {
                                    return DataResult.error(() -> "Invalid input: " + s + "(" + e.getMessage() + ")");
                                }
                            }
                            return DataResult.error(() -> "Invalid input: " + s);
                        },
                        p -> DataResult.success(p.getX() + "," + p.getY() + "," + p.getZ())
                ),
                Data.codec.CODEC
        );

        public static final Codec<Structure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MAP.fieldOf("structure").forGetter(i -> i.structure)
        ).apply(instance, Structure::new));
    }

    public Structure(Map<BlockPos, Data> structure) {
        this.structure.putAll(structure);
    }

    public void put(BlockPos pos, BlockState state) {
        this.structure.put(pos, new Data(state, true));
    }

    public void compute() {
        for(Map.Entry<BlockPos, Data> entry : this.structure.entrySet()) {
            BlockPos pos = entry.getKey();
            if(
                    this.structure.containsKey(pos.below()) &&
                            this.structure.containsKey(pos.above()) &&
                            this.structure.containsKey(pos.north()) &&
                            this.structure.containsKey(pos.east())  &&
                            this.structure.containsKey(pos.south()) &&
                            this.structure.containsKey(pos.west())
            ) {
                this.structure.get(pos).second = false;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void render(PoseStack pose, MultiBufferSource.BufferSource source) {
        for(Map.Entry<BlockPos, Data> entry : this.structure.entrySet()) {
            if(entry.getValue().second) {
                pose.pushPose();
                pose.translate(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
                // todo : batched rendering (AO, culling, and etc)

                //RenderSystem.enableBlend();
                //RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                //RenderSystem.setShaderColor(1, .5f, .5f, 1);

                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(entry.getValue().first, pose, source, LightTexture.FULL_SKY, OverlayTexture.NO_OVERLAY);
                pose.popPose();
            }
        }
    }
}
