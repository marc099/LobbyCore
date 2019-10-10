package me.mralecroyt.LobbyCore;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.mralecroyt.Eventos.CustomJoinItems.ItemListener;
import me.mralecroyt.Eventos.CustomJoinItems.joinitems;
import me.mralecroyt.Eventos.ScoreBoard.BoardTask;
import me.mralecroyt.Eventos.ServerOptions.*;
import me.mralecroyt.Listener.Utils.CustomJoins;
import me.mralecroyt.Listener.Utils.TablistAPI;
import me.mralecroyt.RegistrosCMD.DonacionCMD;
import me.mralecroyt.RegistrosCMD.MemoriaCMD;
import me.mralecroyt.administrador.ConfigAdmin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;



public class CoreMain extends JavaPlugin implements Listener {
    public static CoreMain instance;
    public static String TpP;
    public static String PREFIX;
    PluginManager pm;
    public List<CustomJoins> customjoins;
    public static Permission perms;
    public Chat chat;
    public Economy econ;
    public static ConfigAdmin cm;
    PluginDescriptionFile pdffile;
    public String version;
    public String nombre;
    public final Boolean papi;
    private final Boolean mvdw;


    public void onEnable() {
        final ConsoleCommandSender cs = this.getServer().getConsoleSender();
        cs.sendMessage("#######################################");
        cs.sendMessage("[LobbyCore] Cargando configs....");
        this.loadConfigs();
        cs.sendMessage("Cargando items....");
        this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new joinitems(this), this);
        joinitems.JoinItems(this);
        joinitems.load();
        cs.sendMessage("Cargando eventos....");
        this.setupPermissions();
        this.getServer().getPluginManager().registerEvents(new JumpPads(this), this);
        this.getServer().getPluginManager().registerEvents(new Tablist(this), this);
        this.loadEventos();
        loadCustomJoins();
        this.deletefiles2();
        this.getServer().getPluginManager().registerEvents(new WorldSpawn(this), this);
        this.getServer().getPluginManager().registerEvents(new Customjoinmessages(this), this);
        cs.sendMessage("Cargando comandos....");
        this.loadComandos();
        WorldSpawn.SpawnManager(this);
        this.getCommand("setspawn").setExecutor(new WorldSpawn(this));
        this.TabUpdater();
        cs.sendMessage("Cargando scoreboard....");
        BoardTask.load(this);
        new BukkitRunnable() {
            public void run() {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    BoardTask.contentBoard(p);
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, (long)(20 * BoardTask.storage.getInt("Time-Update")));
        for (final World world : Bukkit.getWorlds()) {
            world.setStorm(false);
            world.setThundering(false);
            this.getLogger().log(Level.INFO, "Version: {0} Activado", getInstance().getDescription().getVersion());
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                Bukkit.getServer().getLogger().log(Level.INFO, "Enlazado con Vault -> version: {0}", Bukkit.getPluginManager().getPlugin("Vault").getDescription().getVersion());
            } else {
                Bukkit.getServer().getLogger().log(Level.INFO, "Este plugin requiere Vault");
                Bukkit.getServer().getLogger().log(Level.INFO, "Vault Download: https://dev.bukkit.org/projects/vault");
            }
            if (this.papi) {
                if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
                    Bukkit.getServer().getLogger().log(Level.INFO, "Enlazado con PlaceholderAPI -> version: {0}", PlaceholderAPIPlugin.getInstance().getDescription().getVersion());
                } else {
                    Bukkit.getServer().getLogger().log(Level.INFO, "PlaceholderAPI Download: https://www.spigotmc.org/resources/placeholderapi.6245/");
                }
            } else {
                Bukkit.getServer().getLogger().log(Level.INFO, "PlaceholderAPI desactivado en config.yml");
            }
            if (this.mvdw) {
                if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
                    Bukkit.getServer().getLogger().log(Level.INFO, "Enlazado con MVdWPlaceholderAPI -> version {0}", Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI").getDescription().getVersion());
                }
            } else {
                Bukkit.getServer().getLogger().log(Level.INFO, "MVdWPlaceholderAPI desactivado en config.yml");
            }
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
            this.reloadConfig();
        }
        cs.sendMessage("Cargando plugin....");
        cs.sendMessage("Plugin completado " + this.getDescription().getVersion());
        cs.sendMessage("[LobbyCore] Plugin by MrAlecroYT");
        cs.sendMessage("#######################################");
    }


    public static String setPlaceholders(Player p, String s) {
        s = PlaceholderAPI.setPlaceholders(p, s);
        s = s.replace("%player%", p.getName());
        s = s.replace("%player-displayname%", p.getDisplayName());
        return s;
    }

    public void loadCustomJoins() {
        final FileConfiguration c = ConfigAdmin.getConfigJoins();
        this.customjoins = new ArrayList<CustomJoins>();
        final Set<String> key = c.getKeys(false);
        for (final String nodo : key) {
            final ConfigurationSection a = c.getConfigurationSection(nodo);
            if (!a.isSet("permission")) {
            }
            final CustomJoins cj = new CustomJoins (a.getString("permission"), this);
            if (a.isSet("join-message")) {
                cj.setJoinMessage(a.getString("join-message"));
            }
            this.customjoins.add(cj);
        }
    }

    public void onDisable() {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
        }
    }

    private void deleteFiles(final File directory) {
        if (directory.exists()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File file : files) {
                    file.delete();
                }
            }
        }
    }

    private void deleteFile(final File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public void deletefiles2() {
        this.deleteFile(new File("Lobby/uid.dat"));
        this.deleteFile(new File("Lobby/level.dat_old"));
        this.deleteFile(new File("Lobby/data/scoreboard.dat"));
    }

    private void loadEventos() {
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new Worldguard(), this);
        pm.registerEvents(new Clima(), this);
        pm.registerEvents(new NoMobs(), this);
        pm.registerEvents(new VIPJOIN(), this);
        pm.registerEvents(new SpawnBorder(), this);
        pm.registerEvents(new joinmessage(), this);
        pm.registerEvents(new Voidspawn(), this);
        pm.registerEvents(new TitleOnJoin(), this);
    }

    private void loadComandos() {
        this.getCommand("memoriaa").setExecutor(new MemoriaCMD()); // es para que veas
        this.getCommand("donacion").setExecutor(new DonacionCMD());
    }

    public static CoreMain getInstance() {
        return CoreMain.instance;
    }

    private void loadConfigs() {
        (cm = new ConfigAdmin()).createConfigConfig();
        (cm = new ConfigAdmin()).createConfigJoins();
    }


    public CoreMain() {

        CoreMain.instance = this;
        this.papi = this.getConfig().getBoolean("PlaceholderAPI");
        this.mvdw = this.getConfig().getBoolean("MVdWPlaceholderAPI");
        this.chat = null;
        this.customjoins = new ArrayList<CustomJoins>();
        this.econ = null;
    }

    public static CoreMain get() {
        return getInstance();
    }

    public void updateTab(final Player p) {
        final FileConfiguration c = ConfigAdmin.getConfigConfig();
        final String headline = setPlaceholders(p, c.getString("Tablist.header").replace("&", "§").replace("%player%", p.getName()));
        final String footer = setPlaceholders(p, c.getString("Tablist.footer").replace("&", "§").replace("%player%", p.getName()));
        TablistAPI.TablistAPI(p, headline, footer);
    }

    public void TabUpdater() {
        if (getInstance().getConfig().getBoolean("Tablist.Enable")) {
            final FileConfiguration c = ConfigAdmin.getConfigConfig();
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getOnlinePlayers().forEach(pl -> CoreMain.this.updateTab(pl));
                }
            }.runTaskTimerAsynchronously(get(), (long)(c.getInt("Online.Time-Update") * 20), (long)(c.getInt("Online.Time-Update") * 20));
        }
    }

    public boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> rsp = (RegisteredServiceProvider<Permission>)this.getServer().getServicesManager().getRegistration((Class)Permission.class);
        CoreMain.perms = rsp.getProvider();
        return CoreMain.perms != null;
    }


    static {
        CoreMain.PREFIX = "§a§lLobby§2§lCore §8» §7";
    }


    private String fixColors(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String chatColors(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message.replace("\\\\n", "\n").replace("\\n", "\n").replace("&nl", "\n"));
    }
}
