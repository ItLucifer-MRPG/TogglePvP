package com.lucifer.togglepvp.plugin.Commands;

import com.lucifer.togglepvp.plugin.Commands.base.PlayerCommand;
import com.lucifer.togglepvp.plugin.PlayerManager;
import com.lucifer.togglepvp.plugin.TogglePVP;
import com.lucifer.togglepvp.plugin.Util.Util;
import com.lucifer.togglepvp.plugin.WorldManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PVPMode extends PlayerCommand {

    private YamlConfiguration config;
    private final PlayerManager playerManager;
    private final WorldManager worldManager;
    public String prefix;

    public PVPMode(TogglePVP plugin) {
        super("pvpmode", null, plugin);
        this.config = plugin.getMyConfig();
        this.playerManager = plugin.getPlayerManager();
        this.worldManager = plugin.getWorldManager();
        this.prefix = config.getString("Prefix");
    }

    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        Player p = (Player) sender;
        if (!cmd.getName().equalsIgnoreCase("pvpmode")) {
            return false;
        }
        if (worldManager.forceOldWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("NoPermissionMessage")));
            Util.setOldCombat(p);
            return false;
        }
        if (worldManager.forceNewWorld(p.getWorld())) {
            p.sendMessage(Util.useColors(config.getString("NoPermissionMessage")));
            Util.setNewCombat(p);
            return false;
        }
        if (args.length == 0) {
            if (playerManager.isOldCombat(p.getUniqueId())) {
                p.sendMessage(Util.useColors(prefix + "Your pvp mode is Old Combat"));
                return false;
            } else {
                p.sendMessage(Util.useColors(prefix + "Your pvp mode is New Combat"));
                return false;
            }
        }
        if (args.length == 1) {
            args[0] = args[0].toLowerCase();
            if (args[0].equals("toggle")) {
                if (playerManager.isOldCombat(p.getUniqueId())) {
                    playerManager.setNewPvP(p.getUniqueId());
                    Util.setNewCombat(p);
                    p.sendMessage(Util.useColors(prefix + config.getString("ModeNew")));
                    return true;
                } else {
                    playerManager.setOldPvP(p.getUniqueId());
                    Util.setOldCombat(p);
                    p.sendMessage(Util.useColors(prefix + config.getString("ModeOld")));
                }
            }
            if (args[0].equals("new")) {
                if (playerManager.isNewCombat(p.getUniqueId())) {
                    p.sendMessage(Util.useColors(prefix + config.getString("AlreadyNew")));
                    return true;
                } else {
                    playerManager.setNewPvP(p.getUniqueId());
                    Util.setNewCombat(p);
                    p.sendMessage(Util.useColors(prefix + config.getString( "ModeNew")));
                }
            }
            if (args[0].equals("old")) {
                if (playerManager.isOldCombat(p.getUniqueId())) {
                    p.sendMessage(Util.useColors(prefix + config.getString("AlreadyOld")));
                } else {
                    playerManager.setOldPvP(p.getUniqueId());
                    Util.setOldCombat(p);
                    p.sendMessage(Util.useColors(prefix + config.getString("ModeOld")));
                }
            }
        }
        return false;
    }

    @Override
    public List<String> tabCompletes(CommandSender sender, String[] args) {
        final ArrayList<String> completes = new ArrayList();
        Player p = (Player) sender;
        Object[] forceold = config.getStringList("Force-Old").toArray();
        Object[] forcenew = config.getStringList("Force-New").toArray();
        if (args.length == 1) {
            completes.add("Old");
            completes.add("New");
            completes.add("Toggle");
            for (Object o : forceold) {
                if (o.equals(p.getWorld().getName())) {
                    completes.remove("Old");
                    completes.remove("New");
                    completes.remove("Toggle");
                    break;
                }
            }
            for (Object o : forcenew) {
                if (o.equals(p.getWorld().getName())) {
                    completes.remove("Old");
                    completes.remove("New");
                    completes.remove("Toggle");
                    break;
                }
            }
        }

        Collections.sort(completes);
        return completes;
    }
}
