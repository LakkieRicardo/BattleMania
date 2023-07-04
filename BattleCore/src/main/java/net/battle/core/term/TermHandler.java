package net.battle.core.term;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TermHandler {
    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] result = digest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xFF) + 256, 16).substring(1));
        }
        return sb.toString();
    }
}