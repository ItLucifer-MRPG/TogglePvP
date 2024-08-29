package com.lucifer.togglepvp.plugin;

import com.lucifer.togglepvp.plugin.Data.Data;
import com.lucifer.togglepvp.plugin.Util.Util;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PlayerManager implements Listener {

    public ArrayList<UUID> enabled = new ArrayList<>();

    private Data data;

    public PlayerManager(TogglePVP plugin) {
        this.data = new Data(plugin,"data.yml");
        Util.registerEvents(this,plugin);
    }
    // Player PVP
    public boolean isEnabled(UUID id) {
        if (!enabled.contains(id)) {
            return false;
        }
        return true;
    }
    public boolean bothEnabled(UUID p1,UUID p2) {
        return isEnabled(p1) && isEnabled(p2);
    }
    public void setEnabled(UUID id) {
        enabled.add(id);
    }
    public void setDisabled(UUID id) {
        enabled.remove(id);
    }
    public ArrayList<UUID> getEnabled(){
        return enabled;
    }

    // Player PVP Mode Methods
    public boolean isOldCombat(UUID id) {
        return Objects.equals(data.checkMode(id), "old");
    }
    public boolean isNewCombat(UUID id) {
        return Objects.equals(data.checkMode(id), "new");
    }
    public boolean bothSameCombat(UUID p1, UUID p2) {
        return isOldCombat(p1)&&isOldCombat(p2)|| isNewCombat(p1) && isNewCombat(p2);
    }
    public void setOldPvP(UUID id) {
        data.setModeOld(id);
    }
    public void setNewPvP(UUID id) {data.setModeOld(id);}

    // Save/Reloaders
    public void reload(TogglePVP plugin) {
        data.save();
        this.data = new Data(plugin,"data.yml");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        enabled.clear();
    }

    public void save(){
        data.save();
    }

    // Listeners
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (data.isRegistered(id)) {
            return;
        }
        data.setNewPlayerData(id);
        data.save();
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        UUID id = event.getPlayer().getUniqueId();
        enabled.remove(id);
    }


}
