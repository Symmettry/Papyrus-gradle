package me.symmettry.papyrus.commands;

import lombok.SneakyThrows;
import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.script.ScriptManager;
import me.symmettry.papyrus.script.ScriptObj;
import me.symmettry.papyrus.util.commands.AbstractCommand;
import me.symmettry.papyrus.util.text.TextUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class PapyrusCommand extends AbstractCommand {

    public PapyrusCommand() {
        super("papyrus", "Command for Papyrus.", "/papyrus <reload/enable/disable>", "pap");
    }

    private File getFile(final String arg, final CommandSender commandSender) {
        File file;
        final String scriptPath = Papyrus.inst.pluginPath + "scripts\\" + arg;
        if(arg.endsWith(".pap") || arg.endsWith(".pap.disabled") || arg.endsWith(".js") || arg.endsWith(".js.disabled")) {
            file = new File(scriptPath);
        } else {
            file = new File(scriptPath + ".pap");
            if(!file.exists()) {
                file = new File(scriptPath + ".js");
            }
        }
        if(!file.exists()) {
            TextUtil.error(commandSender, "That script does not exist.");
            return null;
        }
        return file;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String... args) {
        if(args.length == 0) {
            TextUtil.error(commandSender, "Valid options are reload, enable, and disable.");
            return false;
        }
        try {
            return switch (args[0].toLowerCase()) {
                case "reload" -> reload(commandSender, args);
                case "disable" -> disable(commandSender, args);
                case "enable" -> enable(commandSender, args);
                default -> {
                    TextUtil.error(commandSender, "Valid options are reload, enable, and disable.");
                    yield false;
                }
            };
        } catch (Exception e) {
            TextUtil.error(commandSender, "An error occurred when modifying this script. Check console!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasNoPermission(CommandSender commandSender, String permission) {
        if(!commandSender.hasPermission(permission)) {
            TextUtil.error(commandSender, "You do not have the required permission to run this command!");
            return true;
        }
        return false;
    }

    public boolean reload(final CommandSender commandSender, final String... args) {
        if(hasNoPermission(commandSender, "papyrus.reload")) return false;

        if(args.length == 1) {
            TextUtil.error(commandSender, "You must input a script.");
            return false;
        }

        if(args[1].equals("all")) {
            final long now = System.nanoTime();
            TextUtil.success(commandSender, "Reloading all scripts...");
            ScriptManager.loadedScripts.values().forEach(ScriptObj::reload);
            TextUtil.success(commandSender, "Reloaded all scripts in " + ((System.nanoTime() - now) / 1_000_000D) + "ms!");
            return false;
        }

        final File file = getFile(args[1], commandSender);
        if(file == null) return false;

        final String fileName = file.getPath().substring(ScriptManager.scriptsPath.length());

        TextUtil.success(commandSender, "Reloading " + fileName + "...");
        final long now = System.nanoTime();
        if(!ScriptManager.loadedScripts.containsKey(file.getPath())) ScriptManager.loadScript(fileName);
        else ScriptManager.loadedScripts.get(file.getPath()).reload();

        TextUtil.success(commandSender, "Reloaded " + fileName + " in " + ((System.nanoTime() - now) / 1_000_000D) + "ms!");
        return true;
    }

    public boolean disable(final CommandSender commandSender, final String... args) {
        if(hasNoPermission(commandSender, "papyrus.disable")) return false;

        if(args.length == 1) {
            TextUtil.error(commandSender, "You must input a script.");
            return false;
        }

        if(args[1].equals("all")) {
            ScriptManager.loadedScripts.values().forEach(obj -> obj.disable(true));
            TextUtil.success(commandSender, "Disabled all enabled scripts!");
            return false;
        }

        final File file = getFile(args[1], commandSender);
        if(file == null) return false;

        if(ScriptManager.loadedScripts.containsKey(file.getPath())) {
            ScriptManager.loadedScripts.get(file.getPath()).disable(true);
        } else {
            //noinspection ResultOfMethodCallIgnored
            file.renameTo(new File(file.getPath() + ".disabled"));
        }
        TextUtil.success(commandSender, "Disabled script!");
        return true;
    }
    public boolean enable(final CommandSender commandSender, final String... args) {
        if(hasNoPermission(commandSender, "papyrus.enable")) return false;

        if(args.length == 1) {
            TextUtil.error(commandSender, "You must input a script.");
            return false;
        }

        if(args[1].equals("all")) {
            ScriptManager.loadedScripts.values().forEach(ScriptObj::enable);
            TextUtil.success(commandSender, "Enabled all disabled scripts!");
            return false;
        }

        final File file = getFile(args[1], commandSender);
        if(file == null) return false;

        if(ScriptManager.loadedScripts.containsKey(file.getPath())) {
            ScriptManager.loadedScripts.get(file.getPath()).enable();
        } else if(file.getPath().endsWith(".disabled")) {
            //noinspection ResultOfMethodCallIgnored
            file.renameTo(new File(file.getPath().substring(0, file.getPath().length() - 9)));
            ScriptManager.loadScript(file.getPath().substring(ScriptManager.scriptsPath.length(), file.getPath().length() - 9));
        }
        TextUtil.success(commandSender, "Enabled script!");
        return true;
    }

    @SneakyThrows
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String... args) throws IllegalArgumentException {
       return switch(args[0].toLowerCase()) {
           case "reload", "disable", "enable" -> {
                final List<String> files = new ArrayList<>();
                ScriptManager.listFiles(files, args[0].equals("enable"));
                files.add("all");
                yield files;
           }
           default -> List.of("reload", "enable", "disable");
       };
    }

}