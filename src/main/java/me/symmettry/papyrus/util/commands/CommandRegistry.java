package me.symmettry.papyrus.util.commands;

import lombok.experimental.UtilityClass;
import me.symmettry.papyrus.Papyrus;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

@UtilityClass
public final class CommandRegistry {

    private final Server server = Papyrus.inst.getServer();
    private final CommandMap cmdMap;

    static {
        try {
            final Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            cmdMap = (CommandMap) field.get(server);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void register(final AbstractCommand command) {
        command.getAliases().removeIf(StringUtils::isBlank);
        cmdMap.register(Papyrus.inst.getName(), command);
    }

    public void unregister(final AbstractCommand command) {
        command.unregister(cmdMap);
        cmdMap.getKnownCommands().remove(command.getName());
    }

}