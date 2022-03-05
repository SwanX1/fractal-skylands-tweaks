// Copyright (c) 2022 Kārlis Čerņavskis, All Rights Reserved.
package dev.cernavskis.fractalskylandstweaks.data;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;

public class GameStages {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final Gson GSON = new GsonBuilder().setLenient().serializeNulls().disableHtmlEscaping().create();

  private MinecraftServer server;
  private Map<UUID, List<String>> playerStages;
  private Path dataDirectory;
  private boolean shouldSave;

  public void setServer(MinecraftServer server) {
    this.server = server;
  }

  public boolean isLoaded() {
    return server != null && playerStages != null;
  }

  public void unload() {
    server = null;
    playerStages = null;
    dataDirectory = null;
    shouldSave = false;
  }

  public void load() {
    if (isLoaded()) {
      return;
    }
    if (server == null) {
      throw new IllegalStateException("Server is not set");
    }
    playerStages = new ConcurrentHashMap<>();

    dataDirectory = server.getWorldPath(new FolderName("data"));

    try {
      Files.createDirectories(dataDirectory);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    Path dataFile = dataDirectory.resolve("gamestages.json");

    try {
      if (Files.notExists(dataFile)) {
        System.out.println("Skyblock game stage data file not found, creating one.");
        Files.createFile(dataFile);
        Files.write(dataFile, "{}".getBytes());
      }

      JsonObject jsondata;

      try (Reader reader = Files.newBufferedReader(dataFile)) {
        jsondata = GSON.fromJson(reader, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : jsondata.entrySet()) {
          String jsonUUID = entry.getKey();
          JsonArray jsonStages = entry.getValue().getAsJsonArray();

          UUID player = UUID.fromString(jsonUUID);
          List<String> stages = new LinkedList<>();
          for (JsonElement stage : jsonStages) {
            stages.add(stage.getAsString());
          }
          stages = new CopyOnWriteArrayList<>(stages);
          playerStages.put(player, stages);
        }
      } catch (JsonIOException | IOException ex) {
        ex.printStackTrace();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void save() {
    if (!assertIsLoaded()) {
      return;
    }
    if (!shouldSave) {
      return;
    }
    shouldSave = false;

    LOGGER.info("Saving game stage data...");
    JsonObject jsondata = new JsonObject();

    for (Map.Entry<UUID, List<String>> entry : playerStages.entrySet()) {
      UUID player = entry.getKey();
      List<String> stages = entry.getValue();

      JsonArray jsonStages = new JsonArray();
      for (String stage : stages) {
        jsonStages.add(stage);
      }

      jsondata.add(player.toString(), jsonStages);
    }

    Path dataFile = dataDirectory.resolve("gamestages.json");

    try (Writer writer = Files.newBufferedWriter(dataFile)) {
      GSON.toJson(jsondata, writer);
      LOGGER.info("Saved game stage data!");
    } catch (Exception ex) {
      LOGGER.error("Failed to save gamestages.json!");
      ex.printStackTrace();
    }
  }

  public boolean assertIsLoaded() {
    if (!isLoaded()) {
      LOGGER.error("Cannot access game stage data because it has not been initialized yet!");
      new IllegalStateException().printStackTrace();
      return false;
    }
    return true;
  }

  public String[] getStages(UUID player) {
    if (!assertIsLoaded()) {
      return null;
    }

    return getRawStages(player).toArray(new String[0]);
  }

  public List<String> getStageList(UUID player) {
    if (!assertIsLoaded()) {
      return null;
    }

    return new ArrayList<>(getRawStages(player));
  }

  private List<String> getRawStages(UUID player) {
    if (!assertIsLoaded()) {
      return null;
    }

    List<String> stages = playerStages.get(player);
    if (stages == null) {
      stages = new CopyOnWriteArrayList<>();
      playerStages.put(player, stages);
    }

    return stages;
  }

  public boolean hasStage(UUID player, String stage) {
    if (!assertIsLoaded()) {
      return false;
    }

    List<String> stages = getRawStages(player);
    return stages.contains(stage);
  }

  public void addStage(UUID player, String stage) {
    if (!assertIsLoaded()) {
      return;
    }

    List<String> stages = getRawStages(player);
    if (!stages.contains(stage)) {
      stages.add(stage);
      shouldSave = true;
    }
  }

  public void removeStage(UUID player, String stage) {
    if (!assertIsLoaded()) {
      return;
    }

    List<String> stages = getRawStages(player);
    if (stages.contains(stage)) {
      stages.remove(stage);
      shouldSave = true;
    }
  }
}
