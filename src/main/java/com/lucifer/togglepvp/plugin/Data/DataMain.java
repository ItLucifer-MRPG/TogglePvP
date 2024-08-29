package com.lucifer.togglepvp.plugin.Data;

import com.lucifer.togglepvp.plugin.TogglePVP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class DataMain {

    protected TogglePVP togglePVP;
    private final File file;
    protected @NotNull YamlConfiguration config;

    public DataMain(TogglePVP togglePVP, String fileName) {
        this.togglePVP = togglePVP;
        this.file = new File(togglePVP.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
