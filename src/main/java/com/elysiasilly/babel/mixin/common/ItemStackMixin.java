package com.elysiasilly.babel.mixin.common;

import com.elysiasilly.babel.api.events.ItemStackEvents;
import com.elysiasilly.babel.impl.common.misc.LevelGetter;
import com.elysiasilly.babel.util.UtilsDev;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, IItemStackExtension, MutableDataComponentHolder {

    @Inject(
            method = "<init>*",
            at = @At("TAIL")
    )

    private void babel$init(CallbackInfo cir) {
        LevelGetter.level().ifPresent(level ->
            UtilsDev.postGameEvent(new ItemStackEvents.Created((ItemStack) (Object) this, level))
        );
    }
}
