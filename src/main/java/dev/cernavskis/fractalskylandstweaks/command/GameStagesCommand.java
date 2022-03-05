// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.command;

import java.util.UUID;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import dev.cernavskis.fractalskylandstweaks.FractalSkylandsTweaks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class GameStagesCommand implements ICommand {
  @Override
  public LiteralArgumentBuilder<CommandSource> getCommand() {
    return Commands.literal("game_stages")
      .then(
        Commands.argument("player", EntityArgument.player())
          .then(
            Commands.literal("get")
              .executes(context ->
                executeGet(
                  context,
                  EntityArgument.getPlayer(context, "player")
                )
              )
          )
          .then(
            Commands.literal("add")
              .then(
                Commands.argument("stage", StringArgumentType.word())
                  .executes(context ->
                    executeAdd(
                      context,
                      EntityArgument.getPlayer(context, "player"),
                      StringArgumentType.getString(context, "stage")
                    )
                  )
              )
          )
          .then(
            Commands.literal("remove")
              .then(
                Commands.argument("stage", StringArgumentType.word())
                  .executes(context ->
                    executeRemove(
                      context,
                      EntityArgument.getPlayer(context, "player"),
                      StringArgumentType.getString(context, "stage")
                    )
                  )
              )
          )
      );
  }

  private int executeGet(CommandContext<CommandSource> context, ServerPlayerEntity player) {
    UUID uuid = player.getUUID();
    String[] stages = FractalSkylandsTweaks.PLAYER_STAGES.getStages(uuid);
    CommandSource source = context.getSource();

    if (stages.length == 0) {
      source.sendSuccess(new StringTextComponent("Player currently doesn't have any stages."), false);
    } else {
      source.sendSuccess(new StringTextComponent("Player currently has the following stages:"), false);
      for (String stage : stages) {
        source.sendSuccess(new StringTextComponent(" - " + stage), false);
      }
    }

    return 1;
  }

  private int executeAdd(CommandContext<CommandSource> context, ServerPlayerEntity player, String string) {
    UUID uuid = player.getUUID();
    CommandSource source = context.getSource();

    if (FractalSkylandsTweaks.PLAYER_STAGES.hasStage(uuid, string)) {
      source.sendFailure(new StringTextComponent("Player already has this stage."));
      return 0;
    }

    FractalSkylandsTweaks.PLAYER_STAGES.addStage(uuid, string);
    source.sendSuccess(new StringTextComponent("Player now has the stage " + string + "."), false);
    return 1;
  }

  private int executeRemove(CommandContext<CommandSource> context, ServerPlayerEntity player, String string) {
    UUID uuid = player.getUUID();
    CommandSource source = context.getSource();

    if (!FractalSkylandsTweaks.PLAYER_STAGES.hasStage(uuid, string)) {
      source.sendFailure(new StringTextComponent("Player doesn't have this stage."));
      return 0;
    }

    FractalSkylandsTweaks.PLAYER_STAGES.removeStage(uuid, string);
    source.sendSuccess(new StringTextComponent("Player no longer has the stage " + string + "."), false);
    return 1;
  }

}
