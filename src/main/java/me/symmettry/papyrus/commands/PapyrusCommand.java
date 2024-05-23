package me.symmettry.papyrus.commands;

import lombok.SneakyThrows;
import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.script.ScriptManager;
import me.symmettry.papyrus.script.ScriptObj;
import me.symmettry.papyrus.util.text.ColorUtil;
import me.symmettry.papyrus.util.commands.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PapyrusCommand extends AbstractCommand {

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
            commandSender.sendMessage(ColorUtil.translateColorCodes("&cThat script does not exist."));
            return null;
        }
        return file;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        try {
            switch (args[0].toLowerCase()) {
                case "reload" -> {
                    if(args.length == 1) {
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &cYou must input a script."));
                        break;
                    }

                    if(args[1].equals("all")) {
                        final double now = System.nanoTime();
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aReloading all scripts..."));
                        ScriptManager.loadedScripts.values().forEach(ScriptObj::reload);
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aReloaded all scripts in " + ((System.nanoTime() - now) / 1_000_000) + "ms!"));
                        break;
                    }

                    final File file = getFile(args[1], commandSender);
                    if(file == null) break;

                    final String fileName = file.getPath().substring(ScriptManager.scriptsPath.length());

                    commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aReloading " + fileName + "..."));
                    final double now = System.nanoTime();
                    if(!ScriptManager.loadedScripts.containsKey(file.getPath())) {
                        ScriptManager.loadScript(fileName);
                    } else {
                        ScriptManager.loadedScripts.get(file.getPath()).reload();
                    }
                    commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aReloaded " + fileName + " in " + ((System.nanoTime() - now) / 1_000_000D) + "ms!"));
                }
                case "disable" -> {
                    if(args.length == 1) {
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &cYou must input a script."));
                        break;
                    }

                    if(args[1].equals("all")) {
                        ScriptManager.loadedScripts.values().forEach(obj -> obj.disable(true));
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aDisabled all enabled scripts!"));
                        break;
                    }

                    final File file = getFile(args[1], commandSender);
                    if(file == null) break;

                    if(ScriptManager.loadedScripts.containsKey(file.getPath())) {
                        ScriptManager.loadedScripts.get(file.getPath()).disable(true);
                    } else {
                        //noinspection ResultOfMethodCallIgnored
                        file.renameTo(new File(file.getPath() + ".disabled"));
                    }
                    commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aDisabled script!"));
                }
                case "enable" -> {
                    if(args.length == 1) {
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &cYou must input a script."));
                        break;
                    }

                    if(args[1].equals("all")) {
                        ScriptManager.loadedScripts.values().forEach(ScriptObj::enable);
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aEnabled all disabled scripts!"));
                        break;
                    }

                    final File file = getFile(args[1], commandSender);
                    if(file == null) break;

                    if(ScriptManager.loadedScripts.containsKey(file.getPath())) {
                        ScriptManager.loadedScripts.get(file.getPath()).enable();
                    } else if(file.getPath().endsWith(".disabled")){
                        //noinspection ResultOfMethodCallIgnored
                        file.renameTo(new File(file.getPath().substring(0, file.getPath().length() - 9)));
                        ScriptManager.loadScript(file.getPath().substring(ScriptManager.scriptsPath.length(), file.getPath().length() - 9));
                    }
                    commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &aEnabled script!"));
                }
                default ->
                        commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &cValid options are reload, enable, and disable."));
            }
        } catch (Exception e) {
            commandSender.sendMessage(ColorUtil.translateColorCodes("&6[Papyrus] &cAn error occured when modifying this script. Check console!"));
            e.printStackTrace();
        }
        return true;
    }

    @SneakyThrows
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String... args) throws IllegalArgumentException {

       return switch(args[0].toLowerCase()) {
           case "reload", "disable", "enable" -> {
                List<String> files = new ArrayList<>();
                ScriptManager.listFiles(files, args[0].equals("enable"));
                files.add("all");
                yield files;
           }
           default -> List.of("reload", "enable", "disable");
       };
    }

}
