package com.lucifer.togglepvp.plugin.Data;

import com.lucifer.togglepvp.plugin.TogglePVP;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private YamlConfiguration config;

    private File file;

    public Config(TogglePVP plugin) {
        this.file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        plugin.saveConfig();

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
