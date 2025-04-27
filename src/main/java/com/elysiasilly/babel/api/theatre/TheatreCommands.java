package com.elysiasilly.babel.api.theatre;

import com.elysiasilly.babel.Babel;
import com.elysiasilly.babel.api.theatre.actor.Actor;
import com.elysiasilly.babel.api.theatre.actor.ActorRemovalReason;
import com.elysiasilly.babel.api.theatre.actor.command.ActorArgument;
import com.elysiasilly.babel.api.theatre.actor.registry.ActorType;
import com.elysiasilly.babel.api.theatre.scene.Scene;
import com.elysiasilly.babel.api.theatre.scene.registry.SceneType;
import com.elysiasilly.babel.core.BBRegistries;
import com.elysiasilly.babel.util.conversions.ConversionsVector;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.util.Collection;
import java.util.function.Function;

public class TheatreCommands {

    public static LiteralArgumentBuilder<CommandSourceStack> create(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection, LiteralArgumentBuilder<CommandSourceStack> argumentBuilder) {

        return argumentBuilder.then(Commands.literal("theatre").requires(permission -> permission.hasPermission(2))

                .then(Commands.literal("actor")

                        .then(Commands.literal("target")

                                .then(Commands.argument("targets", ActorArgument.actors())
                                        .then(Commands.literal("move")
                                                .executes(css -> {
                                                    Collection<Actor> actors = ActorArgument.getOptionalActors(css, "targets");
                                                    move(actors, vec(css.getSource().getPosition()), css.getSource());
                                                    return 2;
                                                })
                                        )


                                        .then(Commands.literal("destroy"))
                                        .then(Commands.literal("uuid"))
                                        .then(Commands.literal("modify"))
                                )
                        )

                        .then(Commands.literal("create")
                                .then(Commands.argument("type", ResourceArgument.resource(context, BBRegistries.ACTOR_TYPE.key()))
                                        .executes(css -> {
                                            create(css.getSource().getLevel(), actorType(css, "type").value(), vec(css.getSource().getPosition()), null, css.getSource());
                                            return 2;
                                        })
                                        .then(Commands.argument("pos", Vec3Argument.vec3())
                                                .executes(css -> {
                                                    create(css.getSource().getLevel(), actorType(css, "type").value(), vec(Vec3Argument.getVec3(css, "pos")), null, css.getSource());
                                                    return 2;
                                                })
                                                .then(Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                                        .executes(css -> {
                                                            create(css.getSource().getLevel(), actorType(css, "type").value(), vec(Vec3Argument.getVec3(css, "pos")), CompoundTagArgument.getCompoundTag(css, "nbt"), css.getSource());
                                                            return 2;
                                                        })
                                                )
                                        )
                                )

                        )

                        .then(Commands.literal("list")
                        )
                )
                .then(Commands.literal("scene")
                                .then(Commands.literal("list"))
                )
        );
    }

    private static Vector3d vec(Vec3 vec) {
        return ConversionsVector.toJOML(vec);
    }

    private static Holder.Reference<SceneType<?, ?>> sceneType(CommandContext<CommandSourceStack> context, String argument) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, argument, BBRegistries.SCENE_TYPE.key());
    }

    private static Holder.Reference<ActorType<?>> actorType(CommandContext<CommandSourceStack> context, String argument) throws CommandSyntaxException {
        return ResourceArgument.getResource(context, argument, BBRegistries.ACTOR_TYPE.key());
    }

    private static int destroy(ServerLevel server, SceneType<?, ?> sceneType) {
        Scene<?> scene = Theatre.get(server, sceneType);

        Babel.LOGGER.info(scene);

        int amount = scene.actors().size();

        for(Actor actor : scene.actors()) {
            scene.removeActor(actor, ActorRemovalReason.COMMAND);
        }

        return amount;
    }

    private static final Function<Actor, SimpleCommandExceptionType> ERROR_MODIFY_POSITION = actor -> new SimpleCommandExceptionType(Component.translatable("command.babel.actor.modify.failed.position", actor));

    private static void move(Collection<Actor> actors, Vector3d pos, CommandSourceStack css) throws CommandSyntaxException {

        for(Actor actor : actors) {
            if(actor.posInvalid(pos))
                throw ERROR_MODIFY_POSITION.apply(actor).create();
            else
                actor.pos(pos);
        }

        css.sendSuccess(() -> Component.translatable("command.babel.actor.modify.success.position"), true);
    }

    private static final SimpleCommandExceptionType ERROR_CREATE = new SimpleCommandExceptionType(Component.translatable("command.babel.actor.created.failed"));
    private static final SimpleCommandExceptionType ERROR_CREATE_POSITION = new SimpleCommandExceptionType(Component.translatable("command.babel.actor.created.failed.position"));
    private static final SimpleCommandExceptionType ERROR_CREATE_NBT = new SimpleCommandExceptionType(Component.translatable("command.babel.actor.created.failed.nbt"));
    private static final SimpleCommandExceptionType ERROR_CREATE_SCENE = new SimpleCommandExceptionType(Component.translatable("command.babel.actor.created.failed.scene"));

    private static void create(ServerLevel server, ActorType<?> type, Vector3d pos, CompoundTag tag, CommandSourceStack css) throws CommandSyntaxException {

        Actor actor = type.create(new Vector3d());

        if(actor.posInvalid(pos))
            throw ERROR_CREATE_POSITION.create();
        else
            actor.pos(pos);

        if(tag != null) {
            try {
                actor.deserializeFromCommand(tag, server.registryAccess());
            } catch (Exception e) {
                throw ERROR_CREATE_NBT.create();
            }
        }

        try {
            Theatre.add(server, actor);
        } catch (Exception e) {
            throw ERROR_CREATE_SCENE.create();
        }

        css.sendSuccess(() -> Component.translatable("command.babel.actor.created.success", actor.translationKey()), true);
    }
}
