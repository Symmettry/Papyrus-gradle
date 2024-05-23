package me.symmettry.papyrus.script.listeners;

import me.symmettry.papyrus.script.ScriptObj;
import me.symmettry.papyrus.util.events.EventRegistry;
import org.bukkit.event.Event;

import java.util.*;

public class ScriptListenerManager {

    private static final Map<ScriptObj, List<BukkitListenerPair<? extends Event>>> bukkitListenerMap = new HashMap<>();
    private static final Map<ScriptObj, List<PapyrusListenerPair>> papyrusListenerMap = new HashMap<>();

    public static <T extends Event> void addBukkitListener(final ScriptObj script, final BukkitListenerPair<T> listenerPair) {
        if (!bukkitListenerMap.containsKey(script)) {
            bukkitListenerMap.put(script, new ArrayList<>());
        }
        bukkitListenerMap.get(script).add(listenerPair);
        EventRegistry.registerEvent(listenerPair);
    }

    public static void addPapyrusListener(final PapyrusListenerPair listenerPair) {
        if (!papyrusListenerMap.containsKey(listenerPair.script())) {
            papyrusListenerMap.put(listenerPair.script(), new ArrayList<>());
        }
        papyrusListenerMap.get(listenerPair.script()).add(listenerPair);
        EventRegistry.registerEvent(listenerPair);
    }

    public static void removeScript(final ScriptObj script) {
        bukkitListenerMap.getOrDefault(script, Collections.emptyList()).forEach(EventRegistry::unregister);
        bukkitListenerMap.remove(script);
        papyrusListenerMap.getOrDefault(script, Collections.emptyList()).forEach(EventRegistry::unregister);
        papyrusListenerMap.remove(script);
    }

}
