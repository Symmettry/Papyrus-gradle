package me.symmettry.papyrus.util.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@SuppressWarnings("unused")
public abstract class AbstractCommand extends Command {
    public AbstractCommand(@NotNull Object name, @NotNull Object description, @NotNull Object usageMessage, @NotNull List<String> aliases) {
        super(String.valueOf(name), String.valueOf(description), String.valueOf(usageMessage), Stream.of(aliases).map(String::valueOf).toList());
    }
    public AbstractCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull String... aliases) {
        super(name, description, usageMessage, List.of(aliases));
    }
    public AbstractCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
    public AbstractCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage) {
        super(name, description, usageMessage, Collections.emptyList());
    }
    public AbstractCommand(@NotNull String name) {
        super(name);
    }

}