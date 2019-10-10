package me.mralecroyt.administrador;

import me.mralecroyt.LobbyCore.*;
import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.configuration.*;

public class ConfigAdmin {
    private CoreMain plugin;
    private static File configFile;
    private static File JoinsFile;
    private static FileConfiguration config;
    private static FileConfiguration joins;

    public ConfigAdmin() {
        this.plugin = CoreMain.getInstance();
    }

    public void createConfigConfig() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdirs();
        }
        ConfigAdmin.configFile = new File(this.plugin.getDataFolder(), "config.yml");;
        if (!ConfigAdmin.configFile.exists()) {
            ConfigAdmin.configFile.getParentFile().mkdirs();
            this.plugin.saveResource("config.yml", false);
        }
        ConfigAdmin.config = new YamlConfiguration();
        try {
            ConfigAdmin.config.load(ConfigAdmin.configFile);
        } catch (IOException | InvalidConfigurationException ex2) {
            final Exception ex;
            final Exception e = ex2;
            e.printStackTrace();
        }
    }

    public void createConfigJoins() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdirs();
        }
        ConfigAdmin.JoinsFile = new File(this.plugin.getDataFolder(), "customjoinmessages.yml");
        if (!ConfigAdmin.JoinsFile.exists()) {
            ConfigAdmin.JoinsFile.getParentFile().mkdirs();
            this.plugin.saveResource("customjoinmessages.yml", false);
        }
        ConfigAdmin.joins = new YamlConfiguration();
        try {
            ConfigAdmin.joins.load(ConfigAdmin.JoinsFile);
        } catch (IOException | InvalidConfigurationException ex2) {
            final Exception ex;
            final Exception e = ex2;
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfigConfig() {
        return ConfigAdmin.config;
    }

    public static FileConfiguration getConfigJoins() {
        return ConfigAdmin.joins;
    }

    public static void saveConfigConfig() {
        try {
            ConfigAdmin.config.save(ConfigAdmin.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfigJoins() {
        try {
            ConfigAdmin.joins.save(ConfigAdmin.JoinsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfigConfig() {
        ConfigAdmin.config = YamlConfiguration.loadConfiguration(ConfigAdmin.configFile);

    }
    public static void reloadConfigJoins() {
        ConfigAdmin.config = YamlConfiguration.loadConfiguration(ConfigAdmin.JoinsFile);

    }
}

