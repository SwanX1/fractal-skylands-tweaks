// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.metrics.MetricCategory;

public class ProfilerWrapper implements ProfilerFiller {
  private ProfilerFiller profiler;

  public ProfilerWrapper(@Nullable ProfilerFiller profiler) {
    this.profiler = profiler;
  }

  public boolean hasProfiler() {
    return this.profiler != null;
  }

  public void setProfiler(@Nullable ProfilerFiller profiler) {
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
  public void markForCharting(MetricCategory p_145959_) {
    if (this.hasProfiler()) {
      this.profiler.markForCharting(p_145959_);
    }
  }

  @Override
  public void incrementCounter(String p_230035_1_) {
    if (this.hasProfiler()) {
      this.profiler.incrementCounter(p_230035_1_);
    }
  }

  @Override
  public void incrementCounter(String p_185258_, int p_185259_) {
    if (this.hasProfiler()) {
      this.profiler.incrementCounter(p_185258_, p_185259_);
    }
  }

  @Override
  public void incrementCounter(Supplier<String> p_230036_1_) {
    if (this.hasProfiler()) {
      this.profiler.incrementCounter(p_230036_1_);
    }
  }

  @Override
  public void incrementCounter(Supplier<String> p_185260_, int p_185261_) {
    if (this.hasProfiler()) {
      this.profiler.incrementCounter(p_185260_, p_185261_);
    }
  }
}
