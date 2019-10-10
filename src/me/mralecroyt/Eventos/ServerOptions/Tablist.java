package me.mralecroyt.Eventos.ServerOptions;

import me.mralecroyt.Listener.Utils.TablistAPI;
import me.mralecroyt.LobbyCore.CoreMain;
import java.io.*;

import me.mralecroyt.administrador.ConfigAdmin;
import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

public class Tablist implements Listener
{
    private CoreMain plugin;
    public static FileConfiguration storage;
    public static File storageFile;

    public Tablist(final CoreMain plugin) {
        this.plugin = plugin;
    }

    public Tablist(final Player player, final String header, final String footer) {
    }

    @EventHandler
    public void onTablist(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final FileConfiguration c = ConfigAdmin.getConfigConfig();
        if (c.getBoolean("Tablist.Enable")) {
            this.plugin.updateTab(p);
            final String servername = c.getString("ServerName");
            final String ServerIP = c.getString("ServerIP");
            final int online1 = this.plugin.getServer().getOnlinePlayers().size();
            final String online2 = Integer.toString(online1);
            final int max = this.plugin.getServer().getMaxPlayers();
            final String max2 = Integer.toString(max);
            TablistAPI.sendPacket(e.getPlayer(), p);
        }
    }


    public static String tk(final String a) {
        return a.replaceAll("&", "§");
    }

    public static void onTablist() {
    }

    static {
        Tablist.storage = null;
        Tablist.storageFile = null;
    }
}
