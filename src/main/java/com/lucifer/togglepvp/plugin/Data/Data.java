package com.lucifer.togglepvp.plugin.Data;

import com.lucifer.togglepvp.plugin.TogglePVP;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class Data extends DataMain {

    private String fileName;
    public Data(TogglePVP togglePVP,String s) {
        super(togglePVP, s);
        this.fileName = s;
    }
    public void setNewPlayerData(UUID player) {
        config.set(player.toString(),"new");
        this.save();
    }
    public String checkMode(UUID player){
        if (config.getString(player.toString()).equals("new")) {
            return "new";
        } else {
            return "old";
        }
    }
    public boolean isRegistered(UUID id) {
        return config.contains(id.toString());
    }
    public void setModeOld(UUID player) {
        config.set(player.toString(),"old");
    }
    public void setModeNew(UUID player) {
        config.set(player.toString(),"new");
    }
}
