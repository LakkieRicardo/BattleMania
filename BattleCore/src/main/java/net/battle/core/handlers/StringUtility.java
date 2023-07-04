package net.battle.core.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtility {
    public static String assemble(String[] components) {
        String word = "";
        for (int i = 0; i < components.length; i++) {
            String s = components[i];
            word = word + s;
        }

        return word;
    }

    public static String assemble(List<String> components) {
        return assemble(components, 0, components.size(), "");
    }

    public static String assemble(List<String> components, int offset, int readLength, String divider) {
        components = components.subList(offset, readLength);
        String result = "";
        for (String string : components) {
            result = result + divider + string;
        }
        result = result.replaceFirst(divider, "");
        return result;
    }

    public static String assemble(String[] components, int offset, int readLength, String divider) {
        return assemble(Arrays.asList(components), offset, readLength, divider);
    }

    public static String assemble(String[] components, String divider) {
        return assemble(Arrays.asList(components), 0, components.length, divider);
    }

    @Deprecated
    public static List<String> getSimilarSwears(String word, double percent) {
        List<String> matches = new ArrayList<>();
        char[] wordChars = word.toCharArray();
        for (int i = 0; i < BasicSwearAlgorithm.SWEARS.size(); i++) {
            String s = BasicSwearAlgorithm.SWEARS.get(i);
            char[] chars = s.toCharArray();
            int foo = 0;
            int targetMatches = (int) (((chars.length + wordChars.length) / 2) * percent);
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                for (int k = 0; k < wordChars.length; k++) {
                    char wc = wordChars[k];
                    if (c == wc) {
                        foo++;
                    }
                }
            }

            if (foo >= targetMatches) {
                matches.add(s);
            }
        }

        return matches;
    }
}