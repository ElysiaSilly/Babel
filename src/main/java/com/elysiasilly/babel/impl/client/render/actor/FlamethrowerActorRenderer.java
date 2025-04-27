package com.elysiasilly.babel.impl.client.render.actor;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.client.model.ModelLoader;
import com.elysiasilly.babel.api.client.model.Util;
import com.elysiasilly.babel.api.client.model.resources.CubeModelElement;
import com.elysiasilly.babel.api.client.model.resources.ModelElement;
import com.elysiasilly.babel.api.client.model.resources.model.Model;
import com.elysiasilly.babel.api.theatre.actor.render.ActorRenderer;
import com.elysiasilly.babel.impl.common.actor.FlamethrowerActor;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FlamethrowerActorRenderer implements ActorRenderer<FlamethrowerActor> {

    @Override
    public void render(FlamethrowerActor actor, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {

        Model model = ModelLoader.model(Babel.location("models/neo_flamethrower.bbmodel"));


        for(ModelElement object : model.getElements()) {
            if(object instanceof CubeModelElement cube) {
                poseStack.pushPose();

                //poseStack.scale(.1f, .1f, .1f);

                Vector3f pos = cube.pivot();
                //poseStack.translate(pos.x, pos.y, pos.z);

                Vector3f rot = cube.rotation();
                poseStack.rotateAround(new Quaternionf().rotationXYZ(rot.x, rot.y, rot.z), pos.x, pos.y, pos.z);

                Util.render(cube, multiBufferSource.getBuffer(RenderType.CUTOUT), poseStack.last().pose(), packedLight, RGBA.NULL);

                /*
                RenderUtil.drawCube(
                        multiBufferSource.getBuffer(RenderType.SOLID),
                        poseStack.last().pose(),
                        packedLight,
                        RGBA.BLACK,
                        RenderUtil.Cube.cube(),
                        new Vec3(cube.from()),
                        new Vec3(cube.to())
                );

                 */

                poseStack.popPose();
            }
        }
    }
}
