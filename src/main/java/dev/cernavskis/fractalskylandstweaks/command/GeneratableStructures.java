// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.cernavskis.fractalskylandstweaks.util.IslandBaseSettings;
import dev.cernavskis.fractalskylandstweaks.util.IslandSurfaceSettings;
import dev.cernavskis.fractalskylandstweaks.util.IslandUtil;
import dev.cernavskis.fractalskylandstweaks.world.gen.SBConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.crash.ReportedException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.registries.ForgeRegistries;

public class GeneratableStructures {
  private static final Logger LOGGER = LogManager.getLogger();
  public static final Map<String, IStructure<?>> STRUCTURES = new ConcurrentHashMap<>();
  private static String[] STRUCTURE_NAMES = new String[0];
  
  static {
    registerStructure("start_island", new INoConfigStructure() {
      @Override
      public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, Object config) {
        return SBConfiguredFeatures.START_ISLAND.place(world, generator, random, pos);
      }
    });

    registerStructure("island", new IStructure<IslandConfig>() {
      @Override
      public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, IslandConfig config) {
        IslandUtil.generateBasicIsland(
          world,
          random,
          pos,
          config.radius,
          new IslandBaseSettings(
            config.baseNoiseScale,
            config.baseDepthMultiplier,
            (totalDepth, radius, relativePosition, seed) -> {
              BlockState[] blocks = config.baseBlocks;
              if (blocks.length == 0) {
                return null;
              }
              if (config.baseBlocksAreRandom) {
                return blocks[random.nextInt(blocks.length)];
              } else {
                return blocks[(int) (Math.abs(relativePosition.getY()) * blocks.length / totalDepth)];
              }
            }
          ),
          new IslandSurfaceSettings(
            config.surfaceNoiseScale,
            config.surfaceDepthMultiplier,
            (totalHeight, radius, relativePosition, seed) -> {
              BlockState[] blocks = config.surfaceBlocks;
              if (blocks.length == 0) {
                return null;
              }
              if (config.surfaceBlocksAreRandom) {
                return blocks[random.nextInt(blocks.length)];
              } else {
                if (totalHeight - relativePosition.getY() < 1) {
                  return blocks[blocks.length - 1];
                }
                return blocks[(int) (Math.abs(relativePosition.getY()) * blocks.length / totalHeight)];
              }
            }
          )
        );
        return true;
      }
      
      @Override
      public CompoundNBT getDefaultConfig() {
        CompoundNBT defaultConfig = new CompoundNBT();
        CompoundNBT base = new CompoundNBT();
        ListNBT baseBlocks = new ListNBT();
        baseBlocks.add(StringNBT.valueOf("minecraft:stone"));
        base.put("blocks", baseBlocks);
        base.putBoolean("blocksAreRandom", false);
        base.putDouble("noiseScale", 0.0625);
        base.putDouble("depthMultiplier", 0.9);
        CompoundNBT surface = new CompoundNBT();
        ListNBT surfaceBlocks = new ListNBT();
        surfaceBlocks.add(StringNBT.valueOf("minecraft:grass_block"));
        surface.put("blocks", surfaceBlocks);
        surface.putBoolean("blocksAreRandom", false);
        surface.putDouble("noiseScale", 1);
        surface.putDouble("depthMultiplier", 0);
        defaultConfig.put("base", base);
        defaultConfig.put("surface", surface);
        defaultConfig.putDouble("radius", 8);
        return defaultConfig;
      }

      @Override
      public boolean isValidConfig(@Nullable CompoundNBT config) {
        if (config == null) {
          return true;
        }

        if (config.contains("base")) {
          if (!config.contains("base", 10)) {
            return false;
          }
          CompoundNBT base = config.getCompound("base");
          if (base.contains("blocks")) {
            if (!base.contains("blocks", 9)) {
              return false;
            }
            try {
              ListNBT blocks = base.getList("blocks", 8);

              for (int i = 0; i < blocks.size(); i++) {
                String block = blocks.getString(i);
                ResourceLocation blockId = ResourceLocation.tryParse(block);
                if (blockId == null || ForgeRegistries.BLOCKS.getValue(blockId) == null) {
                  return false;
                }
              }
            } catch (ReportedException e) {
              return false;
            }
          }
          if (base.contains("noiseScale") && !base.contains("noiseScale", 6)) {
            return false;
          }
          if (base.contains("depthMultiplier") && !base.contains("depthMultiplier", 6)) {
            return false;
          }
          if (base.contains("blocksAreRandom") && !base.contains("blocksAreRandom", 1)) {
            return false;
          }
        }

        if (config.contains("surface")) {
          if (!config.contains("surface", 10)) {
            return false;
          }
          CompoundNBT surface = config.getCompound("surface");
          if (surface.contains("blocks")) {
            if (!surface.contains("blocks", 9)) {
              return false;
            }
            try {
              ListNBT blocks = surface.getList("blocks", 8);

              for (int i = 0; i < blocks.size(); i++) {
                String block = blocks.getString(i);
                ResourceLocation blockId = ResourceLocation.tryParse(block);
                if (blockId == null || ForgeRegistries.BLOCKS.getValue(blockId) == null) {
                  return false;
                }
              }
            } catch (ReportedException e) {
              return false;
            }
          }
          if (surface.contains("noiseScale") && !surface.contains("noiseScale", 6)) {
            return false;
          }
          if (surface.contains("depthMultiplier") && !surface.contains("depthMultiplier", 6)) {
            return false;
          }
          if (surface.contains("blocksAreRandom") && !surface.contains("blocksAreRandom", 1)) {
            return false;
          }
        }

        if (config.contains("radius") && !config.contains("radius", 6)) {
          return false;
        }

        return true;
      }

      @Override
      public IslandConfig parseConfig(@Nullable CompoundNBT config) {
        IslandConfig islandConfig = new IslandConfig();
        CompoundNBT defaultConfig = getDefaultConfig();
        if (config == null) {
          config = defaultConfig;
        }
        CompoundNBT base;
        if (config.contains("base")) {
          base = config.getCompound("base");
        } else {
          base = defaultConfig.getCompound("base");
        }

        ListNBT baseBlocks;
        if (base.contains("blocks")) {
          baseBlocks = base.getList("blocks", 8);
        } else {
          baseBlocks = defaultConfig.getCompound("base").getList("blocks", 8);
        }
        List<BlockState> baseBlockList = new ArrayList<>(baseBlocks.size());
        for (int i = 0; i < baseBlocks.size(); i++) {
          Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(baseBlocks.getString(i)));
          baseBlockList.add(block.defaultBlockState());
        }
        islandConfig.baseBlocks = baseBlockList.toArray(new BlockState[0]);

        if (base.contains("noiseScale")) {
          islandConfig.baseNoiseScale = base.getDouble("noiseScale");
        } else {
          islandConfig.baseNoiseScale = defaultConfig.getCompound("base").getDouble("noiseScale");
        }

        if (base.contains("depthMultiplier")) {
          islandConfig.baseDepthMultiplier = base.getDouble("depthMultiplier");
        } else {
          islandConfig.baseDepthMultiplier = defaultConfig.getCompound("base").getDouble("depthMultiplier");
        }

        if (base.contains("blocksAreRandom")) {
          islandConfig.baseBlocksAreRandom = base.getBoolean("blocksAreRandom");
        } else {
          islandConfig.baseBlocksAreRandom = defaultConfig.getCompound("base").getBoolean("blocksAreRandom");
        }

        CompoundNBT surface;
        if (config.contains("surface")) {
          surface = config.getCompound("surface");
        } else {
          surface = defaultConfig.getCompound("surface");
        }

        ListNBT surfaceBlocks;
        if (surface.contains("blocks")) {
          surfaceBlocks = surface.getList("blocks", 8);
        } else {
          surfaceBlocks = defaultConfig.getCompound("surface").getList("blocks", 8);
        }
        List<BlockState> surfaceBlockList = new ArrayList<>(surfaceBlocks.size());
        for (int i = 0; i < surfaceBlocks.size(); i++) {
          Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(surfaceBlocks.getString(i)));
          surfaceBlockList.add(block.defaultBlockState());
        }
        islandConfig.surfaceBlocks = surfaceBlockList.toArray(new BlockState[0]);

        if (surface.contains("noiseScale")) {
          islandConfig.surfaceNoiseScale = surface.getDouble("noiseScale");
        } else {
          islandConfig.surfaceNoiseScale = defaultConfig.getCompound("surface").getDouble("noiseScale");
        }

        if (surface.contains("depthMultiplier")) {
          islandConfig.surfaceDepthMultiplier = surface.getDouble("depthMultiplier");
        } else {
          islandConfig.surfaceDepthMultiplier = defaultConfig.getCompound("surface").getDouble("depthMultiplier");
        }

        if (surface.contains("blocksAreRandom")) {
          islandConfig.surfaceBlocksAreRandom = surface.getBoolean("blocksAreRandom");
        } else {
          islandConfig.surfaceBlocksAreRandom = defaultConfig.getCompound("surface").getBoolean("blocksAreRandom");
        }


        if (config.contains("radius")) {
          islandConfig.radius = config.getDouble("radius");
        } else {
          islandConfig.radius = defaultConfig.getDouble("radius");
        }
        
        return islandConfig;
      }
    });

    // registerStructure("test_feature", new INoConfigStructure() {
    //   @Override
    //   public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, Object config) {
    //     return SBConfiguredFeatures.TEST_FEATURE.place(world, generator, random, pos);
    //   }
    // });
  }

  public static void registerStructure(String name, IStructure<?> structure) {
    name = name.toLowerCase(Locale.ROOT);
    if (STRUCTURES.containsKey(name)) {
      LOGGER.error("Structure {} already registered, skipping...", name);
      return;
    }
    STRUCTURES.put(name, structure);
    STRUCTURE_NAMES = STRUCTURES.keySet().toArray(new String[0]);
  }

  protected static CompletableFuture<Suggestions> suggestStructure(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
    return ISuggestionProvider.suggest(STRUCTURE_NAMES, builder);
  }

  public static interface IStructure<T> {
    boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, @Nullable T config);

    boolean isValidConfig(@Nullable CompoundNBT config);

    @Nullable
    CompoundNBT getDefaultConfig();

    @Nullable
    T parseConfig(CompoundNBT config);
  }

  public static interface INoConfigStructure extends IStructure<Object> {
    boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, @Nullable Object config);

    default boolean isValidConfig(@Nullable CompoundNBT config) {
      return true;
    }

    @Nullable
    default CompoundNBT getDefaultConfig() {
      return null;
    }

    @Nullable
    default Object parseConfig(CompoundNBT config) {
      return null;
    }
  }

  private static class IslandConfig {
    public double baseNoiseScale;
    public double baseDepthMultiplier;
    public BlockState[] baseBlocks;
    public boolean baseBlocksAreRandom;
    public double surfaceNoiseScale;
    public double surfaceDepthMultiplier;
    public BlockState[] surfaceBlocks;
    public boolean surfaceBlocksAreRandom;
    public double radius;
  }

  public static IStructure<?> getStructure(String structureName) {
    return STRUCTURES.get(structureName.toLowerCase(Locale.ROOT));
  }
}
