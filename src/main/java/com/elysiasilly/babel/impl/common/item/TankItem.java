package com.elysiasilly.babel.impl.common.item;

import com.elysiasilly.babel.api.theatre.Theatre;
import com.elysiasilly.babel.impl.common.actor.TankActor;
import com.elysiasilly.babel.impl.registry.BBActors;
import com.elysiasilly.babel.util.MCUtil;
import com.elysiasilly.babel.util.conversions.VectorConversions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TankItem extends Item {

    public TankItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if(!level.isClientSide) {

            TankActor tank = BBActors.TANK.get().create();
            List<Vec3> ray = MCUtil.Raycast.shittyRayCast(player, 10, MCUtil.Raycast.GOOD_ENOUGH);

            tank.setPos(player.isShiftKeyDown() ? ray.getLast() : VectorConversions.toBlockPos(ray.getLast()).getBottomCenter());
            //tank.setRot((int) MathUtil.numbers.castToRange(-1, 1, 0, 360, (float) player.getLookAngle().z));

            Theatre.add(level, tank);

        }

        return InteractionResultHolder.success(stack);
    }
}
