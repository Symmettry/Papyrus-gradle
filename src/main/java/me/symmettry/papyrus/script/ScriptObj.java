package me.symmettry.papyrus.script;

import lombok.SneakyThrows;
import me.symmettry.papyrus.commands.ScriptedCommand;
import me.symmettry.papyrus.script.listeners.ScriptListenerManager;
import me.symmettry.papyrus.script.members.*;
import me.symmettry.papyrus.util.commands.CommandRegistry;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ScriptObj {

    public String data, path;
    private Context context;

    public final List<ScriptedCommand> commands = new ArrayList<>();

    public ScriptObj(final String path) throws IOException {
        this.path = path;
        this.updateData();
    }
    public void updateData() throws IOException {
        this.data = new String(Files.readAllBytes(Path.of(path)));
    }

    public Context generateContext() {
        final Context context = Context.newBuilder("js").allowHostAccess(HostAccess.ALL).allowHostClassLookup(className -> true).build();
        final Value bindings = context.getBindings("js");

        bindings.putMember("Item", new ScriptItem());
        bindings.putMember("Server", new ScriptServer(this));
        bindings.putMember("World", new ScriptWorld());
        bindings.putMember("Data", new ScriptData());
        bindings.putMember("Events", new ScriptEvent(this));
        bindings.putMember("Gui", new ScriptGui());
        bindings.putMember("Text", new ScriptText());

        bindings.putMember("Entity", Entity.class);
        bindings.putMember("Material", Material.class);
        bindings.putMember("ItemStack", ItemStack.class);
        bindings.putMember("AttributeModifier", AttributeModifier.class);
        bindings.putMember("AttributeOperation", AttributeModifier.Operation.class);
        bindings.putMember("Attribute", Attribute.class);

        // skript-reflect in 1 line !!!!
        bindings.putMember("Java", new ScriptReflect());

        // something not in Papyrus? no problem! :3
        bindings.putMember("this", this);

        return context;
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final String autoSaveValue = """
        class AutoSaveValue {
            constructor(id, defaultValue) {
                this._value = Data.get(id, defaultValue);
                this._id = id;
            }
            set value(value) {
                this._value = value;
                Data.save(this._id, this._value);
            }
            get value() {
                return this._value;
            }
        }
    """;

    @SneakyThrows
    public void enable() {
        if(context != null) return; // don't enable if already enabled
        if(this.path.endsWith(".disabled")) {
            this.path = this.path.substring(0, this.path.length() - 9);
        }
        this.updateData();
        try {
            this.context = this.generateContext();

            context.eval("js", autoSaveValue + this.data);
        } catch(final PolyglotException e) {
            e.printStackTrace();
        }
    }
    public void disable(boolean rename) {
        if(context == null) return; // don't disable if already disabled

        commands.forEach(CommandRegistry::unregister);

        if(rename) //noinspection ResultOfMethodCallIgnored
            new File(this.path).renameTo(new File(this.path + ".disabled"));
        this.path += ".disabled";
        ScriptListenerManager.removeScript(this);
        this.context.close();
        this.context = null;
    }

    @SneakyThrows
    public void reload() {
        this.disable(false);
        this.enable();
    }

}