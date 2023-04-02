package org.kenakata.Helper.Phone;


import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class UserValidation {

    public static boolean checkNumber(String phone) {
        //System.out.println(phone +" "+ String.valueOf(phone.length()));
        String contactRegex = "[0-1]{2}[^012]{1}[0-9]{8}";

        return Pattern.matches(contactRegex, phone);
    }

    public static boolean checkName(String name) {

        return name.length() >= 5;
    }

    public static boolean checkPassword(String password) {

        String passwordRegex = "[0-9]{6}";
        return Pattern.matches(passwordRegex, password);
    }

}
