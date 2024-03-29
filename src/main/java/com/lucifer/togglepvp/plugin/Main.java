package com.lucifer.togglepvp.plugin;

import com.lucifer.togglepvp.plugin.Commands.PvP;
import com.lucifer.togglepvp.plugin.Commands.Toggle;
import com.lucifer.togglepvp.plugin.Data.Data;
import com.lucifer.togglepvp.plugin.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class Main extends JavaPlugin {

    public ArrayList<Player> enabled = new ArrayList<>();
    public ArrayList<Player> oldCombat = new ArrayList<>();
    private Data data;

    @Override
    public void onEnable() {

        getCommand("pvp").setExecutor(new PvP());
        getCommand("pvp").setTabCompleter(new PvP());
        getCommand("togglepvp").setExecutor(new Toggle());

        this.data = new Data(this);
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        registerEvents((Listener) new Listeners(this));

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        data.save();
    }

    public void registerEvents(Listener l){
        Bukkit.getServer().getPluginManager().registerEvents(l,this);
    }

    public ArrayList<Player> getEnabled(){
        return this.enabled;
    }
    public ArrayList<Player> getOldCombat(){
        return this.oldCombat;
    }

    public static String useColors(String args) {
        return ChatColor.translateAlternateColorCodes('&',args);
    }


}

