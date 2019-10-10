package me.mralecroyt.Eventos.ServerOptions;

import me.mralecroyt.LobbyCore.*;
import me.mralecroyt.administrador.ConfigAdmin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.*;
import me.mralecroyt.Listener.Utils.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class TitleOnJoin implements Listener
{
    public static CoreMain plugin;

    public TitleOnJoin() {
        this.plugin = plugin;

    }

    @EventHandler
    public void onJoinTitle(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final FileConfiguration c = ConfigAdmin.getConfigConfig();
        if (c.getBoolean("EnableJoinTitle")) {
            final String title = c.getString("JoinTitle").replace("%player%", e.getPlayer().getDisplayName()).replace("%player%", p.getName());
            final String subtitle = c.getString("SubTitle").replace("%player%", p.getName());
            TitleAPI.sendTitle(p, CoreMain.setPlaceholders(p, title).replace("&", "§").replace("&", "§"));
            TitleAPI.sendSubTitle(p, CoreMain.setPlaceholders(p, subtitle));
        }
    }
}
