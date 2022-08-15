// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class SkyblockCommand implements ICommand {
  private final ICommand generateCommand = new GenerateCommand();
  private final ICommand getConfigCommand = new GetStructureConfigCommand();
  @Override
  public LiteralArgumentBuilder<CommandSource> getCommand() {
    return Commands.literal("skyblock")
        .requires(source -> source.hasPermission(2))
        .then(generateCommand.getCommand())
        .then(getConfigCommand.getCommand());
  }
}
