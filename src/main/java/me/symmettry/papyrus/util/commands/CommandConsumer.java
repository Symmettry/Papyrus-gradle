package me.symmettry.papyrus.util.commands;

import me.symmettry.papyrus.script.data.PapyrusCommandSender;
import org.graalvm.polyglot.proxy.ProxyArray;

@FunctionalInterface
public interface CommandConsumer {
    void accept(final PapyrusCommandSender sender, final String string, final ProxyArray args);
}