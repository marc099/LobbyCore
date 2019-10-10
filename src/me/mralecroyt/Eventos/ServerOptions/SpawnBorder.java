package me.mralecroyt.Eventos.ServerOptions;

import me.mralecroyt.administrador.ConfigAdmin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.*;
import me.mralecroyt.LobbyCore.CoreMain;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class SpawnBorder implements Listener
{
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("lsp.ignore")) {
            final Location playerloc = player.getLocation();
            final FileConfiguration c = ConfigAdmin.getConfigConfig();
            final World world = Bukkit.getWorld(c.getString("SpawnBorder.world"));
            final double x = c.getDouble("SpawnBorder.x");
            final double y = c.getDouble("SpawnBorder.y");
            final double z = c.getDouble("SpawnBorder.z");
            final float yaw = (float)c.getDouble("SpawnBorder.yaw");
            final float pitch = (float)c.getDouble("SpawnBorder.pitch");
            final double maxY = c.getDouble("SpawnBorder.maxY");
            final double minY = c.getDouble("SpawnBorder.minY");

            final Location spawnplayer = player.getLocation();
            final World world1 = Bukkit.getServer().getWorld(WorldSpawn.storage.getString("Spawn.world"));
            final float yaw1 = (float)WorldSpawn.storage.getDouble("Spawn.yaw");
            final float pitch1 = (float)WorldSpawn.storage.getDouble("Spawn.pitch");
            final double x1 = WorldSpawn.storage.getDouble("Spawn.x");
            final double y1 = WorldSpawn.storage.getDouble("Spawn.y");
            final double z1 = WorldSpawn.storage.getDouble("Spawn.z");
            final Location spawn = new Location(world1, x1, y1, z1);
            spawn.setPitch(pitch1);
            spawn.setYaw(yaw1);

            if (player.getWorld().equals(world)) {
                final int radius = c.getInt("SpawnBorder.radius");
                if (playerloc.getX() > x + radius) {
                    player.teleport(spawn);
                }
                else if (playerloc.getX() < x - radius) {
                    player.teleport(spawn);
                }
                else if (playerloc.getZ() > z + radius) {
                    player.teleport(spawn);
                }
                else if (playerloc.getZ() < z - radius) {
                    player.teleport(spawn);
                }
                if (playerloc.getY() > maxY) {
                    player.teleport(spawn);
                }
                if (playerloc.getY() < minY) {
                    player.teleport(spawn);
                }
            }
        }
    }
}
