package com.elysiasilly.babel.api.client.model;

import com.elysiasilly.babel.api.client.model.resources.CubeModelElement;
import com.elysiasilly.babel.api.client.model.resources.Face;
import com.elysiasilly.babel.util.type.RGBA;
import com.elysiasilly.babel.util.type.UV;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.UUID;

import static com.elysiasilly.babel.util.UtilsRender.drawPlane;

public class Util {

    public static Vector3f vector3f(JsonObject element, String id) {
        JsonArray array = element.getAsJsonArray(id);
        return array == null ? new Vector3f() : new Vector3f(array.get(0).getAsFloat(), array.get(1).getAsFloat(),array.get(2).getAsFloat());
    }

    public static UUID uuid(JsonObject element, String id) {
        return uuid(element.get(id).getAsString());
    }

    public static UUID uuid(String string) {
        return UUID.nameUUIDFromBytes(string.getBytes());
    }

    private static UV uv(Face face, TextureAtlasSprite sprite) {
        UV uv = face.uv();

        float u = sprite.getU(uv.startU());
        float v = sprite.getV(uv.startV());
        float uu = sprite.getU(uv.endU());
        float vv = sprite.getV(uv.endV());

        return new UV(u, v, uu, vv);
    }

    public static void render(CubeModelElement cube, VertexConsumer consumer, Matrix4f matrix4f, int packedLight, RGBA rgba) {

        Vec3 start = new Vec3(cube.from());
        Vec3 end = new Vec3(cube.to());

        if(cube.up() != null) {

            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, end.y, start.z),
                    new Vec3(end.x, end.y, end.z),
                    new Vec3(end.x, end.y, start.z),
                    new Vec3(start.x, end.y, end.z),
                    uv(cube.up(), Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(cube.up().texture().location())).flip()
            );
        }

        if(cube.down() != null) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, start.y, end.z),
                    new Vec3(end.x, start.y, start.z),
                    new Vec3(end.x, start.y, end.z),
                    new Vec3(start.x, start.y, start.z),
                    uv(cube.down(), Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(cube.down().texture().location())).flip()
            );
        }

        if(cube.north() != null) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, start.y, start.z),
                    new Vec3(end.x, end.y, start.z),
                    new Vec3(end.x, start.y, start.z),
                    new Vec3(start.x, end.y, start.z),
                    uv(cube.north(), Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(cube.north().texture().location()))
            );
        }

        if(cube.south() != null) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, end.y, end.z),
                    new Vec3(end.x, start.y, end.z),
                    new Vec3(end.x, end.y, end.z),
                    new Vec3(start.x, start.y, end.z),
                    uv(cube.south(), Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(cube.south().texture().location())).flip()
            );
        }

        if(cube.east() != null) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(end.x, start.y, start.z),
                    new Vec3(end.x, end.y, end.z),
                    new Vec3(end.x, start.y, end.z),
                    new Vec3(end.x, end.y, start.z),
                    uv(cube.east(), Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(cube.east().texture().location()))

            );
        }

        if(cube.west() != null) {
            drawPlane(consumer, matrix4f, packedLight, rgba,
                    new Vec3(start.x, end.y, start.z),
                    new Vec3(start.x, start.y, end.z),
                    new Vec3(start.x, end.y, end.z),
                    new Vec3(start.x, start.y, start.z),
                    uv(cube.west(), Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(cube.west().texture().location())).flip()
            );
        }
    }
}
