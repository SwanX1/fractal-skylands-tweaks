// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util.math.poisson;

import java.util.ArrayList;
import java.util.List;

public class Grid<T> {

    private final List<Point<T>>[][] partitions;
    private final int partitionSize;
    private final int offsetX;
    private final int offsetY;

    @SuppressWarnings("unchecked")
    public Grid(int width, int height, int partitionSize, int minX, int minY) {
        this.partitionSize = partitionSize;
        this.offsetX = -minX;
        this.offsetY = -minY;
        this.partitions = new List[(width + offsetX) / partitionSize + 1][(height + offsetY) / partitionSize + 1];
        for (int i = 0; i < partitions.length; i++) {
            for (int j = 0; j < partitions[i].length; j++) {
                partitions[i][j] = new ArrayList<>();
            }
        }
    }

    public void add(Point<T> point) {
        getPartition(point).add(point);
    }

    public List<Point<T>> getNearby(Point<T> point) {
        int x = getIndex(point.position.x, offsetX);
        int y = getIndex(point.position.y, offsetY);

        int startX = Math.max(0, x - 1);
        int endX = Math.min(partitions.length - 1, x + 1);
        int startY, endY;

        int nearbySize = 0;
        for (int i = startX; i <= endX; i++) {
            startY = Math.max(0, y - 1);
            endY = Math.min(partitions[i].length - 1, y + 1);
            for (int j = startY; j <= endY; j++) {
                nearbySize += partitions[i][j].size();
            }
        }

        List<Point<T>> nearby = new ArrayList<>(nearbySize);
        for (int i = startX; i <= endX; i++) {
            startY = Math.max(0, y - 1);
            endY = Math.min(partitions[i].length - 1, y + 1);
            for (int j = startY; j <= endY; j++) {
                nearby.addAll(partitions[i][j]);
            }
        }

        return nearby;
    }

    private List<Point<T>> getPartition(Point<T> point) {
        int x = getIndex(point.position.x, offsetX);
        int y = getIndex(point.position.y, offsetY);
        return partitions[x][y];
    }

    private int getIndex(double position, int offset) {
        return (int) (position + offset) / partitionSize;
    }
}
