package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class WorldChange implements Listener {

    Main main = JavaPlugin.getPlugin(Main.class);
    public String prefix = main.getConfig().getString("Prefix");

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        Main main = JavaPlugin.getPlugin(Main.class);
        double attackspeed = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue();
        Object[] forceold = main.getConfig().getStringList("Force-Old").toArray();
        for (Object value : forceold) {
            if (value.equals(p.getWorld().getName())) {
                p.sendMessage(UseColors(main.getConfig().getString("Forced-Old")));
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
                return;
            }
        }
        Object[] forcenew = main.getConfig().getStringList("Force-New").toArray();
        for (Object o : forcenew) {
            if (o.equals(p.getWorld().getName())) {
                p.sendMessage(UseColors(main.getConfig().getString("Forced-New")));
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
    private @NotNull String UseColors(String args) {
        return ChatColor.translateAlternateColorCodes('&',args);
    }
}
