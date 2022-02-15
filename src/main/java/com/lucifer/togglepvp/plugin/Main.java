package com.lucifer.togglepvp.plugin;

import com.lucifer.togglepvp.plugin.Commands.PvP;
import com.lucifer.togglepvp.plugin.Commands.Toggle;
import com.lucifer.togglepvp.plugin.Data.Data;
import com.lucifer.togglepvp.plugin.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class Main extends JavaPlugin implements Listener {

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
        Bukkit.getPluginManager().registerEvents(new Join(), this);
        Bukkit.getPluginManager().registerEvents(new Pvp(),this);
        Bukkit.getPluginManager().registerEvents(new Respawn(),this);
        Bukkit.getPluginManager().registerEvents(new WorldChange(),this);
        Bukkit.getPluginManager().registerEvents(new PetAttackListener(),this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        data.save();
    }
}

