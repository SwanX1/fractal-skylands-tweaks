// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util.math.poisson;

import dev.cernavskis.fractalskylandstweaks.util.math.Vector2d;

public class Point<T> {

    public Vector2d position;
    public PointConfiguration<T> config;

    public Point(Vector2d position, PointConfiguration<T> config) {
        this.position = position;
        this.config = config;
    }
}
