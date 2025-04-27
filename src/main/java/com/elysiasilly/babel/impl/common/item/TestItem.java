package com.elysiasilly.babel.impl.common.item;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.impl.common.actor.TestActor;
import com.elysiasilly.babel.util.UtilsMC;
import com.elysiasilly.babel.util.conversions.ConversionsVector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TestItem extends Item {

    public static Vec3 start;
    public static Vec3 end;

    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(!level.isClientSide) {

            if(end == null && start != null) {
                List<Vec3> ray = UtilsMC.Raycast.shittyRayCast(player, 10, UtilsMC.Raycast.GOOD_ENOUGH);
                end = ray.getLast();
            }
            if(start == null) {
                List<Vec3> ray = UtilsMC.Raycast.shittyRayCast(player, 10, UtilsMC.Raycast.GOOD_ENOUGH);
                start = ray.getLast();
            }

            if(start != null && end != null) {
                TestActor actor = BBActors.TEST_ACTOR.get().create(ConversionsVector.toJOML(start));
                //actor.pos(start);
                actor.init(start, end);
                Theatre.add(level, actor);
                start = null;
                end = null;
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
