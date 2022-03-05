// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.command;

import java.util.Locale;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import dev.cernavskis.fractalskylandstweaks.command.GeneratableStructures.IStructure;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;

public class GetStructureConfigCommand implements ICommand {
  @Override
  public LiteralArgumentBuilder<CommandSource> getCommand() {
    return Commands.literal("getstructureconfig")
      .then(
        Commands.argument("structure", StringArgumentType.word())
          .suggests(GeneratableStructures::suggestStructure)
          .executes(GetStructureConfigCommand::execute)
      );
  }

  private static int execute(CommandContext<CommandSource> context) {
    String structureName = StringArgumentType.getString(context, "structure").toLowerCase(Locale.ROOT);
    IStructure<?> structure = GeneratableStructures.getStructure(structureName);
    CommandSource source = context.getSource();

    if (structure == null) {
      source.sendFailure(new StringTextComponent("Unknown structure: " + structure));
      return 0;
    }

    CompoundNBT config = structure.getDefaultConfig();
    if (config != null) {
      source.sendSuccess(new StringTextComponent("Default config for \u00a7a\"" + structureName + "\"\u00a7r:"), false);
      source.sendSuccess(config.getPrettyDisplay(" ", 1), false);
    } else {
      source.sendSuccess(new StringTextComponent("\u00a7a\"" + structureName + "\"\u00a7r doesn't have a config."), false);
    }

    return 1;
  }
}
