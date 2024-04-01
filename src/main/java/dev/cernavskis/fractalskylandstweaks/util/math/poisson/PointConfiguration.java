// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util.math.poisson;

public class PointConfiguration<T> {
  public final double radius;
  public final double weight;
  public final T data;

  public PointConfiguration(double radius, double weight, T data) {
    this.radius = radius;
    this.data = data;
    this.weight = weight;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(radius) ^ data.hashCode();
  }
}
