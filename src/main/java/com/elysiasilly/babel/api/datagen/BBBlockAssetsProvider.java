package com.elysiasilly.babel.api.datagen;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class BBBlockAssetsProvider extends BlockStateProvider {

    public BBBlockAssetsProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    ///

    private String namespace(Block block) {
        return key(block).getNamespace();
    }

    private String id(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private ResourceLocation mcParent(String name) {
        return ResourceLocation.withDefaultNamespace(String.format("%s/%s", ModelProvider.BLOCK_FOLDER, name));
    }

    private ResourceLocation modParent(Block block, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace(block), String.format("%s/%s", ModelProvider.BLOCK_FOLDER, path));
    }

    ///

    /*
    public void simpleWoodSet(SimpleWoodSet.Entries set) {

    }

    public void simpleBlockSet(SimpleBlockSet.Entries set) {


        //String path = set.name()

        set.block().ifPresent(block -> {
            simpleBlock(block.get(), set.name() + "/block", set.name(), "block");
        });

        set.slab().ifPresent(slab -> {
            simpleSlab(slab.get(), set.name() + "/block", set.name());
        });

        set.stairs().ifPresent(stairs -> {
            simpleStairs(stairs.get(), set.name() + "/block", set.name());
        });

        set.wall().ifPresent(wall -> {
            simpleWall(wall.get(), set.name() + "/block", set.name());
        });
    }

     */

    ///

    public void simpleBlock(Block block, String texture, String path, String name) {

        ModelFile model = models().withExistingParent("block/" + path + "/" + name, mcParent("cube_all"))
                .texture("all", modParent(block, texture));

        simpleBlock(block, model);
        item(block, model);
    }

    public void simpleStairs(StairBlock block, String texture, String path) {

        ModelFile normal = models().withExistingParent("block/" + path + "/stairs/normal", mcParent("stairs"))
                .texture("side", modParent(block, texture))
                .texture("bottom", modParent(block, texture))
                .texture("top", modParent(block, texture));

        ModelFile inner = models().withExistingParent("block/" + path + "/stairs/inner", mcParent("inner_stairs"))
                .texture("side", modParent(block, texture))
                .texture("bottom", modParent(block, texture))
                .texture("top", modParent(block, texture));

        ModelFile outer = models().withExistingParent("block/" + path + "/stairs/outer", mcParent("outer_stairs"))
                .texture("side", modParent(block, texture))
                .texture("bottom", modParent(block, texture))
                .texture("top", modParent(block, texture));

        stairsBlock(block, normal, inner, outer);
        item(block, normal);
    }

    public void simpleSlab(SlabBlock block, String texture, String path) {

        ModelFile top = models().withExistingParent("block/" + path + "/slab/top", mcParent("slab_top"))
                .texture("side", modParent(block, texture))
                .texture("bottom", modParent(block, texture))
                .texture("top", modParent(block, texture));

        ModelFile bottom = models().withExistingParent("block/" + path + "/slab/bottom", mcParent("slab"))
                .texture("side", modParent(block, texture))
                .texture("bottom", modParent(block, texture))
                .texture("top", modParent(block, texture));

        ModelFile full = models().withExistingParent("block/" + path + "/slab/double", mcParent("cube_column"))
                .texture("side", modParent(block, texture))
                .texture("end", modParent(block, texture));

        slabBlock(block, bottom, top, full);
        item(block, bottom);
    }

    public void simplePillar(RotatedPillarBlock block, String texture, String path) {
    }

    public void simpleWall(WallBlock block, String texture, String path) {

        ModelFile post = models().withExistingParent("block/" + path + "/wall/post", mcParent("template_wall_post"))
                .texture("wall", modParent(block, texture));

        ModelFile side = models().withExistingParent("block/" + path + "/wall/side", mcParent("template_wall_side"))
                .texture("wall", modParent(block, texture));

        ModelFile sideTall = models().withExistingParent("block/" + path + "/wall/side_tall", mcParent("template_wall_side_tall"))
                .texture("wall", modParent(block, texture));

        wallBlock(block, post, side, sideTall);

        ModelFile inventory = models().withExistingParent("block/" + path + "/wall/inventory", mcParent("wall_inventory"))
                .texture("wall", modParent(block, texture));

        item(block, inventory);
    }

    private void item(Block block, ModelFile parent) {
        itemModels().getBuilder(id(block)).parent(parent);
    }
}
