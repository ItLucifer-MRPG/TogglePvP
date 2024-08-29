package com.lucifer.togglepvp.plugin.Commands;

import com.lucifer.togglepvp.plugin.Commands.base.Command;
import com.lucifer.togglepvp.plugin.PlayerManager;
import com.lucifer.togglepvp.plugin.TogglePVP;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ToggleCommand extends Command {

    private String prefix;
    private PlayerManager pm;
    private YamlConfiguration config;

    public ToggleCommand(TogglePVP plugin) {
        super("togglepvp",plugin.getMyConfig().getString("PERM_ADMIN"),plugin);
        this.pm = plugin.getPlayerManager();
        this.config = plugin.getMyConfig();
        this.prefix = plugin.getMyConfig().getString("Prefix");
    }
    @Override
    public boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("togglepvp")) {
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {

        }

        return false;
    }

    @Override
    public List<String> tabCompletes(CommandSender sender, String[] args) {
        return null;
    }
}