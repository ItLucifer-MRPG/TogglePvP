package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Pvp implements Listener {

    Main main = JavaPlugin.getPlugin(Main.class);
    public String prefix = main.getConfig().getString("Prefix");

    @EventHandler(priority = EventPriority.HIGH)
    public void onPvP(EntityDamageEvent event) {

        Player attacker = null;
        Entity target = event.getEntity();

        Main main = JavaPlugin.getPlugin(Main.class);
        Entity damagesource = ((EntityDamageByEntityEvent)event).getDamager();

        if (!(target instanceof Player)) {
            return;
        }
        if (damagesource instanceof Player) {
            attacker = (Player) damagesource;
        } else if (damagesource instanceof Arrow) {
            Arrow arrow = (Arrow) damagesource;
            if (arrow.getShooter() instanceof Player) {
                attacker = (Player)arrow.getShooter();
            }
        } else if(damagesource instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion)damagesource;
            if(potion.getShooter() instanceof Player) {
                attacker = (Player) potion.getShooter();
            }
        } else if(damagesource instanceof Egg) {
            Egg egg = (Egg) damagesource;
            if(egg.getShooter() instanceof Player) {
                attacker = (Player) egg.getShooter();
            }
        } else if(damagesource instanceof Snowball) {
            Snowball ball = (Snowball) damagesource;
            if(ball.getShooter() instanceof Player) {
                attacker = (Player) ball.getShooter();
            }
        } else if(damagesource instanceof FishHook) {
            FishHook hook = (FishHook) damagesource;
            if(hook.getShooter() instanceof Player) {
                attacker = (Player) hook.getShooter();
            }
        } else if(damagesource instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) damagesource;
            if(pearl.getShooter() instanceof Player) {
                attacker = (Player) pearl.getShooter();
            }
        } else if(damagesource instanceof Trident){
            Trident trident = (Trident) damagesource;
            if(trident.getShooter() instanceof Player) {
                attacker = (Player) trident.getShooter();
            }
        } else {
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
