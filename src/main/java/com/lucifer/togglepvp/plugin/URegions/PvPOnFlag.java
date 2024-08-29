package com.lucifer.togglepvp.plugin.URegions;

import com.lucifer.togglepvp.plugin.TogglePVP;
import me.TechsCode.UltraRegions.UltraRegions;
import me.TechsCode.UltraRegions.base.item.XMaterial;
import me.TechsCode.UltraRegions.flags.Flag;
import me.TechsCode.UltraRegions.flags.FlagTypes;
import me.TechsCode.UltraRegions.flags.calculator.Result;
import me.TechsCode.UltraRegions.storage.FlagValue;
import me.TechsCode.UltraRegions.storage.Region;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class PvPOnFlag extends Flag implements Listener {

    TogglePVP togglePVP = JavaPlugin.getPlugin(TogglePVP.class);

    ArrayList<Player> wasDisabled = new ArrayList<>();

    public PvPOnFlag(UltraRegions plugin, String id, FlagTypes type) {
        super(plugin, id, type);
    }

    @Override
    public String getName() {
        return "Toggle PvP On";
    }

    @Override
    public String getDescription() {
        return "Forces players within the region to have their pvp turned on";
    }

    @Override
    public XMaterial getIcon() {
        return XMaterial.WOODEN_SWORD;
    }

    @Override
    public FlagValue getDefaultValue() {
        return FlagValue.ALLOW;
    }

    @Override
    public boolean isPlayerSpecificFlag() {
        return true;
    }

    @EventHandler
    public void onPlayerEnter(PlayerMoveEvent event){
        Block fromBlock = event.getFrom().getBlock();
        Block toBlock = event.getTo().getBlock();
        if (fromBlock.equals(toBlock)) {
            return;
        }
        Player player = event.getPlayer();
        Result fromResult = calculate(fromBlock, player);
        Result toResult = calculate(toBlock, player);
        Region toRegion = toResult.getRegion();
        if (toRegion == null || toRegion.equals(fromResult.getRegion())) {
            return;
        }
        if (togglePVP.getPlayerManager().isEnabled(player.getUniqueId())) {
            return;
        }
        wasDisabled.add(player);
        togglePVP.getPlayerManager().setEnabled(player.getUniqueId());

    }

    @EventHandler
    public void onPlayerLeave(PlayerMoveEvent event){
        Block fromBlock = event.getFrom().getBlock();
        Block toBlock = event.getTo().getBlock();
        if (fromBlock.equals(toBlock)) {
            return;
        }
        Player player = event.getPlayer();
        Result fromResult = calculate(fromBlock, player);
        Result toResult = calculate(toBlock, player);
        Region fromRegion = fromResult.getRegion();
        if (fromRegion == null || fromRegion.equals(toResult.getRegion())) {
            return;
        }
        if (!wasDisabled.contains(player)) {
            return;
        }
        wasDisabled.remove(player);
        togglePVP.getPlayerManager().setDisabled(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerTeleportIn(PlayerTeleportEvent event){
        Block fromBlock = event.getFrom().getBlock();
        Block toBlock = event.getTo().getBlock();
        if (fromBlock.equals(toBlock)) {
            return;
        }
        Player player = event.getPlayer();
        Result fromResult = calculate(fromBlock, player);
        Result toResult = calculate(toBlock, player);
        Region toRegion = toResult.getRegion();
        if (toRegion == null || toRegion.equals(fromResult.getRegion())) {
            return;
        }
        if (togglePVP.getPlayerManager().isEnabled(player.getUniqueId())) {
            return;
        }
        wasDisabled.add(player);
        togglePVP.getPlayerManager().setEnabled(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerTeleportOut(PlayerTeleportEvent event) {
        Block fromBlock = event.getFrom().getBlock();
        Block toBlock = event.getTo().getBlock();
        if (fromBlock.equals(toBlock)) {
            return;
        }
        Player player = event.getPlayer();
        Result fromResult = calculate(fromBlock, player);
        Result toResult = calculate(toBlock, player);
        Region fromRegion = fromResult.getRegion();
        if (fromRegion == null || fromRegion.equals(toResult.getRegion())) {
            return;
        }
        if (!wasDisabled.contains(player)) {
            return;
        }
        wasDisabled.remove(player);
        togglePVP.getPlayerManager().setDisabled(player.getUniqueId());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        Location loc = p.getLocation();
        Result r = calculate(loc, p);
        if (!r.isPresent() || r.isSetToAllowed()) {
            return;
        }
        String[] message = event.getMessage().split(" ");
        if (message.length == 1) {
            if (message[0].equalsIgnoreCase("togglepvp") || message[0].equalsIgnoreCase("pvp")) {
                sendMessage(p, r.getRegion());
                event.setCancelled(true);
            }
        }
    }
}
