package me.symmettry.papyrus.util.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

@UtilityClass
public class ColorUtil {

    public String translateColorCodes(final String string) {
        return string.replace('&', '§');
    }

    public Component colorText(final String string) {
        return Component.text(translateColorCodes(string));
    }

}
