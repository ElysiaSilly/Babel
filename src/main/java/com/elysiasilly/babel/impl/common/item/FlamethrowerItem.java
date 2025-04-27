package com.elysiasilly.babel.impl.common.item;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.core.registry.BBActors;
import com.elysiasilly.babel.impl.common.actor.FlamethrowerActor;
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

public class FlamethrowerItem extends Item {

    public FlamethrowerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(!level.isClientSide) {

            List<Vec3> ray = UtilsMC.Raycast.shittyRayCast(player, 10, UtilsMC.Raycast.GOOD_ENOUGH);

            FlamethrowerActor tank = BBActors.FLAMETHROWER.get().create(ConversionsVector.toJOML(player.isShiftKeyDown() ? ray.getLast() : ConversionsVector.toBlockPos(ray.getLast()).getBottomCenter()));

            //tank.pos();
            //tank.setRot((int) MathUtil.numbers.castToRange(-1, 1, 0, 360, (float) player.getLookAngle().z));

            Theatre.add(level, tank);

        }

        return InteractionResultHolder.success(stack);
    }
}
