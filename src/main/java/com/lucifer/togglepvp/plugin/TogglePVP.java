package com.lucifer.togglepvp.plugin;

import com.lucifer.togglepvp.plugin.Commands.PVPMode;
import com.lucifer.togglepvp.plugin.Commands.PvP;
import com.lucifer.togglepvp.plugin.Data.Config;
import com.lucifer.togglepvp.plugin.Listeners.*;
import com.lucifer.togglepvp.plugin.Util.Util;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;


public class TogglePVP extends JavaPlugin {

    private Config config;

    private PlayerManager playerManager;
    private WorldManager worldManager;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        this.config = new Config(this);

        this.playerManager = new PlayerManager(this);
        this.worldManager = new WorldManager(this);

        new PvP(this);
        new PVPMode(this);

        Util.registerEvents(new Listeners(this), this);

    }
    @Override
    public void onDisable() {
        super.onDisable();
        playerManager.save();
        config.save();
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
    public @NotNull YamlConfiguration getMyConfig() {
        return config.getConfig();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

}

