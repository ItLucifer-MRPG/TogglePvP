package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Pvp implements Listener {

    Main main = JavaPlugin.getPlugin(Main.class);
    public String prefix = main.getConfig().getString("Prefix");

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {

        Main main = JavaPlugin.getPlugin(Main.class);
        if (!(event.getDamager() instanceof Player attacker) || !(event.getEntity() instanceof Player target)) {
            return;
        }
        Object[] disable = main.getConfig().getStringList("Disabled-Worlds").toArray();
        for (Object value : disable) {
            if (value.equals(attacker.getWorld().getName())) {
                return;
            }
        }
        if (main.enabled.contains(target) && main.enabled.contains(attacker) && main.oldCombat.contains(target) && main.oldCombat.contains(attacker)) {
            return;
        }
        if (!main.enabled.contains(target) && main.enabled.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(UseColors(prefix+main.getConfig().getString("Them-Disabled").replace("[playerName]", target.getName())));
            return;
        }
        if (!main.enabled.contains(target) && !main.enabled.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(UseColors(prefix+main.getConfig().getString("Both-Disabled").replace("[playerName]", target.getName())));
            return;
        }
        if (!main.enabled.contains(attacker) && main.enabled.contains(target)) {
            event.setCancelled(true);
            attacker.sendMessage(UseColors(prefix+main.getConfig().getString("You-Disabled")));
            return;
        }
        if (main.oldCombat.contains(attacker) && !main.oldCombat.contains(target)) {
            event.setCancelled(true);
            attacker.sendMessage(UseColors(prefix+main.getConfig().getString("Different-PvPMode").replace("[playerName]", target.getName())));
            return;
        }
        if (!main.oldCombat.contains(attacker) && main.oldCombat.contains(target)) {
            event.setCancelled(true);
            attacker.sendMessage(UseColors(prefix+main.getConfig().getString("Different-PvPMode").replace("[playerName]", target.getName())));
        }
    }
    private @NotNull String UseColors(String args) {
        return ChatColor.translateAlternateColorCodes('&',args);
    }
}
