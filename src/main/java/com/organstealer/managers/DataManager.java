package com.organstealer.managers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.organstealer.models.PlayerOrgans;
import com.organstealer.enums.Organ;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    
    private JavaPlugin plugin;
    private File dataFolder;
    private File playersFolder;
    private Map<UUID, PlayerOrgans> cachedPlayers;
    
    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cachedPlayers = new HashMap<>();
        
        this.dataFolder = new File(plugin.getDataFolder(), "data");
        this.playersFolder = new File(dataFolder, "players");
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        if (!playersFolder.exists()) {
            playersFolder.mkdirs();
        }
    }
    
    public void savePlayerData(Player player) {
        savePlayerData(player.getUniqueId(), player.getName());
    }
    
    public void savePlayerData(UUID uuid, String playerName) {
        PlayerOrgans organs = cachedPlayers.get(uuid);
        if (organs == null) return;
        
        File playerFile = new File(playersFolder, uuid + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        
        config.set("uuid", uuid.toString());
        config.set("name", playerName);
        config.set("kills", organs.getTotalKills());
        
        for (Organ organ : Organ.values()) {
            config.set("organs." + organ.name(), organs.getOrganStrength(organ));
        }
        
        try {
            config.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save player data for " + playerName);
            e.printStackTrace();
        }
    }
    
    public void loadPlayerData(UUID uuid, String playerName) {
        File playerFile = new File(playersFolder, uuid + ".yml");
        
        if (!playerFile.exists()) {
            PlayerOrgans newOrgans = new PlayerOrgans(uuid, playerName);
            cachedPlayers.put(uuid, newOrgans);
            return;
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        PlayerOrgans organs = new PlayerOrgans(uuid, playerName);
        
        if (config.contains("kills")) {
            for (int i = 0; i < config.getInt("kills"); i++) {
                organs.addKill();
            }
        }
        
        for (Organ organ : Organ.values()) {
            if (config.contains("organs." + organ.name())) {
                int strength = config.getInt("organs." + organ.name());
                organs.setOrganStrength(organ, strength);
            }
        }
        
        cachedPlayers.put(uuid, organs);
    }
    
    public void loadAllPlayerData() {
        if (!playersFolder.exists()) return;
        
        File[] files = playersFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            plugin.getLogger().info("Loading " + files.length + " player data files...");
            for (File file : files) {
                String filename = file.getName().replace(".yml", "");
                try {
                    UUID uuid = UUID.fromString(filename);
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String playerName = config.getString("name", "Unknown");
                    loadPlayerData(uuid, playerName);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid player data file: " + file.getName());
                }
            }
        }
    }
    
    public void saveAllPlayerData() {
        plugin.getLogger().info("Saving " + cachedPlayers.size() + " player data files...");
        for (PlayerOrgans organs : cachedPlayers.values()) {
            savePlayerData(organs.getPlayerUUID(), organs.getPlayerName());
        }
    }
}