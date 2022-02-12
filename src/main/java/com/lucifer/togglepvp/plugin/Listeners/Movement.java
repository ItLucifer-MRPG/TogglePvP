package com.lucifer.togglepvp.plugin.Listeners;

import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Movement implements Listener {

    Main main = JavaPlugin.getPlugin(Main.class);
    public String prefix = main.getConfig().getString("Prefix");

    @EventHandler
    public void onPvPMove(PlayerMoveEvent event){
        Object[] oldCombat = main.oldCombat.toArray();
        Player p = event.getPlayer();
        for(Object o : oldCombat){
            if (o.equals(p.getName())) {
                if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != 24) {
                    p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
                    return;
                }
            }
        }
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
    }
}
