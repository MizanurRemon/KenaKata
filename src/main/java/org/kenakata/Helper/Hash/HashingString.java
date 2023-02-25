package org.kenakata.Helper.Hash;

import java.security.MessageDigest;
import java.util.Base64;

public class HashingString {

    public static String passwordHashing(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
