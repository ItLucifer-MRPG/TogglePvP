package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.Data.Data;
import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Listeners {

    private Main main;
    private Data data;
    public String prefix = main.getConfig().getString("Prefix");

    public Listeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.data = new Data(main);
        Player p = event.getPlayer();
        double attackSpeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue();

        data.onNewPlayer(p);
        data.save();

        Object[] forceold = main.getConfig().getStringList("Force-Old").toArray();
        for (Object value : forceold) {
            if (value.equals(p.getWorld().getName())) {
                p.sendMessage(Main.useColors(main.getConfig().getString("Forced-Old")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
                return;
            }
        }
        Object[] forcenew = main.getConfig().getStringList("Force-New").toArray();
        for (Object o : forcenew) {
            if (o.equals(p.getWorld().getName())) {
                p.sendMessage(Main.useColors(main.getConfig().getString("Forced-New")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                return;
            }
        }
        if (data.checkMode(p).equals("old")) {
            main.oldCombat.add(p);
        }
        if (main.oldCombat.contains(p)) {
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
            return;
        }
        if (!main.oldCombat.contains(p) && attackSpeed != p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue()) {
            double defaultspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue();
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(defaultspeed);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();
        if (main.oldCombat.contains(p)) {
            main.oldCombat.remove(p);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPvP(EntityDamageByEntityEvent event) {

        Entity target = event.getEntity();
        Entity damagesource = event.getDamager();
        Player attacker = null;

        if (!(target instanceof Player)) {
            return;
        }
        if (damagesource instanceof Player) {
            attacker = (Player) damagesource;
        } else if (damagesource instanceof Arrow) {
            Arrow arrow = (Arrow) damagesource;
            if (!(arrow.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player)arrow.getShooter();
        } else if(damagesource instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion)damagesource;
            if(!(potion.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) potion.getShooter();
        } else if(damagesource instanceof Egg) {
            Egg egg = (Egg) damagesource;
            if(!(egg.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) egg.getShooter();
        } else if(damagesource instanceof Snowball) {
            Snowball ball = (Snowball) damagesource;
            if(!(ball.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) ball.getShooter();
        } else if(damagesource instanceof FishHook) {
            FishHook hook = (FishHook) damagesource;
            if(!(hook.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) hook.getShooter();
        } else if(damagesource instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) damagesource;
            if(!(pearl.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) pearl.getShooter();
        } else if(damagesource instanceof Trident){
            Trident trident = (Trident) damagesource;
            if(!(trident.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) trident.getShooter();
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
        } else if (!main.enabled.contains(target) && main.enabled.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(Main.useColors(prefix+main.getConfig().getString("Them-Disabled").replace("[playerName]", target.getName())));
        } else if (!main.enabled.contains(target) && !main.enabled.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("Both-Disabled").replace("[playerName]", target.getName())));
        } else if (!main.enabled.contains(attacker) && main.enabled.contains(target)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("You-Disabled")));
        } else if (main.oldCombat.contains(attacker) && !main.oldCombat.contains(target)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("Different-PvPMode").replace("[playerName]", target.getName())));
        } else if (!main.oldCombat.contains(attacker) && main.oldCombat.contains(target)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("Different-PvPMode").replace("[playerName]", target.getName())));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPetPvP(EntityDamageByEntityEvent event) {
        Player attacker = null;
        Entity target = event.getEntity();
        Entity damagesource = event.getDamager();

        if (!(target instanceof Tameable)) {
            return;
        }
        Tameable tamed = (Tameable) target;
        if (tamed.getOwner() == null){
            return;
        }
        AnimalTamer owner = tamed.getOwner();
        if (damagesource instanceof Player) {
            attacker = (Player) damagesource;
        } else if (damagesource instanceof Arrow) {
            Arrow arrow = (Arrow) damagesource;
            if (!(arrow.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player)arrow.getShooter();
        } else if(damagesource instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion)damagesource;
            if(!(potion.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) potion.getShooter();
        } else if(damagesource instanceof Egg) {
            Egg egg = (Egg) damagesource;
            if(!(egg.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) egg.getShooter();
        } else if(damagesource instanceof Snowball) {
            Snowball ball = (Snowball) damagesource;
            if(!(ball.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) ball.getShooter();
        } else if(damagesource instanceof FishHook) {
            FishHook hook = (FishHook) damagesource;
            if(!(hook.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) hook.getShooter();
        } else if(damagesource instanceof EnderPearl) {
            EnderPearl pearl = (EnderPearl) damagesource;
            if(!(pearl.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) pearl.getShooter();
        } else if(damagesource instanceof Trident){
            Trident trident = (Trident) damagesource;
            if(!(trident.getShooter() instanceof Player)) {
                return;
            }
            attacker = (Player) trident.getShooter();
        } else {
            return;
        }
        Object[] disable = main.getConfig().getStringList("Disabled-Worlds").toArray();
        for (Object value : disable) {
            if (value.equals(attacker.getWorld().getName())) {
                return;
            }
        }
        if (main.enabled.contains(owner) && main.enabled.contains(attacker) && main.oldCombat.contains(owner) && main.oldCombat.contains(attacker)) {
            return;
        } else if (!main.enabled.contains(owner) && main.enabled.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("Owner-Disabled-Pets").replace("[playerName]", tamed.getOwner().getName())));
        } else if (!main.enabled.contains(owner) && !main.enabled.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("You-Disabled-Pets").replace("[playerName]", tamed.getOwner().getName())));
        } else if (!main.oldCombat.contains(owner) && main.oldCombat.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("Pet-Different-Mode").replace("[playerName]", tamed.getOwner().getName())));
        } else if (main.oldCombat.contains(owner) && !main.oldCombat.contains(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(main.useColors(prefix+main.getConfig().getString("Pet-Different-Mode").replace("[playerName]", tamed.getOwner().getName())));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        Main main = JavaPlugin.getPlugin(Main.class);
        double attackspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue();
        Object[] forceold = main.getConfig().getStringList("Force-Old").toArray();
        for (Object value : forceold) {
            if (value.equals(p.getWorld().getName())) {
                p.sendMessage(main.useColors(main.getConfig().getString("Forced-Old")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
                return;
            }
        }
        Object[] forcenew = main.getConfig().getStringList("Force-New").toArray();
        for (Object o : forcenew) {
            if (o.equals(p.getWorld().getName())) {
                p.sendMessage(main.useColors(main.getConfig().getString("Forced-New")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                return;
            }
        }
        if (main.oldCombat.contains(p) && attackspeed != 24) {
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
        } else {
            double defaultspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue();
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(defaultspeed);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        Main main = JavaPlugin.getPlugin(Main.class);
        double attackspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue();
        Object[] forceold = main.getConfig().getStringList("Force-Old").toArray();
        for (Object value : forceold) {
            if (value.equals(p.getWorld().getName())) {
                p.sendMessage(main.useColors(main.getConfig().getString("Forced-Old")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
                return;
            }
        }
        Object[] forcenew = main.getConfig().getStringList("Force-New").toArray();
        for (Object o : forcenew) {
            if (o.equals(p.getWorld().getName())) {
                p.sendMessage(main.useColors(main.getConfig().getString("Forced-New")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                return;
            }
        }
        if (main.oldCombat.contains(p) && attackspeed != 24) {
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
        } else {
            double defaultspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue();
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(defaultspeed);
        }
    }
}
