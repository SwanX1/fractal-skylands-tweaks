// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util.math.poisson;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import dev.cernavskis.fractalskylandstweaks.util.math.Vector2d;
import net.minecraft.util.RandomSource;

public class PoissonUtil {
  /**
   * A function that generates a list of points distributed using poisson disc
   * sampling, given a region size
   *
   * @param width               The width of the region
   * @param height              The height of the region
   * @param maxRetries          The maximum number of times to try to find a point
   *                            before giving up
   * @param random              The random number generator to use
   * @param pointConfigurations The point configurations to use
   */
  public static <T> List<Point<T>> generatePoints(int width, int height, int maxRetries, RandomSource random, @Nonnull List<PointConfiguration<T>> pointConfigurations) {
    return generatePoints(width, height, maxRetries, random, pointConfigurations, new ArrayList<>());
  }

  /**
   * A function that generates a list of points distributed using poisson disc
   * sampling, given a region size
   *
   * @param width               The width of the region
   * @param height              The height of the region
   * @param maxRetries          The maximum number of times to try to find a point
   *                            before giving up
   * @param random              The random number generator to use
   * @param initialPoints       The initial points to use
   *                            (note: initial points are not included in the
   *                            returned list)
   * @param pointConfigurations The point configurations to use
   */
  public static <T> List<Point<T>> generatePoints(int width, int height, int maxRetries, RandomSource random, @Nonnull List<PointConfiguration<T>> pointConfigurations, @Nonnull List<Point<T>> initialPoints) {
    int widthBoundary = width / 2;
    int heightBoundary = height / 2;

    double largestRadius = Double.MIN_VALUE;
    for (PointConfiguration<T> config : pointConfigurations) {
      if (config.radius > largestRadius) {
        largestRadius = config.radius;
      }
    }

    double totalWeight = 0.0;
    for (PointConfiguration<T> pointConfig : pointConfigurations) {
      totalWeight += pointConfig.weight;
    }

    // // Create a grid to partition the space (allows to check nearby points efficiently, without having to check all points in the list)
    Grid<T> grid = new Grid<>(width, height, (int) largestRadius * 2, -widthBoundary, -heightBoundary);

    // Add the initial points to the grid
    for (Point<T> point : initialPoints) {
      grid.add(point);
    }

    // Create a list of active points to keep track of the current point generation process
    List<Point<T>> points = new ArrayList<>();
    List<Point<?>> activePoints = new ArrayList<>(initialPoints);

    if (activePoints.size() == 0) {
      // Choose a random starting point
      Vector2d startingPoint = new Vector2d(0, 0);
      PointConfiguration<T> startingConfig = pointConfigurations.get(random.nextInt(pointConfigurations.size()));
      Point<T> startingPointObject = new Point<>(startingPoint, startingConfig);
      points.add(startingPointObject);
      activePoints.add(startingPointObject);
    }

    // While there are still active points
    while (!activePoints.isEmpty()) {
      // Choose a random active point
      Point<?> activePoint = activePoints.get(random.nextInt(activePoints.size()));

      // Generate a new point around the active point
      boolean pointGenerated = false;
      retryLoop: for (int i = 0; i < maxRetries; i++) {
        // Generate random point around active point
        Point<T> newPoint = new Point<>(
          getRandomPositionAroundPoint(activePoint.position, activePoint.config.radius, activePoint.config.radius + largestRadius, random),
          getRandomPointConfiguration(random, pointConfigurations, totalWeight)
        );

        // Check if the new point is within the region
        if (
          newPoint.position.x < -widthBoundary || newPoint.position.x >= widthBoundary ||
          newPoint.position.y < -heightBoundary || newPoint.position.y >= heightBoundary
        ) {
          continue;
        }

        // Check if the new point is too close to any other points
        for (Point<T> point : grid.getNearby(newPoint)) {
          if (newPoint.position.isWithinDistance(point.position, newPoint.config.radius + point.config.radius)) {
            continue retryLoop;
          }
        }

        // Add the new point to the list of points
        points.add(newPoint);
        activePoints.add(newPoint);

        grid.add(newPoint);

        // Set the point generated flag to true
        pointGenerated = true;
        break;
      }

      // If no point was generated, remove the active point
      if (!pointGenerated) {
        activePoints.remove(activePoint);
      }
    }

    return points;
  }

  private static <T> PointConfiguration<T> getRandomPointConfiguration(RandomSource random, List<PointConfiguration<T>> items, double totalWeight) {
    double randomWeight = random.nextDouble() * totalWeight;
    double currentWeight = 0;
    for (PointConfiguration<T> item : items) {
      currentWeight += item.weight;
      if (randomWeight <= currentWeight) {
        return item;
      }
    }
  
    return items.get(random.nextInt(items.size()));
  }

  private static Vector2d getRandomPositionAroundPoint(Vector2d position, double minRadius, double maxRadius, RandomSource random) {
    double angle = random.nextDouble() * 2 * Math.PI;
    double distance = minRadius + random.nextDouble() * maxRadius;

    return new Vector2d(position.x + distance * Math.cos(angle), position.y + distance * Math.sin(angle));
  }
}
