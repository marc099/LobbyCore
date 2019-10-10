package me.mralecroyt.Eventos.ServerOptions;

import me.mralecroyt.LobbyCore.CoreMain;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JumpPads implements Listener
{
    private CoreMain plugin;
    public static List<UUID> jumpers;

    static {
        JumpPads.jumpers = new ArrayList<UUID>();
    }

    public JumpPads(final CoreMain main) {
        this.plugin = main;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (p.getLocation().getBlock().getType() == Material.STONE_PLATE && p.getLocation().subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.COBBLESTONE) {
            final Vector v = p.getLocation().getDirection().multiply(3.0).setY(1.0);
            p.setVelocity(v);
            JumpPads.jumpers.add(p.getUniqueId());
            JumpPads.jumpers.contains(p.getUniqueId());
        }
    }
}
