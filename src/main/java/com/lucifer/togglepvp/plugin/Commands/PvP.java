package com.lucifer.togglepvp.plugin.Commands;

import com.lucifer.togglepvp.plugin.Commands.base.PlayerCommand;
import com.lucifer.togglepvp.plugin.PlayerManager;
import com.lucifer.togglepvp.plugin.TogglePVP;
import com.lucifer.togglepvp.plugin.Util.Util;
import com.lucifer.togglepvp.plugin.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PvP extends PlayerCommand {

    private YamlConfiguration config;
    private final PlayerManager playerManager;
    private final WorldManager worldManager;
    public String prefix;

    public PvP(TogglePVP plugin) {
        super("pvp",null,plugin);
        this.config = plugin.getMyConfig();
        this.playerManager = plugin.getPlayerManager();
        this.worldManager = plugin.getWorldManager();
        this.prefix = plugin.getMyConfig().getString("Prefix");
    }
    @Override
    public boolean execute(CommandSender sender,Command cmd, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (!cmd.getName().equalsIgnoreCase("PvP")) {
            return true;
        }
        if (worldManager.worldDisabled(player.getWorld())) {
            player.sendMessage(Util.useColors(config.getString("NoPermissionMessage")));
            return false;
        }
        if (!playerManager.isEnabled(player.getUniqueId())) {
            playerManager.setEnabled(player.getUniqueId());
            player.sendMessage(Util.useColors(prefix + config.getString("PvPOn")));
        } else {
            playerManager.setDisabled(player.getUniqueId());
            player.sendMessage(Util.useColors(prefix + config.getString("PvPOff")));
        }
        return true;
    }

    @Override
    public List<String> tabCompletes(CommandSender sender, String[] args) {
        final ArrayList<String> completes = new ArrayList();
        Player p = (Player) sender;

        Collections.sort(completes);
        return completes;
    }
}
