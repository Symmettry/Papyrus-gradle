package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.util.data.Database;
import org.graalvm.polyglot.HostAccess.Export;

/**
 * This class provides methods for saving and retrieving data using a database.
 */
@SuppressWarnings("unused")
public final class ScriptData {

    /**
     * Saves the provided object with the given ID to the database.
     *
     * @param id  The identifier under which the data will be saved.
     * @param obj The object to be saved.
     * @param <T> The type of the object being saved.
     * @return The saved object.
     */
    @Export
    public <T> T save(final String id, final T obj) {
        return Database.saveData(id, obj);
    }

    /**
     * Retrieves the object stored in the database with the given ID, or returns a default value if no such object exists.
     *
     * @param id  The identifier of the data to retrieve.
     * @param def The default value to return if no object is found with the provided ID.
     * @return The object stored in the database with the provided ID, or the default value if no such object exists.
     */
    @Export
    public Object get(final String id, final Object def) {
        return Database.getData(id, def);
    }

}
