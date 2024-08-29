package com.lucifer.togglepvp.plugin.Util;

import com.lucifer.togglepvp.plugin.TogglePVP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Util {

    public Util() {
    }
    public static String useColors(String args) {
        return ChatColor.translateAlternateColorCodes('&',args);
    }

    public static void setNewCombat(Player p) {
        if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue() == 4) {
            return;
        }
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);

    }
    public static void setOldCombat(Player p) {
        if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue() == 24) {
            return;
        }
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
    }
    public static void registerEvents(Listener l, TogglePVP plugin){
        Bukkit.getServer().getPluginManager().registerEvents(l,plugin);
    }
    public static void unregisterEvents(Listener l){
        HandlerList.unregisterAll(l);
    }

    public static Player isPlayer(EntityDamageByEntityEvent event) {

        Entity target = event.getEntity();
        Entity damagesource = event.getDamager();

        if (!(target instanceof Player)) {
            return null;
        }
        if (damagesource instanceof Player) {
            return (Player) damagesource;
        } else if (damagesource instanceof Arrow) {
            Arrow arrow = (Arrow) damagesource;
            if (!(arrow.getShooter() instanceof Player)) {
                return null;
            }
            return (Player)arrow.getShooter();
        } else if(damagesource instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion)damagesource;
            if(!(potion.getShooter() instanceof Player)) {
                return null;
            }
            return  (Player) potion.getShooter();
        } else if(damagesource instanceof Egg) {
            Egg egg = (Egg) damagesource;
            if(!(egg.getShooter() instanceof Player)) {
                return null;
            }
            return  (Player) egg.getShooter();
        } else if(damagesource instanceof Snowball) {
            Snowball ball = (Snowball) damagesource;
            if(!(ball.getShooter() instanceof Player)) {
                return null;
            }
            return  (Player) ball.getShooter();
        } else if(damagesource instanceof FishHook) {
            FishHook hook = (FishHook) damagesource;
            if(!(hook.getShooter() instanceof Player)) {
                return null;
            }
            return  (Player) hook.getShooter();
        } else if(damagesource instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) damagesource;
            if(!(pearl.getShooter() instanceof Player)) {
                return null;
            }
            return  (Player) pearl.getShooter();
        } else if(damagesource instanceof Trident){
            Trident trident = (Trident) damagesource;
            if(!(trident.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) trident.getShooter();
        } else {
            return null;
        }
    }
    public static Player isPlayerPet(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damagesource = event.getDamager();

        if (!(target instanceof Tameable)) {
            return null;
        }
        Tameable tamed = (Tameable) target;
        if (tamed.getOwner() == null){
            return null;
        }
        AnimalTamer owner = tamed.getOwner();
        if (damagesource instanceof Player) {
            return (Player) damagesource;
        } else if (damagesource instanceof Arrow) {
            Arrow arrow = (Arrow) damagesource;
            if (!(arrow.getShooter() instanceof Player)) {
                return null;
            }
            return (Player)arrow.getShooter();
        } else if(damagesource instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion)damagesource;
            if(!(potion.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) potion.getShooter();
        } else if(damagesource instanceof Egg) {
            Egg egg = (Egg) damagesource;
            if(!(egg.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) egg.getShooter();
        } else if(damagesource instanceof Snowball) {
            Snowball ball = (Snowball) damagesource;
            if(!(ball.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) ball.getShooter();
        } else if(damagesource instanceof FishHook) {
            FishHook hook = (FishHook) damagesource;
            if(!(hook.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) hook.getShooter();
        } else if(damagesource instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) damagesource;
            if(!(pearl.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) pearl.getShooter();
        } else if(damagesource instanceof Trident){
            Trident trident = (Trident) damagesource;
            if(!(trident.getShooter() instanceof Player)) {
                return null;
            }
            return (Player) trident.getShooter();
        } else {
            return null;
        }
    }
}
