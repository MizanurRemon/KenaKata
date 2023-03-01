package org.kenakata.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeDateFormat {

    public static String ChangeDateFormat(String date, String oldFormat, String newFormat) throws ParseException {
        String formattedDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        Date d = sdf.parse(date);
        sdf.applyPattern(newFormat);
        formattedDate = sdf.format(d);
        return formattedDate;

    }
}
