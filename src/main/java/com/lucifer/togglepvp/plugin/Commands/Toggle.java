package com.lucifer.togglepvp.plugin.Commands;

import com.lucifer.togglepvp.plugin.Data.Data;
import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Toggle implements CommandExecutor {

    Main main = JavaPlugin.getPlugin(Main.class);
    public String prefix = main.getConfig().getString("Prefix");
    private Data data;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.data = new Data(main);
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (!cmd.getName().equalsIgnoreCase("togglepvp")) {
            return true;
        }
        if (args.length == 0) {
            Object[] disable = main.getConfig().getStringList("Disabled-Worlds").toArray();
            for (Object value : disable) {
                if (value.equals(player.getWorld().getName())) {
                    player.sendMessage(UseColors(main.getConfig().getString("NoPermissionMessage")));
                    return true;
                }
            }
            if (main.enabled.contains(player)) {
                main.enabled.remove(player);
                player.sendMessage(UseColors(prefix + main.getConfig().getString("PvPOff")));
                return true;
            }
            if (!main.enabled.contains(player)) {
                main.enabled.add(player);
                player.sendMessage(UseColors(prefix + main.getConfig().getString("PvPOn")));
            }
        }
        return false;
    }
    private String UseColors(String args) {
        return ChatColor.translateAlternateColorCodes('&',args);
    }
}