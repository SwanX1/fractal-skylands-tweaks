// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.util;

public class IslandShapeSettings {
  public double radius;
  public double noiseScale;
  public double noiseMultiplier;

  public IslandShapeSettings(double radius, double noiseScale, double noiseMultiplier) {
    this.radius = radius;
    this.noiseScale = noiseScale;
    this.noiseMultiplier = noiseMultiplier;
  }
}
