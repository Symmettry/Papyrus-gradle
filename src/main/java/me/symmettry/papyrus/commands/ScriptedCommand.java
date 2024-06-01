package me.symmettry.papyrus.commands;

import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.script.data.PapyrusCommandSender;
import me.symmettry.papyrus.util.commands.AbstractCommand;
import me.symmettry.papyrus.util.commands.CommandConsumer;
import me.symmettry.papyrus.util.text.ComponentFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ScriptedCommand extends AbstractCommand {

    private final CommandConsumer execAction;
    private final String permission;
    private final Component permissionMessage;

    @SuppressWarnings("unchecked")
    public ScriptedCommand(final String name, final Map<String, Object> settings, final CommandConsumer execAction) {
        super(name, (String) settings.getOrDefault("description", ""),
                (String) settings.getOrDefault("usage", ""),
                new ArrayList<>((List<String>) settings.getOrDefault("aliases", Collections.emptyList())));

        this.execAction = execAction;
        this.permission = (String) settings.getOrDefault("permission", "");
        final String permMsg = (String) settings.getOrDefault("permission_message", null);
        this.permissionMessage = permMsg == null ? Papyrus.inst.getServer().permissionMessage() : ComponentFormatter.formatString(permMsg);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String... args) {
        if(sender.hasPermission(this.permission)) {
            execAction.accept(new PapyrusCommandSender(sender), commandLabel, ProxyArray.fromArray((Object[]) args));
        } else {
            sender.sendMessage(this.permissionMessage);
        }
        return true;
    }

}