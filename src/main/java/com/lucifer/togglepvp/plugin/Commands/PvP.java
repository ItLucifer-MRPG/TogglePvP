package com.lucifer.togglepvp.plugin.Commands;

import com.lucifer.togglepvp.plugin.Data.Data;
import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PvP implements TabCompleter, CommandExecutor{

    Main main = JavaPlugin.getPlugin(Main.class);
    public String prefix = main.getConfig().getString("Prefix");
    private Data data;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.data = new Data(main);
        Object[] forceold = main.getConfig().getStringList("Force-Old").toArray();
        Object[] forcenew = main.getConfig().getStringList("Force-New").toArray();
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (!cmd.getName().equalsIgnoreCase("PvP")) {
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
                player.sendMessage(UseColors( prefix + main.getConfig().getString("PvPOff")));
                return true;
            }
            if(!main.enabled.contains(player)) {
                main.enabled.add(player);
                player.sendMessage(UseColors(prefix + main.getConfig().getString("PvPOn")));
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("Old")) {
                for (Object value : forceold) {
                    if (value.equals(player.getWorld().getName())) {
                        player.sendMessage(UseColors(main.getConfig().getString("NoPermissionMessage")));
                        return true;
                    }
                }
                for (Object o : forcenew) {
                    if (o.equals(player.getWorld().getName())) {
                        player.sendMessage(UseColors(main.getConfig().getString("NoPermissionMessage")));
                        return true;
                    }
                }
                if(main.oldCombat.contains(player)){
                    player.sendMessage(UseColors(prefix+main.getConfig().getString("AlreadyOld")));
                    return true;
                }
                if(!main.oldCombat.contains(player)) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
                    main.oldCombat.add(player);
                    player.sendMessage(UseColors(prefix+main.getConfig().getString("ModeOld")));
                    return true;
                }

            }
            if (args[0].equalsIgnoreCase("New")) {
                for (Object value : forceold) {
                    if (value.equals(player.getWorld().getName())) {
                        player.sendMessage(UseColors(main.getConfig().getString("NoPermissionMessage")));
                        return true;
                    }
                }
                for (Object o : forcenew) {
                    if (o.equals(player.getWorld().getName())) {
                        player.sendMessage(UseColors(main.getConfig().getString("NoPermissionMessage")));
                        return true;
                    }
                }
                if (main.oldCombat.contains(player)) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                    main.oldCombat.remove(player);
                    player.sendMessage(UseColors(prefix+main.getConfig().getString("ModeNew")));
                    return true;
                }
                if (!main.oldCombat.contains(player)) {
                    player.sendMessage(UseColors(prefix + main.getConfig().getString("AlreadyNew")));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("setdefault")) {
                player.sendMessage(UseColors(prefix + main.getConfig().getString("SpecifyMode")));
            }
            if (args[0].equalsIgnoreCase("Reload")) {
                if (!player.hasPermission("togglepvp.admin")) {
                    player.sendMessage(UseColors(main.getConfig().getString("NoPermission")));
                }
                main.saveDefaultConfig();
                main.reloadConfig();
                player.sendMessage(UseColors(prefix+main.getConfig().getString("ReloadMessage")));
            }
        }
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("Old") && data.checkMode(player).equals("new")) {
                data.setDefaultOld(player);
                data.save();
                player.sendMessage(UseColors(prefix + main.getConfig().getString("DefaultOld")));
                return true;
            }
            if (args[1].equalsIgnoreCase("New") && data.checkMode(player).equals("old")) {
                data.setDefaultNew(player);
                data.save();
                player.sendMessage(UseColors(prefix + main.getConfig().getString("DefaultNew")));
                return true;
            }
            if (args[1].equalsIgnoreCase("New") && data.checkMode(player).equals("new")) {
                player.sendMessage(UseColors(prefix + main.getConfig().getString("AlreadyDefaultNew")));
                return true;
            }
            if (args[1].equalsIgnoreCase("Old") && data.checkMode(player).equals("old")) {
                player.sendMessage(UseColors(prefix + main.getConfig().getString("AlreadyDefaultOld")));
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete (CommandSender sender, Command command, String alias, String[]args){
        final ArrayList<String> completes = new ArrayList();
        Player p = (Player) sender;
        Object[] forceold = main.getConfig().getStringList("Force-Old").toArray();
        Object[] forcenew = main.getConfig().getStringList("Force-New").toArray();
        if (args.length == 1) {
            completes.add("Old");
            completes.add("New");
            completes.add("SetDefault");
            for (Object o : forceold) {
                if (o.equals(p.getWorld().getName())) {
                    completes.remove("Old");
                    completes.remove("New");
                    break;
                }
            }
            for (Object o : forcenew) {
                if (o.equals(p.getWorld().getName())) {
                    completes.remove("Old");
                    completes.remove("New");
                    break;
                }
            }
            if(sender.hasPermission("togglepvp.admin") || sender.isOp()) {
                completes.add("Reload");
            }
        }
        if (args.length == 2) {
            completes.add("Old");
            completes.add("New");
        }

        Collections.sort(completes);
        return completes;
    }
    private String UseColors(String args) {
        return ChatColor.translateAlternateColorCodes('&',args);
    }
}

