package me.mralecroyt.Eventos.ServerOptions;

import me.mralecroyt.Eventos.ScoreBoard.BoardTask;
import me.mralecroyt.Listener.Utils.CustomJoins;
import me.mralecroyt.LobbyCore.CoreMain;
import me.mralecroyt.administrador.ConfigAdmin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class Customjoinmessages implements Listener {
    public static FileConfiguration storage;
    private static File storageFile;
    private final CoreMain plugin;

    public Customjoinmessages(final CoreMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCustomJoin(final PlayerJoinEvent e) {
        final FileConfiguration c = ConfigAdmin.getConfigConfig();
        if (c.getBoolean("CustomJoins")) {
            final Player p = e.getPlayer();
            for (final CustomJoins cj : this.plugin.customjoins) {
                if (e.getPlayer().hasPermission(cj.getPermission())) {
                    if (cj.hasJoinMessage()) {
                        if (cj.getJoinMessage().equalsIgnoreCase("none")) {
                            e.setJoinMessage(null);
                        } else {
                            e.setJoinMessage(cj.getJoinMessage().replace("%player%", p.getName()).replace('&', '§'));
                        }
                    }
                    if (cj.hasJoinSound()) {
                        cj.sendJoinSound(e.getPlayer());
                    }
                    break;
                }
            }
        }
    }
}
