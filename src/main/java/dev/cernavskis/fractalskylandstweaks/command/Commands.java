// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.command;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class Commands {
  private static final List<ICommand> commands = new ArrayList<>(1);

  public static final ICommand SKYBLOCK = register(new SkyblockCommand());


  private static ICommand register(ICommand command) {
    commands.add(command);
    return command;
  }

  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    for (ICommand command : commands) {
      dispatcher.register(command.getCommand());
    }
  }
}
