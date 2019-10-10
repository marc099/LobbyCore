package me.mralecroyt.Listener.Utils;


import org.bukkit.entity.*;
import org.bukkit.*;
import me.mralecroyt.LobbyCore.*;
import org.bukkit.plugin.*;
import java.io.*;
import java.util.logging.*;
import org.bukkit.plugin.messaging.*;
import org.bukkit.entity.*;
import org.bukkit.*;


public class Utils
{
    public static String color(final String input) {
        if (input == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String addDefaultColor(String input) {
        if (input == null) {
            return null;
        }
        if (!input.startsWith("§")) {
            input = "§f" + input;
        }
        return input;
    }

    public static boolean connectToBungeeServer(final Player player, final String server) {
        try {
            final Messenger messenger = Bukkit.getMessenger();
            if (!messenger.isOutgoingChannelRegistered((Plugin)CoreMain.getInstance(), "BungeeCord")) {
                messenger.registerOutgoingPluginChannel((Plugin)CoreMain.getInstance(), "BungeeCord");
            }
            if (server.length() == 0) {
                player.sendMessage("§cTarget server was \"\" (empty string) cannot connect to it.");
                return false;
            }
            final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(byteArray);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage((Plugin)CoreMain.getInstance(), "BungeeCord", byteArray.toByteArray());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            player.sendMessage("§cAn exception has occurred. Please notify OPs about this. (They should look at the console).");
            CoreMain.getInstance().getLogger().log(Level.WARNING, "Could not handle BungeeCord command from {0}: tried to connect to \"{1}\".", new Object[] { player.getName(), server });
            return false;
        }
        return true;
    }
}
