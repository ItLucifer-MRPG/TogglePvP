package com.lucifer.togglepvp.plugin;

import com.lucifer.togglepvp.plugin.Util.Util;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WorldManager {

    private TogglePVP plugin;
    private YamlConfiguration config;

    public WorldManager(TogglePVP plugin) {
        this.plugin = plugin;
        this.config = plugin.getMyConfig();
    }


    public boolean forceOldWorld(World world) {
        Object[] force = config.getStringList("Force-Old").toArray();
        for (Object value : force) {
            if (world.getName().equals(value.toString())) {
                return true;
            }
        }
        return false;
    }
    public boolean forceNewWorld(World world) {
        Object[] force = config.getStringList("Force-New").toArray();
        for (Object value : force) {
            if (world.getName().equals(value.toString())) {
                return true;
            }
        }
        return false;
    }
    public boolean worldDisabled(World world) {
        Object[] force = config.getStringList("World-Disabled").toArray();
        for (Object value : force) {
            if (world.getName().equals(value.toString())) {
                return true;
            }
        }
        return false;
    }

}
