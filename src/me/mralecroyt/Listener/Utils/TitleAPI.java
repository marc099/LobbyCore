package me.mralecroyt.Listener.Utils;


import org.bukkit.entity.*;
import java.lang.reflect.*;
import org.bukkit.*;

public class TitleAPI
{
    public static void sendTitle(final Player player, String title) {
        try {
            title = ChatColor.translateAlternateColorCodes('&', title);
            final Object packetTitle = getMcClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            final Object objectTitle = getMcClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
            final Constructor<?> cTitle = getMcClass("PacketPlayOutTitle").getConstructor(getMcClass("PacketPlayOutTitle").getDeclaredClasses()[0], getMcClass("IChatBaseComponent"));
            sendPacket(player, cTitle.newInstance(packetTitle, objectTitle));
        }
        catch (IllegalAccessException ex) {}
        catch (IllegalArgumentException ex2) {}
        catch (InstantiationException ex3) {}
        catch (NoSuchFieldException ex4) {}
        catch (NoSuchMethodException ex5) {}
        catch (SecurityException ex6) {}
        catch (InvocationTargetException ex7) {}
    }

    public static void sendSubTitle(final Player player, String subtitle) {
        try {
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            final Object packetTitle = getMcClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            final Object objectTitle = getMcClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
            final Constructor<?> cTitle = getMcClass("PacketPlayOutTitle").getConstructor(getMcClass("PacketPlayOutTitle").getDeclaredClasses()[0], getMcClass("IChatBaseComponent"));
            sendPacket(player, cTitle.newInstance(packetTitle, objectTitle));
        }
        catch (IllegalAccessException ex) {}
        catch (IllegalArgumentException ex2) {}
        catch (InstantiationException ex3) {}
        catch (NoSuchFieldException ex4) {}
        catch (NoSuchMethodException ex5) {}
        catch (SecurityException ex6) {}
        catch (InvocationTargetException ex7) {}
    }

    public static void clearTitle(final Player player) {
        sendTitle(player, "");
        sendSubTitle(player, "");
    }

    private static Class<?> getMcClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private static void sendPacket(final Player player, final Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getMcClass("Packet")).invoke(playerConnection, packet);
        }
        catch (IllegalAccessException ex) {}
        catch (IllegalArgumentException ex2) {}
        catch (NoSuchFieldException ex3) {}
        catch (NoSuchMethodException ex4) {}
        catch (SecurityException ex5) {}
        catch (InvocationTargetException ex6) {}
    }
}

