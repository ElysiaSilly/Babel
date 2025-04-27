package com.elysiasilly.babel.mixin.common;

import com.elysiasilly.babel.api.dbi.DynamicBlockItemComponent;
import com.elysiasilly.babel.core.registry.BBComponents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Shadow private Block block;

    @WrapOperation(
            method = "getPlacementState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;")
    )

    private BlockState babel$getPlacementState(Block instance, BlockPlaceContext context, Operation<BlockState> original) {
        ItemStack stack = context.getItemInHand();

        if(stack.has(BBComponents.DYNAMIC_BLOCK_ITEM)) {
            return stack.get(BBComponents.DYNAMIC_BLOCK_ITEM).state(context);
        } else {
            return original.call(instance, context);
        }
    }

    @Unique
    public void babel$setBlock(DynamicBlockItemComponent component) {
        this.block = component.block();
    }
}
