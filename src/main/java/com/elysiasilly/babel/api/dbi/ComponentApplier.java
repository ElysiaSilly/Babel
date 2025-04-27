package com.elysiasilly.babel.api.dbi;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.events.ItemStackEvents;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.core.registry.BBComponents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Babel.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ComponentApplier {

    private static boolean FLAG = true;
    private static final Map<Item, DynamicBlockItem> MAP = new HashMap<>();

    private static final Supplier<DataComponentType<DynamicBlockItemComponent>> COMPONENT = BBComponents.DYNAMIC_BLOCK_ITEM;
    
    @SubscribeEvent
    private static void dynamicBlockItem(ItemStackEvents.Created event) {

        if(FLAG) {
            Optional<Registry<DynamicBlockItem>> registry = event.level().registryAccess().registry(BBRegistries.DYNAMIC_BLOCK_ITEM);
            if(registry.isPresent()) {
                for(DynamicBlockItem dynamicBlockItem : registry.get().stream().toList()) {
                    MAP.put(dynamicBlockItem.item(), dynamicBlockItem);
                }
            }

            FLAG = false;
        }

        ItemStack stack = event.stack();
        if(MAP.containsKey(event.item())) {
            if(!stack.has(COMPONENT)) {
                DynamicBlockItem dynamicBlockItem = MAP.get(event.item());
                stack.set(COMPONENT, new DynamicBlockItemComponent(0, dynamicBlockItem.entries()));
            }
        } else {
            if(stack.has(COMPONENT)) {
                stack.remove(COMPONENT);
            }
        }
    }

    @SubscribeEvent
    private static void stackDynamicItem(ItemStackedOnOtherEvent event) {
        ItemStack stack = event.getStackedOnItem();
        ItemStack carriedStack = event.getCarriedItem();

        if(DynamicBlockItem.mergeItem(carriedStack.getItem())) {
            if(stack.getItem().equals(DynamicBlockItem.itemToMergeInto(carriedStack.getItem()))) {
                stack.grow(carriedStack.getCount());
                carriedStack.copyAndClear();
            }
        }

        if(DynamicBlockItem.mergeItem(stack.getItem())) {
            if(carriedStack.getItem().equals(DynamicBlockItem.itemToMergeInto(stack.getItem()))) {
                carriedStack.grow(stack.getCount());
                stack.copyAndClear();
            }
        }
    }
}
