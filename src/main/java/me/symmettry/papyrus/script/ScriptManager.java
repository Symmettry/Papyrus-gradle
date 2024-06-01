package me.symmettry.papyrus.script;

import lombok.SneakyThrows;
import me.symmettry.papyrus.Papyrus;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.graalvm.polyglot.Context;

public final class ScriptManager {

    public static HashMap<String, ScriptObj> loadedScripts = new HashMap<>();
    public static final String scriptsPath = Papyrus.inst.pluginPath + "scripts\\";

    public static void loadScripts() {
        final List<String> scripts = new ArrayList<>();
        listFiles(scripts, false);
        if(scripts.isEmpty()) {
            try(final Context context = Context.newBuilder("js").build()) {
                context.eval("js", ""); // load graalvm so that when u reload it doesn't take 10 seconds first time.
            }
        } else {
            scripts.forEach(ScriptManager::loadScript);
        }
    }

    public static void loadScript(final String path) {
        final ScriptObj obj;
        try {
            obj = new ScriptObj(scriptsPath + path);
            loadedScripts.put(scriptsPath + path, obj);
            obj.enable();
        } catch (Exception e) {
            Papyrus.LOGGER.severe("Could not read script (" + path + "):");
            e.printStackTrace();
        }
    }

    private static boolean isValid(final Path file, final boolean disabled) {
        String name = file.toString();
        if(!disabled) name += ".disabled";
        return name.endsWith(".pap.disabled") || name.endsWith(".js.disabled");
    }

    @SneakyThrows
    public static void listFiles(final List<String> fileList, final boolean disabled) {
        Files.walkFileTree(Path.of(scriptsPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if(!isValid(file, disabled)) return FileVisitResult.CONTINUE;
                final String fileName = file.toString().substring(scriptsPath.length());
                if(fileName.startsWith("all.")) {
                    throw new RuntimeException("Scripts cannot be named \"all\"");
                }
                fileList.add(fileName);
                return FileVisitResult.CONTINUE;
            }
        });
    }


}