package me.mralecroyt.Eventos.ServerOptions;

import me.mralecroyt.administrador.ConfigAdmin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinmessage implements Listener {
    @EventHandler
    public void onjoin(final PlayerJoinEvent e) {
        final FileConfiguration c = ConfigAdmin.getConfigConfig();
        final Player p = e.getPlayer();
        for (final String string : c.getStringList("JoinMessage")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', string).replace("%player%", p.getName()));
        }
    }
}



