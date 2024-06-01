package me.symmettry.papyrus.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

public final class ComponentFormatter {

    private static final Map<Character, NamedTextColor> COLOR_MAP = Map.ofEntries(
            entry('0', BLACK),
            entry('1', DARK_BLUE),
            entry('2', DARK_GREEN),
            entry('3', DARK_AQUA),
            entry('4', DARK_RED),
            entry('5', DARK_PURPLE),
            entry('6', GOLD),
            entry('7', GRAY),
            entry('8', DARK_GRAY),
            entry('9', BLUE),
            entry('a', GREEN),
            entry('b', AQUA),
            entry('c', RED),
            entry('d', LIGHT_PURPLE),
            entry('e', YELLOW),
            entry('f', WHITE)
    );
    private static final Map<NamedTextColor, Character> INVERTED_COLOR_MAP = invertMap(COLOR_MAP);

    private static final Map<Character, TextDecoration> DECORATION_MAP = Map.ofEntries(
            entry('k', OBFUSCATED),
            entry('l', BOLD),
            entry('m', STRIKETHROUGH),
            entry('n', UNDERLINED),
            entry('o', ITALIC)
    );
    private static final Map<TextDecoration, Character> INVERTED_DECORATION_MAP = invertMap(DECORATION_MAP);

    private static <K, V> Map<V, K> invertMap(Map<K, V> input) {
        return input.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (k1, k2) -> k1));
    }

    private static final class Formatter {

        private final char[] chars;
        private int i = 0;
        private final Set<TextDecoration> decor = new HashSet<>();
        private TextColor color = WHITE;
        private final StringBuilder text = new StringBuilder();

        public Formatter(final String string) {
            this.chars = string.toCharArray();
        }

        public Component format() {
            final TextComponent.Builder builder = Component.text();

            for (; i < chars.length; i++) {
                final char c = chars[i];
                if (c != '&') {
                    text.append(c);
                    continue;
                }
                builder.append(Component.text(text.toString(), color, decor));
                text.setLength(0);
                if (i == chars.length - 1) {
                    text.append(c);
                    continue;
                }

                final char nextChar = Character.toLowerCase(chars[i + 1]);
                if (COLOR_MAP.containsKey(nextChar)) {
                    color = COLOR_MAP.get(nextChar);
                    decor.clear();
                    i++;
                    continue;
                } else if (DECORATION_MAP.containsKey(nextChar)) {
                    decor.add(DECORATION_MAP.get(nextChar));
                    i++;
                    continue;
                } else if (nextChar == 'r') {
                    decor.clear();
                    color = WHITE;
                    i++;
                    continue;
                } else if (nextChar == 'x') {
                    parseHex();
                    continue;
                }
                text.append(c);
            }
            builder.append(Component.text(text.toString(), color, decor));
            return builder.build();
        }

        private int j = 0;

        private void parseHex() {
            j = 0;
            i += 2;

            final String hex = parseHexInsides();

            if (hex.length() == 6) {
                color = TextColor.color(Integer.parseInt(hex, 16));
            } else {
                text.append('&').append(chars[i - 1]).append(Arrays.stream(hex.split("")).map(value -> '&' + value).collect(Collectors.joining()));
            }
            i += j - 1;
        }

        private String parseHexInsides() {
            final StringBuilder hex = new StringBuilder();
            for (; j < 12; j++) {
                if (i + j >= chars.length) return hex.toString();

                final char nextHex = chars[i + j];
                if (nextHex != '&') return hex.toString();

                j++;

                if (i + j >= chars.length) return hex.toString();
                final char hexValue = Character.toLowerCase(chars[i + j]);


                if (!COLOR_MAP.containsKey(hexValue)) return hex.toString();
                hex.append(hexValue);
            }
            return hex.toString();
        }

    }

    private record Deformatter(Component component) {

        public String format() {
            // TODO: todo
            final StringBuilder builder = new StringBuilder();
            return "abc";
        }

    }

    public static Component formatString(final String string) {
        return new Formatter(string).format();
    }

    public static String deformatComponent(final Component component) {
        return new Deformatter(component).format();
    }

}