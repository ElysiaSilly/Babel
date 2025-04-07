package com.elysiasilly.babel.impl.client.render;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.impl.common.item.TankItem;
import com.elysiasilly.babel.impl.registry.BBActors;
import com.elysiasilly.babel.util.MCUtil;
import com.elysiasilly.babel.util.MathUtil;
import com.elysiasilly.babel.util.conversions.VectorConversions;
import com.elysiasilly.babel.util.resource.RGBA;
import com.elysiasilly.babel.util.utils.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.List;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class Misc {

    @SubscribeEvent
    private static void render(RenderLevelStageEvent event) {
        if(event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES)) {

            if(Minecraft.getInstance().player.getMainHandItem().getItem() instanceof TankItem item) {
                MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                PoseStack poseStack = event.getPoseStack();

                Player player = Minecraft.getInstance().player;
                List<Vec3> ray = MCUtil.Raycast.shittyRayCast(Minecraft.getInstance().player, 10, MCUtil.Raycast.GOOD_ENOUGH);

                Vec3 pos = player.isShiftKeyDown() ? ray.getLast() : VectorConversions.toBlockPos(ray.getLast()).getBottomCenter();
                Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
                poseStack.last().pose().translate((float) (pos.x - cam.getPosition().x), (float) (pos.y - cam.getPosition().y), (float) (pos.z - cam.getPosition().z));


                RenderUtil.drawVoxelShape(bufferSource, poseStack, Block.box(-24, 0, -24, 24, 48, 24), false);
            }
        }
    }
}
