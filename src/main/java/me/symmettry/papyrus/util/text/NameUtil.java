package me.symmettry.papyrus.util.text;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NameUtil {

    public String mojangName(final String text) {
        return text.toUpperCase().replace("MINECRAFT:","").trim();
    }

}