package org.kenakata.Helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogText {


    public static void logd(String text){
        Logger logger = LoggerFactory.getLogger(LogText.class);
        logger.debug(text);
    }
}
