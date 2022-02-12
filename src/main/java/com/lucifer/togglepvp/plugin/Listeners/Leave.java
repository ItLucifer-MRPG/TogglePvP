package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Leave implements Listener {

    Main main = JavaPlugin.getPlugin(Main.class);

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();
        if (main.oldCombat.contains(p)) {
            main.oldCombat.remove(p);
            return;
        }
    }
}
