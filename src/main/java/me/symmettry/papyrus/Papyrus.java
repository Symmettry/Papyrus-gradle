package me.symmettry.papyrus;

import lombok.SneakyThrows;
import me.symmettry.papyrus.commands.PapyrusCommand;
import me.symmettry.papyrus.listeners.InventoryListener;
import me.symmettry.papyrus.script.ScriptManager;
import me.symmettry.papyrus.util.commands.CommandRegistry;
import me.symmettry.papyrus.util.data.Database;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class Papyrus extends JavaPlugin {

    public static Papyrus inst;
    public static Logger LOGGER;
    public final String pluginPath = this.getDataFolder().getAbsolutePath() + "\\";

    private void createFolderIfNotExists(final String path) {
        final File file = new File(pluginPath + path);
        if(!file.exists()) {
            file.mkdir();
        }
    }

    @Override @SneakyThrows
    public void onEnable() {
        System.getProperties().setProperty("polyglot.engine.WarnInterpreterOnly", "false");

        inst = this;
        LOGGER = this.getLogger();

        createFolderIfNotExists("");
        createFolderIfNotExists("scripts");
        createFolderIfNotExists("backups");
        createFolderIfNotExists("corrupted");

        Database.loadDatabase();

        LOGGER.info("Loading engine & scripts...");
        ScriptManager.loadScripts();
        LOGGER.info("Loaded engine & scripts.");

        CommandRegistry.register(new PapyrusCommand());

        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, Database::saveDatabase, 1200L, 1200L);
    }

    @Override @SneakyThrows
    public void onDisable() {
        Database.saveDatabase();
    }

}