package me.symmettry.papyrus.script.listeners;

import me.symmettry.papyrus.script.ScriptObj;
import me.symmettry.papyrus.script.events.PapyrusEvent;

import java.util.function.Consumer;

public record PapyrusListenerPair(PapyrusEvent.PapyrusEventType event, Consumer<PapyrusEvent> runOnEvent, ScriptObj script) {}