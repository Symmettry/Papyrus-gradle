package me.symmettry.papyrus.util.commands;

import org.bukkit.command.CommandSender;
import org.graalvm.polyglot.proxy.ProxyArray;

@FunctionalInterface
public interface CommandConsumer {
    void accept(final CommandSender sender, final String string, final ProxyArray args);
}
