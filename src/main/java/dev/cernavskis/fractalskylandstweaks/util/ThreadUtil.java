// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ThreadUtil {
  public static <R> void runAsync(Supplier<R> function, Consumer<R> callback) {
    new Thread(() -> {
      R result = function.get();
      callback.accept(result);
    }).start();
  }

  public static void runAsync(Runnable function, Runnable callback) {
    new Thread(() -> {
      function.run();
      callback.run();
    }).start();
  }
}
