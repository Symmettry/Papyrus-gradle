package me.symmettry.papyrus.script.members;

import com.ibm.icu.impl.locale.XCldrStub;
import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.commands.ScriptedCommand;
import me.symmettry.papyrus.script.ScriptObj;
import me.symmettry.papyrus.util.commands.CommandConsumer;
import me.symmettry.papyrus.util.commands.CommandRegistry;
import me.symmettry.papyrus.util.text.ComponentFormatter;
import me.symmettry.papyrus.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.graalvm.polyglot.HostAccess.Export;

import java.util.Map;

@SuppressWarnings("unused")
public final class ScriptServer {

    private final ScriptObj script;

    public ScriptServer(final ScriptObj script) {
        this.script = script;
    }

    @Export
    public final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    /**
     * Sends a message composed of the given values to the entire server.
     *
     * @param values The values to be broadcasted, which will be concatenated into a single string with spaces between them.
     */
    @Export
    public void broadcast(final Object... values) {
        System.out.println(Component.text(XCldrStub.join(values, " ")));
        Papyrus.inst.getServer().broadcast(Component.text(XCldrStub.join(values, " ")));
    }

    @Export
    public void broadcastFormatted(final Object... values) {
        Papyrus.inst.getServer().broadcast(ComponentFormatter.formatString(XCldrStub.join(values, " ")));
    }

    @Export
    public void broadcast(final Component value) {
        Papyrus.inst.getServer().broadcast(value);
    }

    /**
     * Registers a command with the given name, settings, and execution action.
     *
     * @param name The name of the command to register.
     * @param settings A map of settings for the command, such as { description: "Test command", usage: "/test", aliases: ["/testagain"], }.
     * @param execAction A function that handles the execution of the command.
     *                   This function is invoked with the CommandSender, the alias used, and the arguments of the command.
     */
    @Export
    public void registerCommand(final String name, final Map<String, Object> settings, final CommandConsumer execAction) {
        final ScriptedCommand cmd = new ScriptedCommand(name, settings, execAction);
        CommandRegistry.register(cmd);
        this.script.commands.add(cmd);
    }

    /**
     * Spawns an entity of the specified type at the given location.
     *
     * @param type The type of entity to spawn.
     * @param location The location where the entity should be spawned.
     * @return The spawned entity.
     */
    @Export
    public Entity spawnEntity(final EntityType type, final Location location) {
        return location.getWorld().spawnEntity(location, type);
    }

    /**
     * Spawns an entity of the specified type (given as a string) at the given location.
     * The type string is converted to the corresponding EntityType.
     *
     * @param type The type of entity to spawn, as a string.
     * @param location The location where the entity should be spawned.
     * @return The spawned entity.
     */
    @Export
    public Entity spawnEntity(final String type, final Location location) {
        return location.getWorld().spawnEntity(location, EntityType.valueOf(TextUtil.mojangName(type)));
    }

    @Export
    public Entity dropItem(final ItemStack item, final Location location) {
        return location.getWorld().dropItem(location, item);
    }

    @Export
    public Player getPlayer(final String name) {
        return Bukkit.getPlayer(name);
    }
    @Export
    public OfflinePlayer getOfflinePlayer(final String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    @Export
    public GameMode gameMode(final String string) {
        return GameMode.valueOf(TextUtil.mojangName(string));
    }

}