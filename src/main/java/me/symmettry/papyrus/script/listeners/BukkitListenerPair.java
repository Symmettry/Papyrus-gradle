package me.symmettry.papyrus.script.listeners;

import org.bukkit.event.Event;

import java.util.function.Consumer;

public record BukkitListenerPair<T extends Event>(Class<T> event, Consumer<T> runOnEvent) {}