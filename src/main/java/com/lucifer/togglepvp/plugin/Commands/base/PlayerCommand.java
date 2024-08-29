package com.lucifer.togglepvp.plugin.Commands.base;

import com.lucifer.togglepvp.plugin.TogglePVP;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class PlayerCommand implements Listener, CommandExecutor, TabCompleter {
    private final String name;
    private final String perm;

    public PlayerCommand(String name, String perm, TogglePVP plugin) {
        this.perm = perm;
        this.name = name;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        Objects.requireNonNull(plugin.getServer().getPluginCommand(name)).setExecutor(this);
        Objects.requireNonNull(plugin.getServer().getPluginCommand(name)).setTabCompleter(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            return false;
        }
        if (perm == null) {
            return execute(sender,command, args);
        } else if (p.hasPermission(perm) || p.isOp()) {
            return execute(sender, command, args);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        return tabCompletes(sender,args);
    }

    public abstract boolean execute(CommandSender sender, org.bukkit.command.Command cmd, String[] args);

    public abstract List<String> tabCompletes(CommandSender sender, String[] args);

    @EventHandler
    public void command(PlayerCommandSendEvent e) {
        if (perm == null) {
            return;
        }
        if (e.getPlayer().hasPermission(perm)) {
            return;
        }
        e.getCommands().remove(name);
    }



    public String name() {
        return name;
    }
}