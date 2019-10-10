package me.mralecroyt.Listener.Utils;


import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import me.mralecroyt.LobbyCore.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

public class CustomJoins
{
    private String permission;
    private String join_message;
    private String quit_message;
    private String join_gamemode;
    private List<String> join_comands;
    private String join_firework;
    private String join_sound;
    private JavaPlugin plugin;
    private int num;

    public CustomJoins(final String permission, final JavaPlugin plugin) {
        this.permission = permission;
        this.plugin = plugin;
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean hasJoinMessage() {
        return this.join_message != null;
    }


    public boolean hasJoinFirework() {
        return this.join_firework != null;
    }

    public boolean hasJoinSound() {
        return this.join_sound != null;
    }


    public boolean hasJoinComands() {
        return this.join_comands != null;
    }

    public void setJoinMessage(final String join_message) {
        this.join_message = join_message;
    }
    public String getJoinMessage() {
        return this.join_message;
    }


    public void sendJoinSound(final Player p) {
        if (this.join_sound.equalsIgnoreCase("random")) {
            final Random r = new Random();
            final Sound[] so = Sound.values();
            final int v = so.length;
            final int rt = r.nextInt(v) + 1;
            p.playSound(p.getLocation(), so[rt], 1.0f, 1.0f);
        }
        else {
            p.playSound(p.getLocation(), Sound.valueOf(this.join_sound.toUpperCase()), 1.0f, 1.0f);
        }
    }


    public void setJoinSound(final String sound) {
        this.join_sound = sound;
    }
}
