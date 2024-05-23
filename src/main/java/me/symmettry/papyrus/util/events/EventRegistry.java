package me.symmettry.papyrus.util.events;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.script.events.PapyrusEvent;
import me.symmettry.papyrus.script.listeners.BukkitListenerPair;
import me.symmettry.papyrus.script.listeners.PapyrusListenerPair;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

@UtilityClass
public class EventRegistry {

    private final Map<BukkitListenerPair<? extends Event>, Listener> bukkitEventMap = new HashMap<>();
    private final Map<PapyrusEvent.PapyrusEventType, List<PapyrusListenerPair>> papyrusEventMap = new HashMap<>();

    @SneakyThrows
    public <T extends Event> void registerEvent(final BukkitListenerPair<T> pair) {
        final Class<T> eventClass = pair.event();
        final Consumer<T> runOnEvent = pair.runOnEvent();

        final Listener listener = new Listener() {
            public void onEvent(final T event) {
                runOnEvent.accept(event);
            }
        };
        final Method method = listener.getClass().getMethod("onEvent", Event.class);
        final EventExecutor executor = (listener1, event) -> {
            try {
                method.invoke(listener, event); // this will only listen for eventClass because it's defined to do so in getPluginManager().registerEvent()
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };

        Papyrus.inst.getServer().getPluginManager().registerEvent(eventClass, listener, EventPriority.NORMAL, executor, Papyrus.inst);

        bukkitEventMap.put(pair, listener);
    }
    public static void registerEvent(final PapyrusListenerPair listenerPair) {
        if (!papyrusEventMap.containsKey(listenerPair.event())) {
            papyrusEventMap.put(listenerPair.event(), new ArrayList<>());
        }
        papyrusEventMap.get(listenerPair.event()).add(listenerPair);
    }

    @SneakyThrows
    public <T extends Event> void unregister(final BukkitListenerPair<T> pair) {
        final Method getHandlerListMethod = pair.event().getMethod("getHandlerList");
        final HandlerList handlerList = (HandlerList) getHandlerListMethod.invoke(null);
        handlerList.unregister(bukkitEventMap.get(pair));
        bukkitEventMap.remove(pair);
    }
    public void unregister(final PapyrusListenerPair pair) {
        papyrusEventMap.getOrDefault(pair.event(), Collections.emptyList()).remove(pair);
    }

    @SuppressWarnings("unused")
    public void onPapyrusEvent(final PapyrusEvent.PapyrusEventType event, final PapyrusEvent args) {
        if(!papyrusEventMap.containsKey(event)) return;
        papyrusEventMap.get(event).forEach(listenerPair -> listenerPair.runOnEvent().accept(args));
    }

}
