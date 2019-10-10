package me.mralecroyt.Eventos.CustomJoinItems;

import org.bukkit.event.*;
import java.io.*;
import me.mralecroyt.LobbyCore.*;
import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;
import java.util.logging.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.configuration.*;

public class joinitems implements Listener
{
    public static FileConfiguration storage;
    public static File storageFile;
    public static List<String> HidePlayer;
    private static String pn;
    public static List<JoinItem> items;
    private final CoreMain plugin;

    public joinitems(final CoreMain plugin) {
        this.plugin = plugin;
    }


    public static void JoinItems(final Plugin plugin) {
        joinitems.storageFile = new File(plugin.getDataFolder(), "customjoinitems.yml");
        if (!joinitems.storageFile.exists()) {
            plugin.saveResource("customjoinitems.yml", false);
            CoreMain.getInstance().getLogger().info("Creando Customjoinitems.yml");
        }
        else {
            CoreMain.getInstance().getLogger().info("Cargando Customjoinitems.yml");
        }
        joinitems.storage = YamlConfiguration.loadConfiguration(joinitems.storageFile);
    }

    public static void load() {
        joinitems.items = new ArrayList<JoinItem>();
        final FileConfiguration itemsConfig = loadFile("customjoinitems.yml");
        final Set<String> cs = itemsConfig.getKeys(false);
        for (final String item : cs) {
            final ConfigurationSection itemNode = itemsConfig.getConfigurationSection(item);
            if (!itemNode.isSet("Name")) {
                CoreMain.getInstance().getLogger().log(Level.INFO, "The item \"{0}\" has no name!", item);
            }
            else if (!itemNode.isSet("ID")) {
                CoreMain.getInstance().getLogger().log(Level.INFO, "The item \"{0}\" has no ID!", item);
            }
            else if (itemNode.getInt("ID") == 0 || Material.getMaterial(itemNode.getInt("id")) == null) {
                CoreMain.getInstance().getLogger().log(Level.INFO, "\" has an invalid item ID: The item \"{0}{1}.", new Object[] { item, itemNode.getInt("ID") });
            }
            else {
                final Material material = Material.getMaterial(itemNode.getInt("ID"));
                final String command = itemNode.getString("Command");
                final String name = itemNode.getString("Name");
                final Integer slot = itemNode.getInt("Slot");
                final Short dataValue = (short)itemNode.getInt("Data-value");
                final JoinItem objetos = new JoinItem(material);
                objetos.setCommands(ItemCommand.arrayFromString(command));
                objetos.setSlot(slot);
                objetos.setCustomName(name);
                objetos.setDataValue(dataValue);
                if (itemNode.isSet("Lore") && itemNode.isList("Lore")) {
                    objetos.setLore(itemNode.getStringList("Lore"));
                }
                objetos.setBlockMovement(itemNode.getBoolean("Deny-move", false));
                objetos.setDroppable(itemNode.getBoolean("Allow-drop", false));
                if (itemNode.getInt("cooldown-seconds") > 0) {
                    objetos.setUseCooldown(true);
                    objetos.setCooldownSeconds(itemNode.getInt("cooldown-seconds"));
                }
                joinitems.items.add(objetos);
            }
        }
    }

    public static FileConfiguration loadFile(String path) {
        if (!path.endsWith(".yml")) {
            path = path + ".yml";
        }
        final File file = new File(CoreMain.getInstance().getDataFolder(), path);
        if (!file.exists()) {
            try {
                CoreMain.getInstance().saveResource(path, false);
            }
            catch (Exception e) {
                CoreMain.getInstance().getLogger().log(Level.WARNING, "Cannot save {0} to disk!", path);
                return null;
            }
        }
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    static {
        joinitems.storage = null;
        joinitems.storageFile = null;
        joinitems.HidePlayer = new ArrayList<String>();
        joinitems.items = new ArrayList<JoinItem>();
    }
}

