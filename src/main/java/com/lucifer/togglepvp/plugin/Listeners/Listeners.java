package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.PlayerManager;
import com.lucifer.togglepvp.plugin.TogglePVP;
import com.lucifer.togglepvp.plugin.Util.Util;
import com.lucifer.togglepvp.plugin.WorldManager;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.player.PlayerRespawnEvent;


public class Listeners implements Listener {

    private PlayerManager playerManager;
    private YamlConfiguration config;
    private WorldManager worldManager;
    public String prefix;

    public Listeners(TogglePVP plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.config = plugin.getMyConfig();
        this.worldManager = plugin.getWorldManager();
        this.prefix = config.getString("Prefix");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        double attackSpeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue();

        if (worldManager.forceOldWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("Forced-Old")));
            Util.setOldCombat(p);
            return;
        }

        if (worldManager.forceNewWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("Forced-New")));
            Util.setNewCombat(p);
            return;
        }
        if (playerManager.isOldCombat(p.getUniqueId())) {
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
            return;
        }
        if (!playerManager.isOldCombat(p.getUniqueId()) && attackSpeed != p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue()) {
            double defaultspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue();
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(defaultspeed);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPvP(EntityDamageByEntityEvent event) {

        Entity target = event.getEntity();
        Entity damagesource = event.getDamager();
        Player attacker = Util.isPlayer(event);

        if (attacker == null) {
            return;
        }
        if (worldManager.worldDisabled(attacker.getWorld())) {
            return;
        }
        if (playerManager.bothEnabled(target.getUniqueId(),attacker.getUniqueId()) &&  playerManager.bothSameCombat(target.getUniqueId(),attacker.getUniqueId())) {
            return;
        }
        if (!playerManager.isEnabled(target.getUniqueId()) && !playerManager.isEnabled(attacker.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("Both-Disabled").replace("[playerName]", target.getName())));
            return;
        }
        if (playerManager.isEnabled(attacker.getUniqueId()) && !playerManager.isEnabled(target.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("Them-Disabled").replace("[playerName]", target.getName())));
            return;
        }
        if (!playerManager.isEnabled(attacker.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("You-Disabled")));
            return;
        }
        if (!playerManager.bothSameCombat(attacker.getUniqueId(),target.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("Different-PvPMode").replace("[playerName]", target.getName())));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPetPvP(EntityDamageByEntityEvent event) {

        Entity target = event.getEntity();
        Entity damagesource = event.getDamager();
        Player attacker = Util.isPlayer(event);

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
        if (worldManager.worldDisabled(attacker.getWorld())) {
            return;
        }
        if (playerManager.bothEnabled(owner.getUniqueId(),attacker.getUniqueId()) && playerManager.bothSameCombat(owner.getUniqueId(),attacker.getUniqueId())) {
            return;
        }
        if (!playerManager.isEnabled(attacker.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("You-Disabled-Pets").replace("[playerName]", tamed.getOwner().getName())));
            return;
        }
        if (!playerManager.isEnabled(owner.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("Owner-Disabled-Pets").replace("[playerName]", tamed.getOwner().getName())));
            return;
        }
        if (!playerManager.bothSameCombat(owner.getUniqueId(), attacker.getUniqueId())) {
            event.setCancelled(true);
            attacker.sendMessage(Util.useColors(prefix+ config.getString("Pet-Different-Mode").replace("[playerName]", tamed.getOwner().getName())));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if (worldManager.forceOldWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("Forced-Old")));
            Util.setOldCombat(p);
            return;
        }

        if (worldManager.forceNewWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("Forced-New")));
            Util.setNewCombat(p);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        if (worldManager.forceOldWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("Forced-Old")));
            Util.setOldCombat(p);
            return;
        }

        if (worldManager.forceNewWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("Forced-New")));
            Util.setNewCombat(p);
        }
    }
}
