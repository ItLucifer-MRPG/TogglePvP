package com.lucifer.togglepvp.plugin.Data;

import com.lucifer.togglepvp.plugin.Main;
import org.bukkit.entity.Player;

public class Data extends DataMain {

    public Data(Main main) {
        super(main, "data.yml");
    }
    public void onNewPlayer(Player player) {
        if (config.contains(player.getUniqueId().toString())) {
            return;
        }
        config.set(player.getUniqueId().toString(),"new");
    }
    public String checkMode(Player player){
        if (config.getString(player.getUniqueId().toString()).equals("new")) {
            return "new";
        } else {
            return "old";
        }
    }
    public void setDefaultOld(Player player) {
        config.set(player.getUniqueId().toString(),"old");
    }
    public void setDefaultNew(Player player) {
        config.set(player.getUniqueId().toString(),"new");
    }
}
