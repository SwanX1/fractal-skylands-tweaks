// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.command;

import java.util.Locale;
import java.util.Random;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import dev.cernavskis.fractalskylandstweaks.command.GeneratableStructures.IStructure;
import dev.cernavskis.fractalskylandstweaks.util.ThreadUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class GenerateCommand implements ICommand {
  @Override
  public LiteralArgumentBuilder<CommandSource> getCommand() {
    return Commands.literal("generate")
      .then(
        Commands.argument("structure", StringArgumentType.word())
          .suggests(GeneratableStructures::suggestStructure)
          .then(
            Commands.argument("config", NBTCompoundTagArgument.compoundTag())
              .executes(GenerateCommand::execute)
          ).executes(GenerateCommand::execute)
      );
  }

  private static int execute(CommandContext<CommandSource> context) {
    String structureName = StringArgumentType.getString(context, "structure").toLowerCase(Locale.ROOT);
    @SuppressWarnings("unchecked")
    IStructure<Object> structure = (IStructure<Object>) GeneratableStructures.getStructure(structureName);
    CommandSource source = context.getSource();

    if (structure == null) {
      source.sendFailure(new StringTextComponent("Unknown structure: " + structureName));
      return 0;
    }

    CompoundNBT config;
    try {
      config = NBTCompoundTagArgument.getCompoundTag(context, "config");
    } catch (IllegalArgumentException e) {
      config = null;
    }

    if (!structure.isValidConfig(config)) {
      source.sendFailure(new StringTextComponent("Invalid config:"));
      source.sendFailure(config.getPrettyDisplay(" ", 1));
      return 0;
    }

    Object parsedConfig = structure.parseConfig(config);

    ServerWorld world = source.getLevel();
    long startTime = System.nanoTime();
    ThreadUtil.runAsync(
      () -> {
        source.sendSuccess(new StringTextComponent("Generating \u00a7a\"" + structureName + "\"\u00a7r..."), true);
        return structure.place(world, world.getChunkSource().getGenerator(), new Random(world.getRandom().nextLong()), new BlockPos(source.getPosition()), parsedConfig);
      },
      success -> {
        long ms = (System.nanoTime() - startTime) / 1000000;
        if (success) {
          source.sendSuccess(new StringTextComponent("Generated \u00a7a\"" + structureName + "\"\u00a7r in " + ms + "ms"), true);
        } else {
          source.sendFailure(new StringTextComponent("Failed to generate \u00a7a\"" + structureName + "\"\u00a7r in " + ms + "ms"));
        }
      }
    );
    return 1;
  }
}
