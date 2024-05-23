package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.util.data.Database;
import org.graalvm.polyglot.HostAccess.Export;

@SuppressWarnings("unused")
public class ScriptData {

    @Export
    public <T> T save(final String id, final T obj) {
        return Database.saveData(id, obj);
    }

    @Export
    public Object get(final String id) {
        return Database.getData(id, null);
    }
    @Export
    public Object get(final String id, final Object def) {
        return Database.getData(id, def);
    }

}
