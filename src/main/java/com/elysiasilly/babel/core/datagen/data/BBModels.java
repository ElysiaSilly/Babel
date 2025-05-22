package com.elysiasilly.babel.core.datagen.data;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.datagen.BBBlockAssetsProvider;
import com.elysiasilly.babel.api.registry.entry.block.BlockAssetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;

public class BBModels extends BBBlockAssetsProvider {

    public static final List<BlockAssetBuilder<?>> assets = new ArrayList<>();

    public BBModels(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Babel.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for(BlockAssetBuilder<?> entry : assets) {
            entry.build(this);
        }
    }
}
