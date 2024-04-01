// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.world.gen.islands;

public class IslandTerrainSettings {
  public final double radius;
  public final double shapeNoiseScale;
  public final double shapeNoiseMultiplier;
  public final double surfaceNoiseScale;
  public final double surfaceNoiseMultiplier;
  public final double groundNoiseScale;
  public final double groundNoiseMultiplier;

  public IslandTerrainSettings(double radius, double shapeNoiseScale, double shapeNoiseMultiplier, double surfaceNoiseScale, double surfaceNoiseMultiplier, double groundNoiseScale, double groundNoiseMultiplier) {
    this.radius = radius;
    this.shapeNoiseScale = shapeNoiseScale;
    this.shapeNoiseMultiplier = shapeNoiseMultiplier;
    this.surfaceNoiseScale = surfaceNoiseScale;
    this.surfaceNoiseMultiplier = surfaceNoiseMultiplier;
    this.groundNoiseScale = groundNoiseScale;
    this.groundNoiseMultiplier = groundNoiseMultiplier;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(radius);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(shapeNoiseScale);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(shapeNoiseMultiplier);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(surfaceNoiseScale);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(surfaceNoiseMultiplier);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(groundNoiseScale);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(groundNoiseMultiplier);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
