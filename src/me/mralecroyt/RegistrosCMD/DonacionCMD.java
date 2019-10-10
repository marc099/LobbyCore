package me.mralecroyt.RegistrosCMD;

import me.mralecroyt.Listener.Utils.Color;
import me.mralecroyt.LobbyCore.*;
import me.mralecroyt.administrador.ConfigAdmin;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;

public class DonacionCMD implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final FileConfiguration c = ConfigAdmin.getConfigConfig();
        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                for (final String i : CoreMain.getInstance().getConfig().getStringList("Donaciones.Uso")) {
                    sender.sendMessage(Color.getColor(i));
                }
            } else if (args.length == 1) {
                final Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    for (final String j : CoreMain.getInstance().getConfig().getStringList("Donaciones.MensajeDonacion")) {
                        Bukkit.broadcastMessage(Color.getColor(j).replace("%player%", target.getName()));
                    }
                } else if (target == null) {
                    for (final String j : CoreMain.getInstance().getConfig().getStringList("Donaciones.Uso")) {
                        sender.sendMessage(Color.getColor(j));
                    }
                }
            } else {
                for (final String i : CoreMain.getInstance().getConfig().getStringList("Donaciones.Uso")) {
                    sender.sendMessage(Color.getColor(i));
                }
            }
        } else if (sender instanceof Player) {
            final Player p = (Player) sender;
            if (p.hasPermission(c.getString("DonacionPerm"))) {
                if (args.length == 0) {
                    for (final String j : CoreMain.getInstance().getConfig().getStringList("Donaciones.Uso")) {
                        p.sendMessage(Color.getColor(j));
                    }
                } else if (args.length == 1) {
                    final Player target2 = Bukkit.getPlayer(args[0]);
                    if (target2 != null) {
                        for (final String k : CoreMain.getInstance().getConfig().getStringList("Donaciones.MensajeDonacion")) {
                            Bukkit.broadcastMessage(Color.getColor(k).replace("%player%", target2.getName()));
                        }
                    } else if (target2 == null) {
                        for (final String k : CoreMain.getInstance().getConfig().getStringList("Donaciones.Uso")) {
                            p.sendMessage(Color.getColor(k));
                        }
                    }
                } else {
                    for (final String j : CoreMain.getInstance().getConfig().getStringList("Donaciones.Uso")) {
                        p.sendMessage(Color.getColor(j));
                    }
                }
            } else {
                p.sendMessage(Color.getColor(CoreMain.getInstance().getConfig().getString("Donaciones.No-Permisisos")));
            }
        }
        return false;
    }
}
