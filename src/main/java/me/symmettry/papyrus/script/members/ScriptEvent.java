package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.script.PapyrusScriptException;
import me.symmettry.papyrus.script.ScriptObj;
import me.symmettry.papyrus.script.events.PapyrusEvent;
import me.symmettry.papyrus.script.listeners.BukkitListenerPair;
import me.symmettry.papyrus.script.listeners.PapyrusListenerPair;
import me.symmettry.papyrus.script.listeners.ScriptListenerManager;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.graalvm.polyglot.HostAccess.Export;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ScriptEvent {

    private final ScriptObj script;

    public ScriptEvent(final ScriptObj script) {
        this.script = script;
    }

    private final String[] eventPaths = { "block", "command", "enchantment", "entity", "hanging",
            "inventory", "player", "raid", "server", "vehicle", "weather", "world", "packet",
            "world.border", "brigadier", "entity", "executor", "executor.asm", "profile"};
    private final String[] eventFolders = { "com.destroystokyo.paper.event.", "io.papermc.paper.event.", "org.bukkit.event." };

    /**
     * Registers a function to run when a specific Bukkit and/or Paper event occurs.
     * <p>
     * This can be quite slow as it has to go through every folder to find if it exists, which can be slow if you want it fast.
     *
     * @param event The name of the event to listen for.
     * @param runOnEvent The function to execute when the event occurs.
     * @param <T> The type of the event.
     * @throws PapyrusScriptException If the event cannot be found.
     */
    @Export
    @SuppressWarnings("unchecked")
    public <T extends Event> void onBukkitEvent(final String event, final Consumer<T> runOnEvent) throws PapyrusScriptException {
        for(final String path : eventPaths) {
            for(final String folder : eventFolders) {
                try {
                    final Class<T> clazz = (Class<T>) Class.forName(folder + path + "." + event.replace("Event", "") + "Event", false, null);
                    ScriptListenerManager.addBukkitListener(script, new BukkitListenerPair<>(clazz, runOnEvent));
                    return;
                } catch (ClassNotFoundException ignored) {}
            }
        }
        throw new PapyrusScriptException("Unknown event: " + event);
    }

    /**
     * This is an event class that I seem to use frequently, so it will increase reload speeds to use.
     * <p>
     * It would be faster to do e.g. Events.onBukkitEvent(Events.MoveEvent, (e) => {}); instead of Events.onBukkitEvent("PlayerMove", (e) => {});
     * <p>
     * This is because all Papyrus needs to do in the first one is add the event; it doesn't have to find the class first.
     */
    @Export
    public final Class<? extends Event> InteractEvent = PlayerInteractEvent.class,
            MoveEvent = PlayerMoveEvent.class,
            DeathEvent = EntityDeathEvent.class,
            DamageEvent = EntityDamageByEntityEvent.class,
            InventoryClickEvent = InventoryClickEvent.class,
            SneakEvent = PlayerToggleSneakEvent.class,
            BreakEvent = BlockBreakEvent.class,
            PlaceEvent = BlockPlaceEvent.class,
            DropEvent = PlayerDropItemEvent.class,
            CommandEvent = PlayerCommandPreprocessEvent.class;

    /**
     * Registers a function to run when a specific Bukkit and/or Paper event occurs.
     * <p>
     * This instead uses the event class itself, so if an event is not found by text, it can be found with Java.type()
     *
     * @param event The class of the event to listen for.
     * @param runOnEvent The function to execute when the event occurs.
     * @param <T> The type of the event.
     */
    @Export
    public <T extends Event> void onBukkitEvent(final Class<T> event, final Consumer<T> runOnEvent) {
        ScriptListenerManager.addBukkitListener(script, new BukkitListenerPair<>(event, runOnEvent));
    }

    /**
     * Registers a runnable to execute when a specific Papyrus event occurs.
     *
     * @param event The name of the Papyrus event to listen for.
     * @param runOnEvent The function to execute when the event occurs.
     */
    @Export
    public void onPapyrusEvent(final String event, final Consumer<PapyrusEvent> runOnEvent) {
        ScriptListenerManager.addPapyrusListener(new PapyrusListenerPair(PapyrusEvent.PapyrusEventType.valueOf(event.toUpperCase().trim()), runOnEvent, script));
    }

}
