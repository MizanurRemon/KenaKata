package org.kenakata.Helper.Phone;


import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class UserValidation {

    public static boolean checkNumber(String phone) {
        //System.out.println(phone +" "+ String.valueOf(phone.length()));
        String contactRegex = "[0-1]{2}[^012]{1}[0-9]{8}";

        if(!Pattern.matches(contactRegex, phone)){
            return false;
        }

        return true;
    }

    public static boolean checkName(String name) {

        if (name.length() < 5) {
            return false;
        }
        return true;
    }

    public static boolean checkPassword(String password) {

        String passwordRegex = "[0-9]{6}";
        if (!Pattern.matches(passwordRegex, password)) {
            return false;
        }
        return true;
    }

}
