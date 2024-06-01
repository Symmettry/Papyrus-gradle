package me.symmettry.papyrus.script.members;

import org.graalvm.polyglot.HostAccess.Export;

@SuppressWarnings("unused")
public final class ScriptReflect {

    /**
     * Helper function which returns the java class based on a string.
     *
     * @param type The fully qualified name of the desired class.
     * @return The Class at that string.
     * @throws ClassNotFoundException If the class does not exist.
     */
    @Export
    public Class<?> type(final String type) throws ClassNotFoundException {
        return Class.forName(type);
    }

}