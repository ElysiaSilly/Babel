package com.elysiasilly.babel.api.client.model.resources.model;

import com.elysiasilly.babel.api.client.model.resources.CubeModelElement;
import com.elysiasilly.babel.api.client.model.resources.ModelElement;
import com.elysiasilly.babel.util.UtilsRender;
import com.elysiasilly.babel.util.type.RGBA;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelRenderer {

    private final Model model;

    public ModelRenderer(Model model) {
        this.model = model;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int packedLight) {

        poseStack.scale(.1f, .1f, .1f);

        for(ModelElement object : model.getElements()) {
            if(object instanceof CubeModelElement cube) {
                poseStack.pushPose();

                Vector3f pos = cube.pivot();
                poseStack.translate(pos.x, pos.y, pos.z);

                Vector3f rot = cube.rotation();
                poseStack.rotateAround(new Quaternionf().rotationXYZ(rot.x, rot.y, rot.z), pos.x, pos.y, pos.z);


                UtilsRender.drawCube(
                        consumer,
                        poseStack.last().pose(),
                        packedLight,
                        RGBA.BLACK,
                        UtilsRender.Cube.cube(),
                        new Vec3(cube.from()),
                        new Vec3(cube.to())
                );

                poseStack.popPose();
            }
        }
    }
}
