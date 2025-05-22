package com.elysiasilly.babel.api.registry.entry.block.assets;

import com.elysiasilly.babel.api.common.block.RotatedSlabBlock;
import com.elysiasilly.babel.api.common.block.StairsBlock;
import com.elysiasilly.babel.api.registry.entry.block.BlockAssetBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.joml.Vector2i;

public class Presets {

    public static BlockAssetBuilder<Block> block(DeferredBlock<Block> block, String tAll, String output) {
        return new BlockAssetBuilder<>(block)
                .item(BlockAssetBuilder.parentedItem("model"))

                .model("model", BlockAssetBuilder.model()
                        .output(output)
                        .texture(tAll, "all")
                        .parent("cube_all")
                )
                .state(BlockAssetBuilder.variantState()
                        .add(state -> true, state -> BlockAssetBuilder.variantStateEntry("model"))
                );
    }

    public static BlockAssetBuilder<RotatedPillarBlock> pillar(DeferredBlock<RotatedPillarBlock> block, String tSide, String tEnd, String output) {
        return new BlockAssetBuilder<>(block)
                .item(BlockAssetBuilder.parentedItem("model"))
                .model("model", BlockAssetBuilder.model()
                        .output(output)
                        .texture(tEnd, "end")
                        .texture(tSide, "side")
                        .parent("cube_column")
                )
                .state(BlockAssetBuilder.variantState()
                        .add(state -> true, state -> {
                            Vector2i rot = axisHelper(state.getValue(RotatedPillarBlock.AXIS));
                            return BlockAssetBuilder.variantStateEntry("model").rotX(rot.x).rotY(rot.y);
                        })
                );
    }

    public static BlockAssetBuilder<RotatedSlabBlock> pillar_slab(DeferredBlock<RotatedSlabBlock> block, String tSide, String tEnd, String output) {
        return new BlockAssetBuilder<>(block)
                .item(BlockAssetBuilder.parentedItem("bottom"))

                .model("bottom", BlockAssetBuilder.model()
                        .output(output + "/bottom")
                        .textures(tEnd, "bottom", "top")
                        .texture(tSide, "side")
                        .parent("slab")
                )
                .model("top", BlockAssetBuilder.model()
                        .output(output + "/top")
                        .textures(tEnd, "bottom", "top")
                        .texture(tSide, "side")
                        .parent("slab_top")
                )
                .model("double", BlockAssetBuilder.model()
                        .output(output + "/double")
                        .texture(tEnd, "end")
                        .texture(tSide, "side")
                        .parent("cube_column")
                )
                .state(BlockAssetBuilder.variantState()
                        .add(state -> true, state -> {
                            Vector2i rot = axisHelper(state.getValue(RotatedSlabBlock.AXIS));

                            return switch(state.getValue(RotatedSlabBlock.TYPE)) {
                                case TOP -> BlockAssetBuilder.variantStateEntry("top").rotX(rot.x).rotY(rot.y);
                                case BOTTOM -> BlockAssetBuilder.variantStateEntry("bottom").rotX(rot.x).rotY(rot.y);
                                case DOUBLE -> BlockAssetBuilder.variantStateEntry("double").rotX(rot.x).rotY(rot.y);
                            };
                        })
                );
    }

    private static Vector2i axisHelper(Direction.Axis axis) {
        return switch(axis) {
            case X -> new Vector2i(90, 90);
            case Y -> new Vector2i();
            case Z -> new Vector2i(90, 0);
        };
    }

    public static BlockAssetBuilder<SlabBlock> slab(DeferredBlock<SlabBlock> block, String tAll, String output) {
        return slab(block, tAll, tAll, output);
    }

    public static BlockAssetBuilder<SlabBlock> slab(DeferredBlock<SlabBlock> block, String tSide, String tEnd, String output) {
        return new BlockAssetBuilder<>(block)
                .item(BlockAssetBuilder.parentedItem("bottom"))

                .model("bottom", BlockAssetBuilder.model()
                        .output(output + "/bottom")
                        .textures(tEnd, "bottom", "top")
                        .texture(tSide, "side")
                        .parent("slab")
                )
                .model("top", BlockAssetBuilder.model()
                        .output(output + "/top")
                        .textures(tEnd, "bottom", "top")
                        .texture(tSide, "side")
                        .parent("slab_top")
                )
                .model("double", BlockAssetBuilder.model()
                        .output(output + "/double")
                        .texture(tEnd, "end")
                        .texture(tSide, "side")
                        .parent("cube_column")
                )
                .state(BlockAssetBuilder.variantState()
                        .add(state -> state.getValue(SlabBlock.TYPE).equals(SlabType.BOTTOM), state -> BlockAssetBuilder.variantStateEntry("bottom"))
                        .add(state -> state.getValue(SlabBlock.TYPE).equals(SlabType.TOP), state -> BlockAssetBuilder.variantStateEntry("top"))
                        .add(state -> state.getValue(SlabBlock.TYPE).equals(SlabType.DOUBLE), state -> BlockAssetBuilder.variantStateEntry("double"))
                );
    }

    public static BlockAssetBuilder<StairsBlock> stairs(DeferredBlock<StairsBlock> block, String tAll, String output) {
        return stairs(block, tAll, tAll, output);
    }

    public static BlockAssetBuilder<StairsBlock> stairs(DeferredBlock<StairsBlock> block, String tSide, String tEnd, String output) {
        return new BlockAssetBuilder<>(block)
                .item(BlockAssetBuilder.parentedItem("normal"))

                .model("normal", BlockAssetBuilder.model()
                        .output(output + "/normal")
                        .texture(tSide, "side")
                        .textures(tEnd, "bottom", "top")
                        .parent("stairs")
                )
                .model("inner", BlockAssetBuilder.model()
                        .output(output + "/inner")
                        .texture(tSide, "side")
                        .textures(tEnd, "bottom", "top")
                        .parent("inner_stairs")
                )
                .model("outer", BlockAssetBuilder.model()
                        .output(output + "/outer")
                        .texture(tSide, "side")
                        .textures(tEnd, "bottom", "top")
                        .parent("outer_stairs")
                )
                .state(BlockAssetBuilder.variantState()
                        .add(state -> /*!state.getValue(StairsBlock.WATERLOGGED)*/ true, state -> {

                            Direction facing = state.getValue(StairBlock.FACING);
                            Half half = state.getValue(StairBlock.HALF);
                            StairsShape shape = state.getValue(StairBlock.SHAPE);

                            int yRot = (int) facing.getClockWise().toYRot();

                            if(shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) yRot += 270;

                            if(shape != StairsShape.STRAIGHT && half == Half.TOP) yRot += 90;

                            yRot %= 360;
                            boolean uvlock = yRot != 0 || half == Half.TOP;

                            return BlockAssetBuilder.variantStateEntry(shape == StairsShape.STRAIGHT ? "normal" : shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? "inner" : "outer")
                                    .rotX(half == Half.BOTTOM ? 0 : 180)
                                    .rotY(yRot)
                                    .uvLock(uvlock);
                        })
                );
    }
}
