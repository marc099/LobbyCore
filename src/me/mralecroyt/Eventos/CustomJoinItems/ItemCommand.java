package me.mralecroyt.Eventos.CustomJoinItems;

import me.mralecroyt.Listener.Utils.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.command.*;
import me.mralecroyt.LobbyCore.*;
import java.util.logging.*;

public class ItemCommand
{
    private String command;
    private final Type type;

    private ItemCommand(final String command, final Type type) {
        this.command = command;
        this.type = type;
    }

    public String getCommand() {
        return this.command;
    }

    public static ItemCommand[] arrayFromString(final String input) {
        if (input == null || input.length() == 0) {
            return new ItemCommand[] { new ItemCommand("", Type.DEFAULT) };
        }
        final String[] commandStrings = input.split(";");
        final ItemCommand[] commands = new ItemCommand[commandStrings.length];
        for (int i = 0; i < commandStrings.length; ++i) {
            commands[i] = fromString(commandStrings[i].trim());
        }
        return commands;
    }

    public static ItemCommand fromString(String input) {
        if (input == null || input.length() == 0) {
            return new ItemCommand("", Type.DEFAULT);
        }
        input = input.trim();
        Type type = Type.DEFAULT;
        if (input.startsWith("console:")) {
            input = input.substring(8);
            type = Type.CONSOLE;
        }
        if (input.startsWith("op:")) {
            input = input.substring(3);
            type = Type.OP;
        }
        if (input.startsWith("server:")) {
            input = input.substring(7);
            type = Type.SERVER;
        }
        if (input.startsWith("tell:")) {
            input = input.substring(5);
            type = Type.TELL;
        }
        input = input.trim();
        input = Utils.color(input);
        return new ItemCommand(input, type);
    }

    public void execute(final Player player) {
        if (this.command == null || this.command.length() == 0) {
            return;
        }
        if (this.command.contains("%player%")) {
            this.command = this.command.replace("%player%", player.getName());
        }
        if (this.command.contains("%world%")) {
            this.command = this.command.replace("%world%", player.getWorld().getName());
        }
        switch (this.type) {
            case CONSOLE: {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
                break;
            }
            case OP: {
                final boolean isOp = player.isOp();
                player.setOp(true);
                try {
                    player.chat("/" + this.command);
                }
                catch (Exception ex) {}
                try {
                    player.setOp(isOp);
                }
                catch (Exception danger) {
                    player.setOp(false);
                    CoreMain.getInstance().getLogger().log(Level.SEVERE, "", player.getName());
                }
                break;
            }
            case SERVER: {
                Utils.connectToBungeeServer(player, this.command);
                break;
            }
            case TELL: {
                player.sendMessage(this.command);
                break;
            }
            default: {
                player.chat("/" + this.command);
                break;
            }
        }
    }

    public enum Type
    {
        DEFAULT("DEFAULT", 0),
        CONSOLE("CONSOLE", 1),
        OP("OP", 2),
        SERVER("SERVER", 3),
        TELL("TELL", 4);

        Type(final String s, final int n) {
        }
    }
}
