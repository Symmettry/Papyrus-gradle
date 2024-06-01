package me.symmettry.papyrus.util.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;

@UtilityClass
public class TextUtil {

    public String mojangName(final String text) {
        return text.toUpperCase().replace("MINECRAFT:","").trim();
    }
    public Component papyrusPrefix() {
        return Component.text("[Papyrus]", NamedTextColor.GOLD);
    }
    public void notify(final CommandSender sender, final Component msg) {
        sender.sendMessage(Component.empty()
                .append(papyrusPrefix())
                .append(Component.space())
                .append(msg)
        );
    }

    public void notify(final CommandSender sender, final String msg, final TextColor color) {
        notify(sender, Component.text(msg, color));
    }

    public void success(final CommandSender sender, final String msg) {
        notify(sender, msg, NamedTextColor.GREEN);
    }

    public void error(final CommandSender sender, final String msg) {
        notify(sender, msg, NamedTextColor.RED);
    }

}
