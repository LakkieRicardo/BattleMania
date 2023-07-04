package net.battle.core.handlers;

import java.util.Arrays;
import java.util.List;

public class SwearHandler {
    public static final int PROCESS_MODE_STAR = 0;
    public static final int PROCESS_MODE_UNDERLINE = 1;
    public static final List<String> whitelist = Arrays.asList(new String[] { "embarrassed", "embarrass" });

    @Deprecated
    public static String getProtectedString(String string) {
        String[] words;
        if (!string.contains(" ")) {
            words = new String[1];
            words[0] = string;
        } else {
            words = string.split(" ");
        }
        String[] pWords = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            pWords[i] = protectString(words[i], '*') + " ";
        }
        return StringUtility.assemble(pWords);
    }

    public static String protectString(String string, char replacement) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            newString.append(replacement);
        }
        string = new String(newString);
        return string;
    }

    public static String processString(int mode, String algorithmKey, String sequence) {
        if (SwearSearchAlgorithm.getSSA(algorithmKey) == null) {
            BMLogger.warning("Could not find a SSA by the name of " + algorithmKey);
            return sequence;
        }
        if (!sequence.contains(" ")) {
            return processWord(mode, sequence, SwearSearchAlgorithm.getSSA(algorithmKey));
        }
        String[] words = sequence.split(" ");
        String result = "";
        byte b;
        int i;
        String[] arrayOfString1;
        for (i = (arrayOfString1 = words).length, b = 0; b < i;) {
            String word = arrayOfString1[b];
            result = result + processWord(mode, word, SwearSearchAlgorithm.getSSA(algorithmKey)) + " ";
            b++;
        }

        return result.trim();
    }

    private static String processWord(int mode, String word, SwearSearchAlgorithm algorithm) {
        if (whitelist.contains(word.toLowerCase())) {
            return word;
        }
        if (algorithm.isSwearWord(word)) {
            if (mode == 0) {
                return protectString(word, '*');
            }
            return "§n" + word + "§f";
        }

        return word;
    }

    @Deprecated
    public static boolean isSwear(String string) {
        return (StringUtility.getSimilarSwears(string, 0.8D).size() > 0);
    }
}