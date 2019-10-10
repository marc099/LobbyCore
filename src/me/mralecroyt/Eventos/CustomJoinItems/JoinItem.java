package me.mralecroyt.Eventos.CustomJoinItems;

import java.util.*;

import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import me.mralecroyt.Listener.Utils.*;
import me.mralecroyt.LobbyCore.*;
import org.bukkit.plugin.*;

public class JoinItem
{
    private final Material material;
    private ItemCommand[] commands;
    private Short dataValue;
    private String customName;
    private String permission;
    private Integer slot;
    private boolean onlyOnFirstJoin;
    private boolean blockMovement;
    private boolean droppable;
    private boolean giveOnRespawn;
    private List<String> lore;
    private int cooldownSeconds;
    private boolean useCooldown;
    private final List<String> playersInCooldown;
    private boolean giveOnWorldChange;
    private List<String> removeInWorlds;

    public JoinItem(final Material mat) {
        this.dataValue = null;
        this.customName = null;
        this.permission = null;
        this.slot = null;
        this.onlyOnFirstJoin = false;
        this.blockMovement = false;
        this.droppable = false;
        this.giveOnRespawn = true;
        this.cooldownSeconds = 0;
        this.useCooldown = false;
        this.playersInCooldown = new ArrayList<String>();
        this.removeInWorlds = new ArrayList<String>();
        this.material = mat;
    }


    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(final String customName) {
        if (customName == null || customName.length() == 0) {
            this.customName = null;
            return;
        }
        this.customName = Utils.addDefaultColor(Utils.color(customName));
    }

    public void setLore(final List<String> lore) {
        if (lore == null || lore.isEmpty()) {
            this.lore = null;
            return;
        }
        this.lore = new ArrayList<String>();
        for (String s : lore) {
            s = Utils.color(s);
            s = Utils.addDefaultColor(s);
            this.lore.add(s);
        }
    }

    public void setSlot(Integer slot) {
        if (slot == null || slot == 0) {
            this.slot = null;
            return;
        }
        if (slot < 1) {
            slot = 1;
        }
        if (slot > 9) {
            slot = 9;
        }
        --slot;
        this.slot = slot;
    }

    public void setPerm(final String permission) {
        if (permission == null || permission.length() == 0) {
            this.permission = null;
        }
        else {
            this.permission = permission;
        }
    }

    public boolean isSimilar(final ItemStack item) {
        if (item == null) {
            return false;
        }
        if (item.getType() != this.material) {
            return false;
        }
        final ItemMeta meta = item.getItemMeta();
        if (this.customName == null) {
            if (meta.hasDisplayName()) {
                return false;
            }
        }
        else {
            if (!meta.hasDisplayName()) {
                return false;
            }
            if (!meta.getDisplayName().equals(this.customName)) {
                return false;
            }
        }
        return this.dataValue == null || this.dataValue == item.getDurability();
    }

    public void removeFrom(final Player player) {
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length; ++i) {
            if (this.isSimilar(contents[i])) {
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }
        if (this.isSimilar(inv.getHelmet())) {
            inv.setHelmet(new ItemStack(Material.AIR));
        }
        if (this.isSimilar(inv.getChestplate())) {
            inv.setChestplate(new ItemStack(Material.AIR));
        }
        if (this.isSimilar(inv.getLeggings())) {
            inv.setLeggings(new ItemStack(Material.AIR));
        }
        if (this.isSimilar(inv.getBoots())) {
            inv.setBoots(new ItemStack(Material.AIR));
        }
    }

    public void giveTo(final Player player, final boolean notifyFailure) {
        final PlayerInventory inv = player.getInventory();
        if (inv.firstEmpty() == -1) {
            if (notifyFailure) {
                player.sendMessage("§cYour inventory is full.");
            }
            return;
        }
        ItemStack[] contents;
        for (int length = (contents = inv.getContents()).length, i = 0; i < length; ++i) {
            final ItemStack itemStack = contents[i];
            if (this.isSimilar(itemStack)) {
                if (notifyFailure) {
                    player.sendMessage("§cYour already own this item.");
                }
                return;
            }
        }
        ItemStack[] armorContents;
        for (int length2 = (armorContents = inv.getArmorContents()).length, j = 0; j < length2; ++j) {
            final ItemStack itemStack2 = armorContents[j];
            if (this.isSimilar(itemStack2)) {
                if (notifyFailure) {
                    player.sendMessage("§cYour already own this item.");
                }
                return;
            }
        }
        final ItemStack item = new ItemStack(this.material);
        if (this.dataValue != null) {
            item.setDurability(this.dataValue);
        }
        final ItemMeta meta = item.getItemMeta();
        if (this.customName != null) {
            meta.setDisplayName(this.customName);
        }
        if (this.lore != null) {
            meta.setLore(this.lore);
        }
        item.setItemMeta(meta);
        if (this.slot != null) {
            final ItemStack previous = inv.getItem(this.slot);
            inv.setItem(this.slot, item);
            if (previous != null) {
                inv.addItem(previous);
            }
        }
        else {
            inv.addItem(item);
        }
    }

    public void executeCommands(final Player player) {
        if (this.commands != null && this.commands.length > 0) {
            if (this.useCooldown) {
                if (this.playersInCooldown.contains(player.getName().toLowerCase())) {
                    player.sendMessage("§cTienes que esperar antes de hacer esto.");
                    return;
                }
                this.addCooldown(player);
            }
            ItemCommand[] comandos;
            for (int length = (comandos = this.commands).length, i = 0; i < length; ++i) {
                final ItemCommand itemCommand = comandos[i];
                itemCommand.execute(player);
            }
        }
    }

    public boolean hasPerm(final Player player) {
        return this.permission == null || player.hasPermission(this.permission);
    }

    public boolean isOnlyOnFirstJoin() {
        return this.onlyOnFirstJoin;
    }

    public void setOnlyOnFirstJoin(final boolean onlyOnFirstJoin) {
        this.onlyOnFirstJoin = onlyOnFirstJoin;
        if (onlyOnFirstJoin && this.giveOnRespawn) {
            this.giveOnRespawn = false;
        }
    }

    public void setDataValue(final Short dataValue) {
        if (dataValue == null || dataValue == 0) {
            this.dataValue = null;
            return;
        }
        this.dataValue = dataValue;
    }

    public boolean isDroppable() {
        return this.droppable;
    }

    public void setDroppable(final boolean droppable) {
        this.droppable = droppable;
    }

    public boolean isGiveOnRespawn() {
        return this.giveOnRespawn;
    }

    public void setGiveOnRespawn(final boolean giveOnRespawn) {
        if (this.onlyOnFirstJoin) {
            return;
        }
        this.giveOnRespawn = giveOnRespawn;
        this.isGiveOnRespawn();
    }

    public void setCommands(final ItemCommand[] commands) {
        this.commands = commands;
    }

    public void setBlockMovement(final boolean blockMovement) {
        this.blockMovement = blockMovement;
    }

    public boolean isMovementBlocked() {
        return this.blockMovement;
    }

    public void setUseCooldown(final boolean use) {
        this.useCooldown = use;
    }

    public void setCooldownSeconds(final int seconds) {
        this.cooldownSeconds = seconds;
    }

    public boolean usesCooldown() {
        return this.useCooldown;
    }

    public void setGiveOnWorldChange(final boolean b) {
        this.giveOnWorldChange = b;
    }

    public boolean isGiveOnWorldChange() {
        return this.giveOnWorldChange;
    }

    public boolean isAllowedInWorld(final String s) {
        return !this.removeInWorlds.contains(s);
    }

    public void setDisabledWorlds(final List<String> list) {
        if (list != null) {
            this.removeInWorlds = list;
        }
    }

    public void addCooldown(final Player player) {
        final String name = player.getName().toLowerCase();
        if (this.playersInCooldown.contains(name)) {
            return;
        }
        this.playersInCooldown.add(name);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                JoinItem.this.playersInCooldown.remove(name);
            }
        }, (long)(this.cooldownSeconds * 20));
    }
}
