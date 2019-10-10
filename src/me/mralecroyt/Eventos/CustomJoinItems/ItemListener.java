package me.mralecroyt.Eventos.CustomJoinItems;

import me.mralecroyt.LobbyCore.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.event.inventory.*;

public class ItemListener implements Listener
{
    public static boolean clearInventoryOnJoin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String world = player.getWorld().getName();
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items")) {
            if (ItemListener.clearInventoryOnJoin) {
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
            }
            for (final JoinItem item : joinitems.items) {
                if (item.hasPerm(player) && item.isAllowedInWorld(world)) {
                    if (item.isOnlyOnFirstJoin() && player.hasPlayedBefore()) {
                        continue;
                    }
                    item.giveTo(player, false);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items")) {
            final Iterator<ItemStack> iter = event.getDrops().iterator();
            while (iter.hasNext()) {
                final ItemStack next = iter.next();
                for (final JoinItem item : joinitems.items) {
                    if (item.isSimilar(next)) {
                        iter.remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        return;
                    }
                    final String world = player.getWorld().getName();
                    for (final JoinItem item : joinitems.items) {
                        if (item.hasPerm(player) && item.isGiveOnRespawn() && item.isAllowedInWorld(world)) {
                            item.giveTo(player, false);
                        }
                    }
                }
            }, 1L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(final PlayerDropItemEvent event) {
        final ItemStack drop = event.getItemDrop().getItemStack();
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items")) {
            for (final JoinItem item : joinitems.items) {
                if (item.isSimilar(drop) && !item.isDroppable()) {
                    event.setCancelled(true);
                    final Player player = event.getPlayer();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            player.updateInventory();
                        }
                    }, 1L);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items") && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack itemInHand = event.getItem();
            for (final JoinItem item : joinitems.items) {
                if (item.isSimilar(itemInHand)) {
                    event.setCancelled(true);
                    item.executeCommands(event.getPlayer());
                    event.getPlayer().updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void changeWorld(final PlayerChangedWorldEvent event) {
        final Player whoSwitched = event.getPlayer();
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (!whoSwitched.isOnline()) {
                        return;
                    }
                    final String world = whoSwitched.getWorld().getName();
                    for (final JoinItem item : joinitems.items) {
                        if (!item.isAllowedInWorld(world)) {
                            item.removeFrom(whoSwitched);
                        }
                        else {
                            if (!item.isGiveOnWorldChange()) {
                                continue;
                            }
                            if (!item.hasPerm(whoSwitched)) {
                                continue;
                            }
                            item.giveTo(whoSwitched, false);
                        }
                    }
                }
            }, 1L);
        }
    }

    @EventHandler
    public void inventoryClick(final InventoryClickEvent event) {
        if (CoreMain.getInstance().getConfig().getBoolean("Join-Items")) {
            final ItemStack item = event.getCurrentItem();
            if (item == null) {
                return;
            }
            for (final JoinItem JoinItem : joinitems.items) {
                if (JoinItem.isSimilar(item) && JoinItem.isMovementBlocked()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    static {
        ItemListener.clearInventoryOnJoin = true;
    }
}

