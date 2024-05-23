package me.symmettry.papyrus.commands;

import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.util.commands.AbstractCommand;
import me.symmettry.papyrus.util.commands.CommandConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ScriptedCommand extends AbstractCommand {

    private final CommandConsumer execAction;
    private final String permission;
    private final Component permissionMessage;

    public ScriptedCommand(final String name, final Map<String, Object> settings, final CommandConsumer execAction) {
        super(name, settings.getOrDefault("description", ""),
                settings.getOrDefault("usage", ""),
                settings.getOrDefault("aliases", new String[]{}));
        this.execAction = execAction;
        this.permission = (String) settings.getOrDefault("permission", "");
        this.permissionMessage = (Component) settings.getOrDefault("permission_message", Papyrus.inst.getServer().permissionMessage());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if(sender.hasPermission(this.permission)) {
            execAction.accept(sender, commandLabel, ProxyArray.fromArray((Object[]) args));
        } else {
            sender.sendMessage(this.permissionMessage);
        }
        return true;
    }

}
