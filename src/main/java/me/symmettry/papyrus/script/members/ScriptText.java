package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.util.text.ComponentFormatter;
import net.kyori.adventure.text.Component;
import org.graalvm.polyglot.HostAccess.Export;

@SuppressWarnings("unused")
public class ScriptText {

    @Export
    public Component format(final String string) {
        return ComponentFormatter.formatString(string);
    }

    // Text.deformat(item.displayName()) == "&c&lHEART"
    @Export
    public String deformat(final Component component) {
        return ComponentFormatter.deformatComponent(component);
    }

}
