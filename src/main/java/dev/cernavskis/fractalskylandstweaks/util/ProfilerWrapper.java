// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.profiler.IProfiler;

public class ProfilerWrapper implements IProfiler {
  private IProfiler profiler;
  
  public ProfilerWrapper(@Nullable IProfiler profiler) {
    this.profiler = profiler;
  }

  public boolean hasProfiler() {
    return this.profiler != null;
  }

  public void setProfiler(@Nullable IProfiler profiler) {
    this.profiler = profiler;
  }
  
  @Override
  public void startTick() {
    if (this.hasProfiler()) {
      this.profiler.startTick();
    }
  }

  @Override
  public void endTick() {
    if (this.hasProfiler()) {
      this.profiler.endTick();
    }
  }

  @Override
  public void push(String p_76320_1_) {
    if (this.hasProfiler()) {
      this.profiler.push(p_76320_1_);
    }
  }

  @Override
  public void push(Supplier<String> p_194340_1_) {
    if (this.hasProfiler()) {
      this.profiler.push(p_194340_1_);
    }
  }

  @Override
  public void pop() {
    if (this.hasProfiler()) {
      this.profiler.pop();
    }
  }

  @Override
  public void popPush(String p_219895_1_) {
    if (this.hasProfiler()) {
      this.profiler.popPush(p_219895_1_);
    }
  }

  @Override
  public void popPush(Supplier<String> p_194339_1_) {
    if (this.hasProfiler()) {
      this.profiler.popPush(p_194339_1_);
    }
  }

  @Override
  public void incrementCounter(String p_230035_1_) {
    if (this.hasProfiler()) {
      this.profiler.incrementCounter(p_230035_1_);
    }
  }

  @Override
  public void incrementCounter(Supplier<String> p_230036_1_) {
    if (this.hasProfiler()) {
      this.profiler.incrementCounter(p_230036_1_);
    }
  }
}
